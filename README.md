# Dev Utility Hub

폐쇄망 개발 환경을 위한 **설치 없는 단일 HTML 개발자 도구 모음**입니다.
`dev-utility-hub.html` 파일 하나를 복사해 브라우저로 열면 바로 동작합니다. 외부 CDN, 웹폰트, 원격 요청이 없고, 입력한 데이터는 브라우저 안에서만 처리됩니다.

- 실행: `dev-utility-hub.html`을 Chrome / Edge / Whale(최근 버전)로 열기 (Internet Explorer는 지원하지 않음)
- 단축키: <kbd>Ctrl</kbd>+<kbd>K</kbd> 기능 검색 · <kbd>Ctrl</kbd>+<kbd>Enter</kbd> 현재 도구 실행 · 아무 데서나 <kbd>Ctrl</kbd>+<kbd>V</kbd> → Smart Paste
- 파일 열기: 입력 패널이나 화면 어디에든 파일을 끌어다 놓으면 알맞은 도구가 열립니다 (UTF-8 / EUC-KR(CP949) 자동 감지)

## 공통 기능

| 기능 | 설명 |
| --- | --- |
| **Smart Paste** | 무엇이든 붙여넣거나 파일을 놓으면 형식을 감지해 도구와 변환을 추천합니다 (이스케이프된 JSON, JWT, cURL, 스택 트레이스, 스레드 덤프, 인증서, HAR, CORS 오류 메시지, 16진수, 색상 등) |
| **레시피 파이프라인** | Base64 디코드 → gzip 해제 → JSON 정렬처럼 변환을 이어 붙여 한 번에 실행하고 레시피로 저장 |
| **여러 탭** | 같은 도구를 여러 개 열어 비교 (주소 `#/도구/번호`로 바로 열기) |
| **입력 기록 · 스냅샷** | 입력 패널의 **기록 ▾**에서 이전 입력으로 되돌리기 |
| **보내기 ▾** | 결과를 다른 도구로 바로 넘기기 (HTTP 응답 → JSON 포매터, 로그 예외 → 스택 분석 등) |
| **대용량 모드** | 큰 JSON은 Web Worker로 처리, 큰 로그·GC 로그·Hex 파일은 나눠 읽기 (수십~수백 MB, Hex 뷰어는 GB 단위) |
| **디자인** | 사이드바의 **◐ 테마**로 라이트/다크, **▦ 디자인**으로 스킨 선택. 기본 스킨 '미드나잇 오로라'는 기능 분류마다 사무실 프로그램 색(데이터 Excel 초록, HTTP Outlook 파랑, 텍스트 Word 파랑 …)을 씁니다 |

## 도구 목록 (80종)

