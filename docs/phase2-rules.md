# Phase 2: Dynamic Routing & Rules Configuration

## New Features

### 1. Model Registry
*   **Database**: `models` table now supports `provider` and `config` JSONB columns.
*   **API**: `POST /api/v1/models` to register new models.
*   **Frontend**: A Vue.js admin page is available at the root URL (served from static resources) to manage these models.

### 2. Rule Engine (HOCON)
*   Routes requests based on `model` name alias or Tags.
*   **Configuration**: `src/main/resources/rules.conf`.
*   **Logic**:
    1.  **Exact Name Match**: Checks if `request.model` matches a registered Model Name.
    2.  **Tag Match**: (Future) Filter if tags are provided.
    3.  **HOCON Aliases**: Checks `rules.aliases` to map generic names (e.g. "gpt-4") to specific registered model names (e.g. "openai-gpt-4").

### 3. Frontend Build
*   The Vue.js project is at `src/main/frontend`.
*   **Modifications**:
    *   Gradle plugin `com.github.node-gradle.node` is added.
    *   `npmBuild` task runs `npm run build`.
    *   `processResources` depends on copying `dist/` to `src/main/resources/static`.
*   **Consequence**: Running `./gradlew bootRun` will automatically build the latest frontend code.

## How to Verify
1.  **Start App**: `./gradlew bootRun`
2.  **Open Browser**: `http://localhost:8080/` (Should see Model Registry).
3.  **API Routing**:
    *   Register a model named `openai-gpt-4`.
    *   Send Chat Request with `"model": "gpt-4"`.
    *   Server should log: `Selected Model: openai-gpt-4`.
