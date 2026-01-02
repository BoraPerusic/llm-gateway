# Phase 3: Observability Tasks

## Goals
Implement comprehensive logging (Postgres/FTS) and Metrics.

## Tasks

### 1. Prompt Logging
- [ ] Define `PromptLogEntry` data class.
- [ ] Create `PromptLogRepository` interface.
- [ ] Implement `PostgresPromptLogRepository` using `JdbcTemplate` and Postgres `TSVECTOR`.
- [ ] Implement `LoggingAspect` or Filter to intercept Request/Response and save to DB.
    - *Note*: Ensure this doesn't block the response (use `@Async` or coroutines for the save op).

### 2. Metrics (Micrometer)
- [ ] Inject `MeterRegistry`.
- [ ] Add Counters: `llm.requests.count` (tags: model, status).
- [ ] Add Timers: `llm.requests.latency`.
- [ ] Expose Actuator Prometheus endpoint.

### 3. Dashboard API
- [ ] Create REST endpoint to query Logs (with full text search).
- [ ] Create REST endpoint to retrieve aggregated Metrics (if more complex than Prometheus export).

### 4. Frontend Dashboard
- [ ] Add "Dashboard" view to Vue App.
- [ ] Visualize Requests per Minute / Latency charts (using Chart.js or similar).
- [ ] Create "Trace Explorer" to view Logged Prompts.

### 5. Documentation
- [ ] Create `docs/phase3-observability.md`.
- [ ] Explain how AspectJ/Filters utilize the `PromptLogRepository` bean.
- [ ] Document the Prometheus scraping path.

### 6. Verification
- [ ] Load Test: Jmeter/K6 script to send 100 requests. Verify DB Log count matches and Metrics increment.
