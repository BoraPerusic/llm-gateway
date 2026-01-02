# Phase 1: Foundation & Core Proxy Tasks

## Goals
Establish the project, configure Security (OAuth2), Database, and a basic OpenAI-compatible Chat Proxy.

## Tasks

### 1. Project Initialization
- [ ] Initialize Spring Boot Project (Gradle Kts, Kotlin, Java 21)
    - Dependencies: WebMVC, Security (OAuth2 Resource Server), JDBC, Flyway, Postgres Driver, Spring AI, Actuator, Kotlinx Serialization.
- [ ] Configure `application.yml` structures (profiles: dev, test).
- [ ] Configure `docker-compose.yml` for PostgreSQL and optional Mock OAuth2 server (e.g. Keycloak or simple mock).
- [ ] Enable **Virtual Threads** in Spring Boot configuration.

### 2. Database & Data Layer
- [ ] Create Flyway migration V1: `models` table, `users` table (or rely on OAuth2 sub, but local mapping might be needed).
- [ ] Configure Spring Data JDBC repositories.
- [ ] **Documentation**: Explain DataSource config and Flyway bean initialization order.

### 3. Security (OAuth2)
- [ ] Configure `SecurityFilterChain` for OAuth2 Resource Server (JWT).
- [ ] Implement a custom `JwtAuthenticationConverter` if mapping roles is needed.
- [ ] **Test**: Integration test using `spring-security-test` with `jwt()` mock.

### 4. Spring AI & OpenAI Proxy
- [ ] Configure `OpenAiChatClient` (or generic Spring AI client) bean.
- [ ] Create `ChatController` implementing `POST /v1/chat/completions`.
- [ ] Map incoming JSON (OpenAI format) to Spring AI request, and response back to JSON.
- [ ] **Test**: Wiremock test verifying the controller strictly forwards the request body structure.

### 5. Documentation
- [ ] Create `docs/phase1-configuration.md`.
- [ ] Document how `OpenAiChatClient` is auto-configured by Spring AI and how we override `baseUrl`.
- [ ] Document the OAuth2 flow and how to test with a bearer token.

### 6. Verification
- [ ] Run `./gradlew test` (Unit + Integration).
- [ ] Manual: Start app, use `curl --header "Authorization: Bearer ..."` to hit the endpoint.
