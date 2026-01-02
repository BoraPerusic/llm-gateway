# LLM Gateway requirements

This file contains the requirements for the LLM Gateway project. 

## Goal

The goal of this project is to create a gateway for LLMs that can be used to access LLMs from any application, mainly agents.
The LLM Gateway should abstract the implementation details of the LLMs and provide a simple interface for the applications (agents) to use. It will also enable the central management of the models to be used by the agents, central seamless swtiching between cloud and on-prem models, and easy model updates.

## Functionality

The LLM Gateway should provide the following features inside its APIs:
- model registry: ability to register, update, delete, and list models (mainly LLMs, but also embeddings, vision etc)
  - cloud models (mainly OpenAI API based ones, HuggingFace etc)
  - on-prem models (models running locally in a docker container, or served in a local network)
- model tagging: the registered models will be tagged with metadata to enable easy filtering and selection
- model requests: the agents will request the models to talk to, either by model name or by tags. The gateway will pick up a model that "best" corresponds to the request according to the internal rules
- internal rules: the gateway will have internal rules to determine which model to use for a given request. These rules will be based on the model tags and the request metadata (e.g. model type, model size, model performance, model availability, model cost, etc)
- metrics collection: the gateway will collect metrics about the models and the requests to enable easy monitoring and analysis, mainly: usage (# of requests), tokens used (and derived costs), latency (performance) etc. These metrics will be available via an API and also will be visualized in a dashboard, and will be available for the rule engine to use for decision making (i.e. ability to throttle requests or switch models based on the metrics)
- extensive observability: the gateway will provide extensive observability features to enable easy monitoring and analysis, mainly logging and tracing. The logs and traces will enable easy "replay" of the requests (running the same requests through different models)and also will enable easy debugging and analysis of the requests.
- authentication and authorization: the clients will authenticate to the gateway using a token, and the gateway will validate the token to ensure the client is authorized to use the gateway, and will use RBAC to check which models the client is authorized to use.
The gateway should allow both synchronous and asynchronous requests. For asynchronous requests, the gateway will allow:
  - polling, by returning a job ID, and the client will use this ID to check the status of the job and retrieve the result.   
  - webhooks, by providing a callback URL, and the gateway will call this URL when the job is completed.   
  - pub / sub mechanism using NATS JetStream, by providing a subject and a trace id, and the gateway will publish the result to this subject when the job is completed.
For the APIs, the gateway will offer both REST and gRPC interfaces. 

## Frontend
The application will provide a web fronted with:
- the management console for managing the models
- management console for managing the users and their permissions
- a landing page with dashboard for monitoring the main metrics


## Tech Stack
The LLM Gateway will be build using Spring Framework. It will be a Spring Boot application, and will use Spring AI for accessing the LLMs, Spring Security for authentication and authorization, and Spring Boot Actuator for observability. We will use Spring Data (templates, no ORM) for database access, and Spring Cloud for distributed tracing and observability.

The application will be written in Kotlin, and will use Gradle (kts) as the build tool.

For the rest of the technologies, see AGENTS.md file. In particular:
- Mainly for the configurations, including the rules engine, please, use HOCON format.
- use kotlinx.serialization for serializing the data, instead of Jackson (if possible) 

For the logging of promtps and answers, apart from textual logs, we will use a database. We will use PosgreSQL with its full text search capabilities to enable easy search and analysis of the prompts and answers' text content. The storage, however, should be configurable, and other storage options should include MS SQL (with its full text search) and OpenSearch.

The application will be containerized using Docker, and will be deployed using Kubernetes.