# 핏(Fit)한 친구: AI 퍼스널 쇼퍼

20~30대 남녀를 위한 AI 퍼스널 쇼퍼 서비스, '핏(Fit)한 친구'의 백엔드 레포지토리입니다.

## 🌟 프로젝트 목표

'핏(Fit)한 친구'는 바쁜 2030 직장인들의 쇼핑 고민을 덜어주는 대화형 커머스 에이전트입니다.
단순 상품 검색을 넘어, TPO(시간, 장소, 상황)와 개인의 취향을 깊이 이해하고, 친구처럼 친근한 대화를 통해 최적의 상품을 추천하는 것을 목표로 합니다. "이번 주말 소개팅에 입을 옷 추천해줘" 와 같은 자연어 질문에 실패 없는 쇼핑 경험을 제공합니다.

## ✨ 핵심 기능

- **💬 대화형 인터페이스 (Chat UI)**: 사용자와 자연스러운 대화를 통해 쇼핑을 진행합니다.
- **💡 상황 및 의도 파악 (Intent Recognition)**: LLM을 활용하여 사용자의 대화에서 TPO, 스타일, 가격대, 선호 색상 등의 숨은 의도를 파악합니다.
- **🛍️ 상품 검색 및 추천 (RAG)**: Vector DB에 저장된 상품 정보를 의미 기반으로 검색(Semantic Search)하여, 사용자 의도에 가장 적합한 상품을 추천합니다.
- **😎 AI 페르소나**: "센스 있는 패션 잘 아는 친구" 컨셉의 친근하고 공감하는 말투로 사용자와 소통합니다.
- **🌦️ 외부 API 연동**: "이번 주말"과 같은 표현을 이해하고, 날씨 API와 연동하여 기온에 맞는 옷을 추천합니다.
- **🖼️ 이미지 기반 코디 추천**: 사용자가 업로드한 옷(상의, 하의 등) 이미지를 분석하여, 그에 어울리는 다른 상품을 추천합니다.

## 🛠️ 기술 스택

- **Language**: `Java 17`
- **Framework**: `Spring Boot 3.5.8`, `Spring AI 1.1.1`
- **LLM**: `Google Gemini Pro`, `Google Gemini 2.5 Flash`
- **Database**: `PostgreSQL`
- **Vector DB**: `pgvector`
- **Build Tool**: `Gradle`

## ⚙️ 프로젝트 실행 방법

### 1. 사전 준비

- `Java 17` 설치
- `Docker` 및 `Docker Compose` 설치

### 2. 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고, 아래 환경 변수를 설정해주세요.

```bash
# .env

# Google Gemini API Key
GEMINI_API_KEY="YOUR_GEMINI_API_KEY"

# Database credentials
DB_HOST="localhost"
DB_PORT="5432"
DB_USERNAME="your_username"
DB_PASSWORD="your_password"
DB_NAME="ai_personal_shopper"
```

### 3. Docker 실행

프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 PostgreSQL 및 pgvector를 포함한 데이터베이스를 실행합니다.

```bash
docker-compose up -d
```

### 4. 애플리케이션 실행

아래 명령어를 통해 Spring Boot 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```

애플리케이션이 성공적으로 실행되면 `http://localhost:8080` 에서 API를 확인할 수 있습니다.

## 📝 API 문서

애플리케이션 실행 후, 아래 주소에서 Swagger API 문서를 확인할 수 있습니다.

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
