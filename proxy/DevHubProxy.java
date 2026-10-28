import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Dev Utility Hub 로컬 프록시 (선택 사항)
 *
 * 브라우저의 CORS · 금지 헤더(Cookie, Host 등) · 자체 서명 인증서 제약 없이
 * HTTP API 테스터에서 요청을 보낼 수 있게 해주는 개발용 프록시입니다.
 * 외부 라이브러리 없이 JDK 11 이상에서 바로 실행됩니다.
 *
 *   java DevHubProxy.java                       (컴파일 없이 실행, JDK 11+)
 *   java DevHubProxy.java --port=8099 --token=mytoken
 *   java DevHubProxy.java --allow-insecure      (자체 서명 인증서 허용 — 개발용)
 *
 * 옵션
 *   --port=8099          수신 포트
 *   --bind=127.0.0.1     수신 주소 (기본: 이 PC에서만 접근 가능)
 *   --token=문자열       접근 토큰 고정 (기본: 실행할 때마다 랜덤 생성)
 *   --no-token           토큰 검사 생략 (권장하지 않음)
 *   --allow-origin=URL   추가로 허용할 페이지 출처 (쉼표 구분, * = 모두)
 *   --allow-insecure     요청별 "인증서 검증 안 함" 옵션 허용 (호스트명 검증도 꺼짐)
 *   --http2              HTTP/2 사용 (기본 HTTP/1.1)
 *
 * 보안: 기본적으로 127.0.0.1에만 바인딩하고, file:// 또는 localhost 페이지에서 온 요청만 허용하며,
 *       실행 시 출력되는 토큰을 X-DevHub-Token 헤더로 확인합니다. 운영 서버에 띄우지 마세요.
 */
public class DevHubProxy {
    static final String VERSION = "1.0.0";
    static final int MAX_BODY = 50 * 1024 * 1024;
    static int port = 8099;
    static String bind = "127.0.0.1";
    static String token = null;
    static boolean noToken = false;
    static boolean allowInsecure = false;
    static boolean http2 = false;
    static final List<String> extraOrigins = new ArrayList<>();
    static final Map<String, HttpClient> clients = new ConcurrentHashMap<>();
    static final Set<String> SKIP_REQ = Set.of("content-length", "connection", "expect", "upgrade", "keep-alive", "transfer-encoding", "te", "trailer", "proxy-connection", "http2-settings");

    public static void main(String[] args) throws Exception {
        for (String a : args) {
            if (a.startsWith("--port=")) port = Integer.parseInt(a.substring(7));
            else if (a.startsWith("--bind=")) bind = a.substring(7);
            else if (a.startsWith("--token=")) token = a.substring(8);
            else if (a.equals("--no-token")) noToken = true;
            else if (a.startsWith("--allow-origin=")) for (String o : a.substring(15).split(",")) extraOrigins.add(o.trim());
            else if (a.equals("--allow-insecure")) allowInsecure = true;
            else if (a.equals("--http2")) http2 = true;
            else if (a.equals("-h") || a.equals("--help")) { usage(); return; }
            else { System.err.println("알 수 없는 옵션: " + a); usage(); System.exit(1); }
        }
        // Host 헤더를 직접 지정할 수 있도록 허용 (HttpClient 기본 제한 해제)
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");
        if (allowInsecure) System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        if (token == null && !noToken) {
            byte[] b = new byte[9];
            new SecureRandom().nextBytes(b);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(bind, port), 0);
        server.createContext("/health", DevHubProxy::health);
        server.createContext("/proxy", DevHubProxy::proxy);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("==============================================");
        System.out.println(" Dev Utility Hub 로컬 프록시 v" + VERSION);
        System.out.println(" 주소   : http://" + bind + ":" + port + "/proxy");
        System.out.println(" 토큰   : " + (noToken ? "(검사 안 함)" : token));
        System.out.println(" 인증서 : " + (allowInsecure ? "검증 생략 허용 (--allow-insecure)" : "검증함"));
        System.out.println("----------------------------------------------");
        System.out.println(" HTML → HTTP API 테스터 → [설정] 탭에서");
        System.out.println(" '로컬 프록시 경유'를 선택하고 위 주소와 토큰을 입력하세요.");
        System.out.println(" 종료: Ctrl+C");
        System.out.println("==============================================");
    }

    static void usage() {
        System.out.println("사용법: java DevHubProxy.java [--port=8099] [--bind=127.0.0.1] [--token=값 | --no-token] [--allow-origin=URL,...] [--allow-insecure] [--http2]");
    }

    /* ---------- 공통: CORS · 인증 ---------- */
    static boolean originAllowed(String origin) {
        if (origin == null || origin.equals("null")) return true;
        if (extraOrigins.contains("*") || extraOrigins.contains(origin)) return true;
        return origin.matches("^https?://(localhost|127\\.0\\.0\\.1|\\[::1\\])(:\\d+)?$");
    }

