# Phase 4: Async Jobs Tasks

## Goals
Implement internal Async Job Manager using Spring Integration and NATS.

## Tasks

### 1. Job Management (Internal)
- [ ] Create Flyway migration: `jobs` table (id, status, result, created_at).
- [ ] Create `JobService`:
    - `submit(Request)` -> returns `jobId`.
    - `poll(jobId)` -> returns Status/Result.
- [ ] Implement `AsyncWorker`:
    - Uses Kotlin Coroutines or Spring `@Async`.
    - Pickups job -> Calls LLM -> Updates DB.

### 2. NATS Integration
- [ ] Add `spring-integration-nats` (or NATS spring boot starter) dependency.
- [ ] Configure NATS Connection Bean.
- [ ] Implement Publisher service: Publish event on Job Completion.
- [ ] **Test**: Integration test with embedded NATS or Testcontainers NATS.

### 3. Webhooks
- [ ] Add `webhook_url` field to Request/Job.
- [ ] Implement Webhook Dispatcher (uses `WebClient` or `RestClient`).

### 4. Documentation
- [ ] Create `docs/phase4-async.md`.
- [ ] Detail the Spring Integration Channel Adapters for NATS.
- [ ] Explain the Async execution thread pool configuration (Virtual Threads vs Fixed).

### 5. Verification
- [ ] E2E Async Test: Submit Job -> Loop Poll until Done -> Verify Webhook received.
