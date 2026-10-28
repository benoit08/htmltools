# Dev Utility Hub

폐쇄망 개발 환경을 위한 **설치 없는 단일 HTML 개발자 도구 모음**입니다.
`dev-utility-hub.html` 파일 하나를 복사해 브라우저로 열면 바로 동작합니다. 외부 CDN, 웹폰트, 원격 요청이 없고, 입력한 데이터는 브라우저 안에서만 처리됩니다.

- 실행: `dev-utility-hub.html`을 Chrome / Edge / Whale(최근 버전)로 열기 (Internet Explorer는 지원하지 않음)
- 단축키: <kbd>Ctrl</kbd>+<kbd>K</kbd> 기능 검색 · <kbd>Ctrl</kbd>+<kbd>Enter</kbd> 현재 도구 실행
- 파일 열기: 각 입력 패널에 파일을 끌어다 놓으면 열립니다 (UTF-8 / EUC-KR(CP949) 자동 감지)

## 도구 목록 (27종)

| 분류 | 도구 | 주요 기능 |
| --- | --- | --- |
| JSON | JSON 포매터 | 정렬 · 압축 · 검증(줄/열 위치와 한국어 원인) · 트리 뷰(경로 복사, 검색) · 이중/삼중 이스케이프 자동 해제 · 중첩 JSON 펼치기 · 관대한 파싱으로 복구 · **2^53을 넘는 Long ID와 키 순서 보존** |
| | JSON 이스케이프 | 문자열 ↔ JSON 문자열 리터럴 · 1단계 / 완전 해제 |
| | JSON 비교 | 키 순서·공백 무시 구조 비교 · 배열 매칭 키(id) · 무시할 키(timestamp 등) · 정규화 텍스트 비교 |
| | JSONPath 검색 | `$..id`, `[?(@.age > 20 && @.active)]`, 정규식 `=~`, `.length` |
| | 컬렉션 도구 | 배열 필드 추출 · 정렬 · 중복 제거 · 그룹화 · 개수 · 필터 · 통계 · 평탄화 |
| HTTP · API | HTTP API 테스터 | GET/POST/PUT/PATCH/DELETE · Params/Headers/Body(JSON, form, multipart, 파일)/Auth · 환경변수 `{{baseUrl}}` · cURL 가져오기(bash/cmd) · 코드 생성(cURL, fetch, Java HttpClient, RestTemplate) · 히스토리 · 저장된 요청 · 응답 비교 · EUC-KR 응답 디코딩 · 로컬 프록시 모드 |
| | HTTP 헤더 · 쿠키 분석 | 헤더 설명 · 보안 헤더 점검(HSTS, CSP, X-Frame-Options, CORS) · Set-Cookie 속성/만료 · Basic 인증 해독 |
| | OpenAPI 뷰어 | Swagger 2 / OpenAPI 3 (JSON·YAML) · 파라미터 · 예제 본문 생성 · HTTP 테스터로 보내기 |
| 인코딩 · 보안 | Base64 | UTF-8 / EUC-KR · URL-safe · 파일 ↔ Base64 · 이미지 미리보기 |
| | URL 인코딩 · 쿼리 편집 | encodeURIComponent / encodeURI / Java URLEncoder · **EUC-KR 퍼센트 인코딩** · URL 분석 · 쿼리 파라미터 표 편집 |
| | JWT 디코더 | Header/Payload 해독 · 만료 확인 · HS/RS/ES/PS 서명 검증 · HS 토큰 생성 |
| | 유니코드 · 문자셋 | `\uXXXX` ↔ 한글 · HTML 엔티티 · UTF-8/EUC-KR 바이트 · **깨진 한글 복구** · 문자별 코드 표 · 바이트 수 |
| | 해시 · HMAC · 체크섬 | MD5 · SHA-1 · SHA-256/384/512 · HMAC · CRC32 · 파일 체크섬 비교 (WebCrypto 없이도 동작) |
| | UUID · 랜덤 문자열 | UUID v4/v7 대량 생성 · UUID 분석 · 안전한 랜덤 문자열 |
| 포매터 | XML 포매터 | 정렬 · 압축 · 검증 · XPath · XML ↔ JSON (MyBatis 매퍼, SOAP) |
| | HTML 포매터 · 엔티티 | 정렬 · 압축 · 엔티티 인코딩/디코딩 · 태그 제거 |
| | SQL 포매터 | SELECT/JOIN/WHERE/서브쿼리/CASE 정렬 · 키워드 대소문자 · 쉼표 앞 배치 · 압축 · `#{}` `${}` `?` 지원 |
| 텍스트 | 텍스트 · 파일 비교 | 줄 단위 + 문자 단위 강조 · 나란히/통합 보기 · 공백/대소문자 무시 · 동일 구간 접기 |
| | 정규식 테스터 | 실시간 매칭 · 그룹/이름 그룹 · 치환 · Java 문자열 변환 · 2초 시간 제한(무한 백트래킹 방지) |
| | 문자열 도구 | 공백·중복·정렬 · camelCase ↔ SNAKE_CASE · SQL IN 절 · 추출(이메일, IP 등) · UTF-8/EUC-KR 바이트 수 |
| | CSV · JSON · 표 변환 | CSV/TSV(엑셀 붙여넣기) ↔ JSON · Markdown 표 · SQL INSERT · Excel용 BOM 저장 |
| Java · 서버 | SQL 바인드 치환 | MyBatis `Preparing/Parameters`, Hibernate `binding parameter` 로그 → 값이 들어간 디버깅용 SQL |
| | 스택 트레이스 분석 | `Caused by` 체인 · 근본 원인 · 앱 코드 프레임 강조 · 프레임워크 프레임 접기 · ORA 오류 등 원인 힌트 |
| | Java 로그 분석기 | Logback/Log4j · JEUS · Tomcat 로그 · 레벨별 필터 · 스택 묶기 · 동일 예외 집계 · 시간/텍스트 필터 |
| | Properties · YAML · JSON | application.properties ↔ application.yml ↔ JSON · native2ascii · `---` 프로파일 문서 |
| 시간 · 스케줄 | 날짜 · 타임스탬프 변환 | Unix 초/밀리초 ↔ ISO 8601 · KST/UTC · yyyyMMddHHmmss · Java 패턴 포맷 · 날짜 계산 |
| | Cron 표현식 | Linux · Spring `@Scheduled` · Quartz · 다음 실행 시각 · 한국어 설명 |