| 분류 | 도구 |
| --- | --- |
| 자동화 | 레시피 파이프라인 |
| JSON | JSON 포매터(2^53 넘는 Long ID·키 순서 보존, 한국어 오류 위치, 관대한 파싱 복구) · JSON 이스케이프 · JSON 비교 · JSONPath 검색 · 컬렉션 도구 · JSON Schema 검증 · JSON Schema 생성 · JSON → Java DTO(class/Lombok/record, TS, Kotlin) · JSON Flatten · JSON Patch(RFC 6902/7396) · NDJSON · JSON Lines |
| 데이터 · 문서 | CSV · JSON · 표 변환 · JSON Mock 생성 · 데이터 표 뷰어(10만 행 정렬·필터) · 테스트 데이터 빌더 · Markdown 미리보기 |
| HTTP · API | HTTP API 테스터(환경변수, cURL 가져오기, 응답 검증 Tests, 값 추출 체이닝, Mock 모드, 코드 생성 11종, 로컬 프록시) · HTTP 헤더 · 쿠키 · 캐시 분석 · OpenAPI 뷰어 · 컬렉션 러너 · HAR 분석기 · WebSocket · SSE 테스터 · CORS 진단 |
| 인코딩 · 보안 | Base64 · URL 인코딩 · 쿼리 편집 · JWT 디코더 · 유니코드 · 문자셋(깨진 한글 복구) · 해시 · HMAC · 체크섬 · UUID · 랜덤 문자열 · 바이트 길이 계산 · 인증서 뷰어(PEM/DER/P7B/CSR/JKS) · OAuth PKCE 생성기 · 민감정보 마스킹 |
| 파일 · 바이너리 | Hex 뷰어 · 파일 형식 판별 · 파일 인코딩 검사 · 변환 · 파일 비교 · 체크섬 검증 · 이미지 메타데이터(EXIF·GPS 보기/제거) · GZIP · 압축 도구 |
| 포매터 | XML 포매터(XPath, XML ↔ JSON) · HTML 포매터 · 엔티티 · SQL 포매터 |
| 텍스트 | 텍스트 · 파일 비교 · 정규식 테스터 · 문자열 도구 · 줄바꿈 · 공백 정리(CRLF 셸 스크립트 감지) · 유니코드 정규화 · 숨은 문자 |
| Java · 서버 | SQL 바인드 치환(MyBatis/Hibernate 로그) · 스택 트레이스 분석 · Java 로그 분석기(Logback/Log4j/JEUS/Tomcat, 큰 파일 스트리밍) · Properties · YAML · JSON · JDBC URL 빌더 · 파서(11종 DB) · JNDI 설정 점검 · 스레드 덤프 분석 · GC 로그 분석 · 실행 계획 분석(Oracle/Tibero/PostgreSQL/MySQL) · 플레이스홀더 해석 · WAR · JAR 탐색기(중복·충돌 라이브러리) · 클래스 버전 확인 · web.xml 분석 |
| 명령어 · 리눅스 | 명령어 사전(한글 작업 검색: 동의어 · 초성 · 오타, 리눅스 417개 + 서버 280개 예제, 위험 명령 · 헷갈리는 짝 · Windows 대응, 즐겨찾기) · 명령어 설명기(파이프 · 리다이렉트 · 묶음 옵션 · 위험 표시, 프로그램 148개 옵션 사전) · 옵션 비교기 · 명령어 북마크({{변수}} 채워 복사) · 셸 따옴표 도우미(bash · ssh · sudo · find -exec · curl JSON · crontab · cmd · PowerShell) · Linux ↔ Windows 명령 변환 · 명령어 조립기(find · grep · tar · rsync · chmod · curl · java -jar 등 12종) · Vim 연습 |
| 운영 · 배포 | 로그 범위 추출(grep -A/-B/-C와 같은 결과, 스택 끝까지, 시간 범위, .gz) · 압축 경로 시뮬레이터(tar · zip 실제 파일 읽기, 최상위 폴더 중복 · zip slip 경고, -C · --strip-components · unzip -d 풀기 전후 트리) · 복사 · 이동 시뮬레이터(cp · mv · rsync 끝 슬래시 · 숨김 파일 · --delete 전후 트리) · 경로 사전(146개, JEUS 9.1 · Tomcat · WebtoB · Spring Boot 등, 변수 치환 · 경로 해석기) · 배포 체크리스트(템플릿 7종) · 장애 · 오류 사전(시나리오 86개, 로그 붙여넣기 자동 매칭) |
| 시간 · 스케줄 | 날짜 · 타임스탬프 변환 · Cron 표현식(Linux/Spring/Quartz) |
| 기타 유틸 | 진법 변환 · 비트 · IP · CIDR 계산기(IPv4/IPv6) · 색상 변환 · 대비 검사 |

### 명령어 · 경로 정보의 근거 표시

명령어 사전 · 경로 사전 · 장애 사전의 모든 항목에 근거를 표시합니다.

| 표시 | 뜻 |
| --- | --- |
| **실행 확인** | 검증 환경(Ubuntu 24.04 · GNU coreutils 9.4 · grep 3.11 · tar 1.35 · rsync 3.2.7 · OpenJDK 21 · Maven 3.9 · Gradle 8.14 · git 2.43 · svn 1.14)에서 실제로 실행해 확인 |
| **매뉴얼 확인** | man 페이지 · `--help` · 배포본 내용으로 확인 (재부팅 · 포맷 · systemd처럼 실행하지 않은 명령) |
| **문서 기준** | 제품 공식 문서 기준 (JEUS · WebtoB · Tibero · Oracle · Windows) — 현장 버전에 따라 다를 수 있으니 확인 필요 |

