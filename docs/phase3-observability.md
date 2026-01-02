# Phase 3: Observability Configuration

## New Features

### 1. Prompt Logging
*   **Database**: `prompt_logs` table (V2 migration).
*   **Search**: Uses Postgres `TSVECTOR` for Full Text Search on prompts and responses.
*   **Async Recording**: Logs are saved asynchronously via `ObservabilityService` to prevent latency impact.

### 2. Metrics
*   **Micrometer**: Standard Spring Boot Actuator integration.
*   **Custom Metrics**:
    *   `llm.requests.count`: Counter (tags: model, provider, status).
    *   `llm.requests.latency`: Timer (duration).
    *   `llm.tokens.prompt` / `completion`: Summary (token usage).
*   **Prometheus**: Exposed at `/actuator/prometheus`.

### 3. Dashboard
*   **Frontend**: New "Dashboard" tab in the Vue.js app.
*   **API**: `/api/v1/observability/logs` supports `?query=...` for FTS.

## How to Verify
1.  **Generate Traffic**: Send a few chat requests.
2.  **Check Logs**: Go to Dashboard tab, click Search. You should see your requests.
3.  **Search**: Try searching for a word in your prompt.
4.  **Metrics**: `curl http://localhost:8080/actuator/prometheus | grep llm_requests`