### 도구 간 연계

결과 패널의 **보내기 ▾** 버튼으로 다른 도구에 바로 넘길 수 있습니다. 예를 들어

- HTTP 테스터 응답 → **JSON 포매터로** → 트리에서 값 경로 복사
- HTTP 테스터 **이전 응답과 비교** → JSON 비교 (직전 응답 vs 현재 응답)
- 로그 분석기의 예외 → **스택 분석기로** → 근본 원인 확인
- OpenAPI 뷰어 → **HTTP 테스터로** (메서드, URL, 예제 본문 자동 채움)

## 저장 데이터와 보안

- 입력 내용은 새로고침해도 복원되도록 이 PC 브라우저의 localStorage에만 저장됩니다. **설정(⚙)**에서 끄거나 지울 수 있습니다.
- **JWT 토큰, HTTP 인증 값, HTTP 응답 본문은 저장하지 않습니다.** Authorization·Cookie·토큰류 헤더도 히스토리에 남기지 않습니다(값이 `Bearer {{token}}`처럼 환경변수 참조뿐이면 유지).
- 토큰은 HTTP 테스터의 **환경변수**(`{{token}}`)로 관리하는 것을 권장합니다.
- 설정 · 환경변수 · 저장된 요청은 설정 화면에서 JSON 파일로 내보내고 다른 PC에서 가져올 수 있습니다.

## HTTP 테스터와 CORS

HTML을 로컬 파일(`file://`)로 열면 브라우저는 `Origin: null`로 요청합니다. 서버가 CORS를 허용하지 않으면 응답을 읽을 수 없고, `Cookie`·`Host` 같은 헤더는 브라우저가 무시합니다. 방법은 두 가지입니다.

1. **개발 서버에서 CORS 허용** (Spring 예)
   ```java
   registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowedHeaders("*");
   ```
2. **로컬 프록시 사용** — `proxy/DevHubProxy.java` (JDK 11 이상, 외부 라이브러리 없음)
   ```bash
   java DevHubProxy.java                    # 컴파일 없이 실행, 127.0.0.1:8099
   java DevHubProxy.java --token=mytoken    # 토큰 고정
   java DevHubProxy.java --allow-insecure   # 자체 서명 인증서 허용 (개발용)
   ```
   실행하면 콘솔에 토큰이 출력됩니다. HTML → HTTP API 테스터 → **설정** 탭에서 **로컬 프록시 경유**를 선택하고 주소와 토큰을 입력하세요. CORS·금지 헤더 제한이 없어지고 `Set-Cookie`를 포함한 모든 응답 헤더를 볼 수 있습니다.
   - 기본적으로 `127.0.0.1`에만 바인딩하고, `file://`·localhost 페이지에서 온 요청과 올바른 토큰만 허용합니다. 운영 서버에서 실행하지 마세요.
   - 콘솔 한글이 깨지면 `chcp 65001` 후 실행하거나 `java -Dstdout.encoding=UTF-8 DevHubProxy.java`로 실행하세요.

## 단일 HTML로 할 수 없는 것

| 기능 | 이유 |
| --- | --- |
| DB 직접 접속 | 브라우저는 JDBC/TCP 소켓을 열 수 없음 — 별도 서버 필요 |
| Ping / TCP 포트 확인 | OS 네트워크 기능 필요 |
| 임의 파일 시스템 접근 | 사용자가 선택하거나 끌어다 놓은 파일만 읽을 수 있음 |
| CORS 미허용 서버 직접 호출 | 브라우저 정책 — 위의 로컬 프록시 사용 |

## 웹에 올려서 쓰기 (선택)

폐쇄망 밖에서도 쓰고 싶다면 이 저장소의 GitHub Pages를 켜면 됩니다 (Settings → Pages → `main` 브랜치 / root).
`https://benoit08.github.io/htmltools/dev-utility-hub.html` 형태로 열 수 있습니다. 무료 플랜에서는 저장소가 공개(public)여야 합니다. HTTPS 페이지에서는 `http://` 내부 주소로 직접 요청할 수 없으므로(혼합 콘텐츠 차단) API 테스트는 로컬 파일로 여는 것을 권장합니다.

## 구성

```
dev-utility-hub.html     단일 실행 파일 (HTML + CSS + JavaScript, 약 430KB)
proxy/DevHubProxy.java   선택: HTTP 테스터용 로컬 프록시 (JDK 11+)
```

`dev-utility-hub.html`이 유일한 소스입니다. 기능 추가는 파일 안의 `defineTool({...})` 블록을 참고하세요. 각 도구는 `id`, `group`, `name`, `init(root)`를 가지며, `inputs`를 정의하면 다른 도구의 **보내기** 메뉴에 나타납니다.