    /** CORS 헤더 설정 + 사전 검사. false면 응답이 이미 전송됨 */
    static boolean guard(HttpExchange ex) throws IOException {
        String origin = ex.getRequestHeaders().getFirst("Origin");
        Headers h = ex.getResponseHeaders();
        if (!originAllowed(origin)) { sendJson(ex, 403, "{\"error\":\"허용되지 않은 출처입니다: " + esc(origin) + " (--allow-origin 옵션 참고)\"}"); return false; }
        if (origin != null) {
            h.set("Access-Control-Allow-Origin", origin);
            h.set("Vary", "Origin");
        }
        h.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        h.set("Access-Control-Allow-Headers", "Content-Type, X-DevHub-Token");
        h.set("Access-Control-Allow-Private-Network", "true");
        h.set("Access-Control-Max-Age", "600");
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(204, -1); ex.close(); return false; }
        if (!noToken && !token.equals(ex.getRequestHeaders().getFirst("X-DevHub-Token"))) {
            sendJson(ex, 401, "{\"error\":\"프록시 토큰이 올바르지 않습니다. 프록시 콘솔에 출력된 토큰을 HTML [설정] 탭에 입력하세요.\"}");
            return false;
        }
        return true;
    }

    static void health(HttpExchange ex) throws IOException {
        try {
            if (!guard(ex)) return;
            sendJson(ex, 200, "{\"ok\":true,\"name\":\"DevHubProxy\",\"version\":\"" + VERSION + "\",\"java\":\"" + esc(System.getProperty("java.version")) + "\",\"insecureAllowed\":" + allowInsecure + "}");
        } catch (Exception e) { safeError(ex, e); }
    }

    /* ---------- 프록시 ---------- */
    @SuppressWarnings("unchecked")
    static void proxy(HttpExchange ex) throws IOException {
        try {
            if (!guard(ex)) return;
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { sendJson(ex, 405, "{\"error\":\"POST만 지원합니다\"}"); return; }
            byte[] raw = readAll(ex.getRequestBody(), MAX_BODY + 1024 * 1024);
            Map<String, Object> req = (Map<String, Object>) new Json(new String(raw, StandardCharsets.UTF_8)).parse();
            String method = str(req.get("method"), "GET").toUpperCase(Locale.ROOT);
            String url = str(req.get("url"), "");
            if (!url.matches("(?i)^https?://.+")) { sendJson(ex, 400, "{\"error\":\"http:// 또는 https:// URL만 지원합니다\"}"); return; }
            boolean follow = !Boolean.FALSE.equals(req.get("followRedirects"));
            boolean insecure = Boolean.TRUE.equals(req.get("insecure"));
            if (insecure && !allowInsecure) { sendJson(ex, 400, "{\"error\":\"인증서 검증 생략은 프록시를 --allow-insecure 옵션으로 실행해야 사용할 수 있습니다\"}"); return; }
            long timeout = req.get("timeoutMs") instanceof Number ? ((Number) req.get("timeoutMs")).longValue() : 30000;
            byte[] body = req.get("bodyBase64") instanceof String ? Base64.getDecoder().decode((String) req.get("bodyBase64")) : null;

            HttpRequest.Builder rb = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofMillis(Math.max(1000, timeout)));
            if (req.get("headers") instanceof List) {
                for (Object o : (List<Object>) req.get("headers")) {
                    if (!(o instanceof List) || ((List<Object>) o).size() < 2) continue;
                    String k = String.valueOf(((List<Object>) o).get(0)).trim(), v = String.valueOf(((List<Object>) o).get(1));
                    if (k.isEmpty() || k.startsWith(":") || SKIP_REQ.contains(k.toLowerCase(Locale.ROOT))) continue;
                    rb.header(k, v);
                }
            }
            rb.method(method, body == null || body.length == 0 ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(body));

            long t0 = System.nanoTime();
            HttpResponse<InputStream> res = client(insecure, follow, timeout).send(rb.build(), HttpResponse.BodyHandlers.ofInputStream());
            long t1 = System.nanoTime();
            byte[] resBody;
            try (InputStream in = res.body()) { resBody = readAll(in, MAX_BODY); }
            long t2 = System.nanoTime();

            StringBuilder sb = new StringBuilder(resBody.length * 4 / 3 + 512);
            sb.append("{\"status\":").append(res.statusCode())
              .append(",\"statusText\":\"\"")
              .append(",\"httpVersion\":\"").append(res.version() == HttpClient.Version.HTTP_2 ? "HTTP/2" : "HTTP/1.1").append('"')
              .append(",\"finalUrl\":\"").append(esc(res.uri().toString())).append('"')
              .append(",\"redirected\":").append(!res.uri().toString().equals(url))
              .append(",\"ttfbMs\":").append((t1 - t0) / 1_000_000)
              .append(",\"timeMs\":").append((t2 - t0) / 1_000_000)
              .append(",\"headers\":[");
            boolean first = true;
            for (Map.Entry<String, List<String>> e : res.headers().map().entrySet()) {
                if (e.getKey().startsWith(":")) continue;
                for (String v : e.getValue()) {
                    if (!first) sb.append(',');
                    first = false;
                    sb.append("[\"").append(esc(e.getKey())).append("\",\"").append(esc(v)).append("\"]");
                }
            }
            sb.append("],\"bodyBase64\":\"").append(Base64.getEncoder().encodeToString(resBody)).append("\"}");
            sendJson(ex, 200, sb.toString());
            System.out.printf("%s %s → %d (%d ms, %d bytes)%n", method, url, res.statusCode(), (t2 - t0) / 1_000_000, resBody.length);
        } catch (java.net.http.HttpTimeoutException e) {
            sendJson(ex, 504, "{\"error\":\"대상 서버 응답 시간 초과: " + esc(e.getMessage()) + "\"}");
        } catch (java.net.ConnectException e) {
            sendJson(ex, 502, "{\"error\":\"대상 서버에 연결할 수 없습니다 (주소·포트·방화벽 확인): " + esc(String.valueOf(e.getMessage())) + "\"}");
        } catch (javax.net.ssl.SSLException e) {
            sendJson(ex, 502, "{\"error\":\"SSL 오류: " + esc(e.getMessage()) + " — 자체 서명 인증서라면 --allow-insecure로 실행 후 '인증서 검증 안 함'을 켜세요\"}");
        } catch (Exception e) { safeError(ex, e); }
    }

    static HttpClient client(boolean insecure, boolean follow, long timeout) throws Exception {
        String key = insecure + "/" + follow;
        HttpClient c = clients.get(key);
        if (c != null) return c;
        HttpClient.Builder b = HttpClient.newBuilder()
            .version(http2 ? HttpClient.Version.HTTP_2 : HttpClient.Version.HTTP_1_1)
            .followRedirects(follow ? HttpClient.Redirect.NORMAL : HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(10));
        if (insecure) {
            TrustManager[] trustAll = { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] c1, String a) { }
                public void checkServerTrusted(X509Certificate[] c1, String a) { }
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            } };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustAll, new SecureRandom());
            b.sslContext(ctx);
        }
        c = b.build();
        clients.put(key, c);
        return c;
    }

    /* ---------- 유틸 ---------- */
    static byte[] readAll(InputStream in, int max) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[65536];
        int n;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
            if (out.size() > max) throw new IOException("본문이 너무 큽니다 (최대 " + (max / 1024 / 1024) + "MB)");
        }
        return out.toByteArray();
    }

    static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] b = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(status, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
    }

    static void safeError(HttpExchange ex, Exception e) {
        try { sendJson(ex, 500, "{\"error\":\"" + esc(e.getClass().getSimpleName() + ": " + e.getMessage()) + "\"}"); }
        catch (IOException ignore) { ex.close(); }
    }

    static String str(Object o, String def) { return o == null ? def : String.valueOf(o); }

    static String esc(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }

    /** 최소 JSON 파서 (요청 봉투 해석용) */
    static final class Json {
        final String s; int i = 0;
        Json(String s) { this.s = s; }
        Object parse() { Object v = value(); ws(); if (i != s.length()) throw new IllegalArgumentException("JSON 끝에 불필요한 내용"); return v; }
        void ws() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }
        Object value() {
            ws();
            if (i >= s.length()) throw new IllegalArgumentException("JSON이 불완전합니다");
            char c = s.charAt(i);
            if (c == '{') {
                i++; Map<String, Object> m = new LinkedHashMap<>(); ws();
                if (s.charAt(i) == '}') { i++; return m; }
                while (true) {
                    ws(); String k = string(); ws(); expect(':'); m.put(k, value()); ws();
                    if (s.charAt(i) == ',') { i++; continue; }
                    expect('}'); return m;
                }
            }
            if (c == '[') {
                i++; List<Object> l = new ArrayList<>(); ws();
                if (s.charAt(i) == ']') { i++; return l; }
                while (true) {
                    l.add(value()); ws();
                    if (s.charAt(i) == ',') { i++; continue; }
                    expect(']'); return l;
                }
            }
            if (c == '"') return string();
            if (s.startsWith("true", i)) { i += 4; return Boolean.TRUE; }
            if (s.startsWith("false", i)) { i += 5; return Boolean.FALSE; }
            if (s.startsWith("null", i)) { i += 4; return null; }
            int st = i;
            while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
            if (st == i) throw new IllegalArgumentException("JSON 값 오류 (위치 " + i + ")");
            return Double.parseDouble(s.substring(st, i));
        }
        void expect(char c) { if (i >= s.length() || s.charAt(i) != c) throw new IllegalArgumentException("'" + c + "' 필요 (위치 " + i + ")"); i++; }
        String string() {
            expect('"'); StringBuilder sb = new StringBuilder();
            while (true) {
                char c = s.charAt(i++);
                if (c == '"') return sb.toString();
                if (c != '\\') { sb.append(c); continue; }
                char e = s.charAt(i++);
                switch (e) {
                    case 'n': sb.append('\n'); break; case 't': sb.append('\t'); break; case 'r': sb.append('\r'); break;
                    case 'b': sb.append('\b'); break; case 'f': sb.append('\f'); break;
                    case 'u': sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16)); i += 4; break;
                    default: sb.append(e);
                }
            }
        }
    }
}
