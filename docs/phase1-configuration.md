# Phase 1: Foundation & Core Proxy Configuration

## Components Initialized

### 1. Spring Boot & Kotlin
*   Version: Spring Boot 3.2.5, Kotlin 1.9.23.
*   **Virtual Threads**: Enabled in `application.yml` (`spring.threads.virtual.enabled: true`).

### 2. Database
*   **PostgreSQL**: Configured via `docker-compose.yml`.
*   **Flyway**: Manages schema migrations.
    *   `V1__initial_schema.sql`: Creates `models` and `users` tables.
*   **Spring Data JDBC**: Used for lightweight persistence (No Hibernate).
    *   `ModelRepository`: CrudRepository for `Model` entity.

### 3. Security (OAuth2)
*   Configured in `SecurityConfig.kt`.
*   **Mode**: OAuth2 Resource Server (JWT).
*   **Endpoints**:
    *   `/actuator/health` -> Public.
    *   All other `/api/**` -> Authenticated (Requires valid Bearer Token).
*   **Configuration**:
    *   Set `spring.security.oauth2.resourceserver.jwt.issuer-uri` and `jwk-set-uri` in `application.yml` or env vars to point to your provider (Auth0/Keycloak).

### 4. AI Proxy
*   **Spring AI**: Uses `spring-ai-openai-spring-boot-starter`.
*   **ChatController**: `POST /api/v1/chat/completions`
    *   Accepts JSON: `{ "messages": [ {"role": "user", "content": "..."} ] }`
    *   Forwards to `OpenAiChatClient`.
    *   Returns content string.
*   **Configuration**:
    *   `spring.ai.openai.api-key`: Must be set (defaults to placeholder).

## How to Run

1.  **Start Database**:
    ```bash
    docker-compose up -d
    ```

2.  **Run Application**:
    ```bash
    ./gradlew bootRun
    ```

## Verification

### Manual Test (curl)
```bash
curl -X POST http://localhost:8080/api/v1/chat/completions \
  -H "Authorization: Bearer <VALID_JWT>" \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [{"role": "user", "content": "Hello!"}]
  }'
```

### Automated Tests
Run generic tests:
```bash
./gradlew test
```