- 예시 출력은 항상 **예시**로 표시합니다. 실제 결과는 사용자가 넣은 로그 · 압축 파일 · 폴더 구조를 분석하는 도구(로그 범위 추출, 압축 경로 시뮬레이터, 복사 · 이동 시뮬레이터)에서만 보여 줍니다.
- 시뮬레이터 결과는 실제 GNU grep · tar · unzip · cp · mv · rsync 실행 결과와 무작위 시나리오 수천 건을 대조해 맞췄습니다. RHEL/CentOS 7 · 8의 구버전 도구와 다른 점은 화면에 따로 안내합니다.

## 저장 데이터와 보안

- 입력 내용은 새로고침해도 복원되도록 이 PC 브라우저의 localStorage에만 저장됩니다. **설정(⚙)**에서 끄거나 지울 수 있습니다.
- **JWT 토큰, HTTP 인증 값, HTTP 응답 본문, 인증서·키스토어 비밀번호, JDBC 비밀번호, 민감정보 마스킹 원본, PKCE verifier는 저장하지 않습니다.** Authorization·Cookie·토큰류 헤더도 히스토리에 남기지 않습니다(값이 `Bearer {{token}}`처럼 환경변수 참조뿐이면 유지).
- HTTP 테스터에서 추출한 값 중 token처럼 민감해 보이는 이름은 기본으로 메모리에만 보관합니다.
- 명령어 북마크 · 배포 체크리스트 · 경로 사전 변수는 저장되지만, 비밀번호 · 토큰처럼 보이는 값은 저장 전에 경고하고 비밀 변수는 저장하지 않습니다.
- 설정 · 환경변수 · 저장된 요청은 설정 화면에서 JSON 파일로 내보내고 다른 PC에서 가져올 수 있습니다.

## HTTP 테스터와 CORS

HTML을 로컬 파일(`file://`)로 열면 브라우저는 `Origin: null`로 요청합니다. 서버가 CORS를 허용하지 않으면 응답을 읽을 수 없고, `Cookie`·`Host` 같은 헤더는 브라우저가 무시합니다. 원인은 **CORS 진단** 도구로 확인할 수 있고, 방법은 두 가지입니다.

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
   - WebSocket · SSE 테스터는 프록시를 거치지 않고 직접 연결합니다.

## 단일 HTML로 할 수 없는 것

| 기능 | 이유 |
| --- | --- |
| DB 직접 접속 | 브라우저는 JDBC/TCP 소켓을 열 수 없음 — 별도 서버 필요 |
| Ping / TCP 포트 확인 | OS 네트워크 기능 필요 |
| 운영 서버 SSL 인증서 직접 가져오기 | 브라우저가 인증서 체인을 노출하지 않음 — `openssl s_client` 결과나 인증서 파일을 붙여넣어 분석 |
| Mock 서버 | 페이지는 포트를 열 수 없음 — HTTP 테스터의 Mock 모드는 브라우저 안에서만 응답을 흉내냄 |
| 임의 파일 시스템 접근 | 사용자가 선택하거나 끌어다 놓은 파일만 읽을 수 있음 |
| CORS 미허용 서버 직접 호출 | 브라우저 정책 — 위의 로컬 프록시 사용 |

## 웹에 올려서 쓰기 (선택)

폐쇄망 밖에서도 쓰고 싶다면 이 저장소의 GitHub Pages를 켜면 됩니다 (Settings → Pages → `main` 브랜치 / root).
`https://benoit08.github.io/htmltools/dev-utility-hub.html` 형태로 열 수 있습니다. 무료 플랜에서는 저장소가 공개(public)여야 합니다. HTTPS 페이지에서는 `http://` 내부 주소로 직접 요청할 수 없으므로(혼합 콘텐츠 차단) API 테스트는 로컬 파일로 여는 것을 권장합니다.

## 구성

```
dev-utility-hub.html     단일 실행 파일 (HTML + CSS + JavaScript, 약 2.4MB)
proxy/DevHubProxy.java   선택: HTTP 테스터용 로컬 프록시 (JDK 11+)
```

`dev-utility-hub.html`이 유일한 실행 파일입니다. 각 도구는 `defineTool({ id, group, name, init(root) })` 블록으로 정의되며, `inputs`를 정의하면 다른 도구의 **보내기** 메뉴에, `defineDetector`로 Smart Paste에, `defineOp`로 레시피 파이프라인에 나타납니다. 명령어 · 경로 · 장애 데이터는 `defineCmds` · `definePaths` · `defineErrors` · `defineOpts`로 등록합니다.
