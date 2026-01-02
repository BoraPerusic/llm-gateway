# Phase 5: Interfaces & Polish Tasks

## Goals
Add gRPC support and finalize the application.

## Tasks

### 1. gRPC Support
- [ ] Add `grpc-spring-boot-starter`.
- [ ] Define `.proto` files for Chat Service.
- [ ] Generate Java/Kotlin stubs (Gradle Protobuf plugin).
- [ ] Implement `GrpcService` logic (delegating to core `JobService` or `ChatService`).

### 2. Alternative Storage (MS SQL)
- [ ] Implement `MssqlPromptLogRepository` (conditional bean based on config).
- [ ] **Documentation**: Explain `@ConditionalOnProperty` usage for swapping storage backends.

### 3. Final Polish
- [ ] Review all KDocs.
- [ ] Ensure all "TODOs" are resolved.
- [ ] Optimize Dockerfile (Multi-stage build).

### 4. Documentation
- [ ] Finalize `README.md`.
- [ ] Compile all Phase docs into a `ARCHITECTURE_HANDBOOK.md`.

### 5. Verification
- [ ] gRPC Client Test (using `grpcurl` or custom client).
