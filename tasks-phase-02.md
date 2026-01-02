# Phase 2: Dynamic Routing & Rules Tasks

## Goals
Enable registering multiple models and selecting them via HOCON rules.

## Tasks

### 1. Model Registry
- [ ] Implement CRUD Service for `Model` entity.
- [ ] Update `ChatController` to lookup dynamic models instead of hardcoded default.
- [ ] **Test**: Unit test Service with mocked Repository.

### 2. HOCON Rules Engine
- [ ] Add `typesafe-config` dependency.
- [ ] Design Rule Schema (Match tags, input criteria).
- [ ] Implement `RuleEngine` service: `(RequestMetadata, List<Model>) -> Model`.
- [ ] **Test**: Unit tests with various sample HOCON files.

### 3. Admin Frontend (Vue.js Monolith)
- [ ] Create `src/main/resources/static` structure (or `frontend` module built into static).
- [ ] Initialize Vue 3 + Vite.
- [ ] Create "Models" view (List, Add, Edit).
- [ ] Connect Frontend to Backend REST API.

### 4. Documentation
- [ ] Update `docs/phase2-rules.md`.
- [ ] Explain HOCON bean loading and how the Rule Engine is injected.
- [ ] Document Vue.js build integration in Gradle (frontend-gradle-plugin?).

### 5. Verification
- [ ] Integration Test: Register two models, add a rule "If tag=gpt4 use Model A", send request, verify Model A was called via Wiremock.
