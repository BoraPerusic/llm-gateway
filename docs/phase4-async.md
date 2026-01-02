# Phase 4: Async Jobs Configuration

## New Features

### 1. Job Management API
Allows long-running operations.
*   **Submit**: `POST /api/v1/async/chat/completions` (Same body as sync chat).
    *   Returns: `{ "jobId": "..." }`
*   **Poll**: `GET /api/v1/async/chat/jobs/{id}`
    *   Returns: `{ "id": "...", "status": "QUEUED|PROCESSING|COMPLETED|ERROR", "result": "..." }`

### 2. Implementation
*   **Database**: `jobs` table (V3 migration).
*   **Worker**: Uses Spring `@Async` to process requests in a background thread (Virtual Thread if enabled).
*   **Logic**: Reuses `ChatController` logic but wraps it in exception handling and state updates.

### 3. Events (NATS)
*   Topic: `jobs.completed`
*   Payload: `{"jobId": "...", "status": "COMPLETED"}`
*   **Integration**: Uses `spring-integration-nats`.
*   **Configuration**: Defaults to `nats://localhost:4222`. Override via Spring Boot properties if needed.

## How to Verify
1.  **Submit Job**:
    ```bash
    curl -X POST http://localhost:8080/api/v1/async/chat/completions -d '...'
    # Response: {"jobId": "123-abc"}
    ```
2.  **Poll Status**:
    ```bash
    curl http://localhost:8080/api/v1/async/chat/jobs/123-abc
    # Response: {"status": "COMPLETED", "result": "..."}
    ```
3.  **Check NATS**:
    *   Use `nats sub jobs.completed` CLI to see the event.
