# SMS Platform — Enterprise-Grade Messaging Service

An enterprise-level SMS sending platform positioned **between internal business systems and telecom operators**,  
providing **stable, scalable, and controllable** messaging capabilities.

The platform supports multiple SMS delivery scenarios and includes comprehensive validation, rate limiting, and strategy control mechanisms.  
By leveraging message queues and a microservice architecture, it ensures system stability under high-concurrency traffic.

---

## Project Background

In real-world business scenarios, enterprise SMS systems typically face the following challenges:

- Integration with multiple telecom operators or third-party SMS providers, each with different protocols
- Large sending volumes with obvious traffic peaks
- Different clients requiring customized risk control, validation, and billing rules
- The need for traceable and queryable message delivery results
- Unstable or delayed delivery receipts from operators

The goal of this project is **not just to “send SMS”**, but to build:

> **A highly available, scalable, and maintainable enterprise-level SMS infrastructure platform**

<img width="1245" height="609" alt="image" src="https://github.com/user-attachments/assets/d67bc504-b1e4-47f6-9de2-6798d3e39933" />


---

## Project Positioning

- **Role**:  
  The platform does not directly serve end users. Instead, it acts as a **middleware layer** providing unified SMS capabilities for internal enterprise systems.

- **Responsibilities**:  
  Fully responsible for **validation, scheduling, delivery, receipt processing, and logging** of SMS messages.

- **Typical Scenarios**:
  - Verification code SMS (high timeliness)
  - Notification SMS (high delivery rate)
  - Marketing SMS (high concurrency, batch sending)

---

## System Architecture Overview

> **Architecture Description**  
> The system adopts a microservices architecture.  
> - **Nacos** is used for service discovery and configuration management  
> - **RabbitMQ** is responsible for traffic buffering and peak shaving  
> - **Redis** handles hot data and counters  
> - **Elasticsearch** stores massive SMS logs for querying and analysis
<img width="7475" height="4439" alt="whiteboard_exported_image" src="https://github.com/user-attachments/assets/e72cdd79-5910-405f-84c3-ce76e58bfd8c" />

---

## Technology Stack

### Backend & Middleware

- **Framework**: Java / Spring Boot / Spring Cloud Alibaba
- **Service Registry & Config Center**: Nacos
- **Message Queue**: RabbitMQ (asynchronous decoupling, traffic smoothing, delayed retries)
- **Caching & Rate Limiting**: Redis (ZSet sliding window, pipeline batch operations)
- **Log Storage**: Elasticsearch (large-scale SMS records and queries)
- **Persistence**: MySQL (core business configuration and metadata)

### Communication & Deployment

- **Network Communication**: Netty (high-performance handling of telecom TCP/HTTP protocols)
- **Containerization**: Docker / Kubernetes
- **CI/CD**: Jenkins / GitLab CI

---

## Module Design

The system is divided into four core service modules with clear responsibility boundaries:

```text
├── api-service        # External API service (entry point)
├── strategy-service   # Strategy & validation service (risk control)
├── gateway-service    # SMS gateway service (delivery)
├── search-service     # Query & analytics service (logging)
└── common             # Shared components
```

## Core Service Modules

### 1. API Service (`api-service`)

#### Responsibilities
- Provides unified **HTTP / RPC** SMS sending APIs to external systems.
- Handles platform-level basic validation:
  - API Key authentication
  - IP whitelist validation
  - Signature verification
  - Template validation
  - Phone number format checking

#### Design Principles
- **Lightweight**  
  The API layer avoids complex business logic to ensure fast response times.
- **Asynchronous**  
  After basic validation, requests are immediately published to MQ, freeing clients from synchronous waiting.

---

### 2. Strategy Service (`strategy-service`)

#### Responsibilities
- Consumes messages from MQ and performs business-level validations.
- Loads different validation strategies based on client-specific configurations.

#### Core Capabilities
- **Pluggable validation chains**  
  Supports sensitive word filtering, blacklist checks, regional restrictions, etc.
- **Configuration-driven**  
  Clients can dynamically configure rule order and enable/disable specific checks.

---

### 3. Gateway Service (`gateway-service`)

#### Responsibilities
- Integrates with real telecom operators or third-party SMS providers  
  (e.g., Alibaba Cloud, Tencent Cloud).
- Abstracts and hides protocol differences  
  (HTTP / TCP / SMPP).

#### Implementation Details
- **Asynchronous Netty communication**  
  High-performance long-lived connections built on Netty.
- **Dual receipt handling**
  - Submission receipt (whether the operator accepted the message)
  - Delivery receipt (whether the end user received it)
- **Dedicated thread pools**  
  Callback processing is isolated from I/O threads to prevent blocking.

---

### 4. Search Service (`search-service`)

#### Responsibilities
- Consumes SMS delivery result logs.
- Provides query and analytics APIs for operations and monitoring.

#### Technical Features
- Built on **Elasticsearch** for massive data storage.
- Supports full-text search:
  - Phone number
  - Message content
  - Timestamp
- Provides aggregation analysis  
  (e.g., delivery success rate).

---

## Key Design Highlights

### 1. Dynamic Validation Chain

- **Design Pattern**  
  Chain of Responsibility to decouple validation logic.
- **Layered Validation**  
  Separate validation chains for the API layer and the strategy layer.
- **Flexibility**  
  Validation rules can be dynamically adjusted via **Nacos** configuration  
  without service restarts.

---

### 2. High-Performance Sensitive Word Filtering (DFA)

- **Algorithm**  
  Deterministic Finite Automaton (DFA).
- **Optimization**  
  Sensitive word dictionary is built as a Trie and resident in JVM memory.
- **Benefit**  
  Eliminates frequent database or Redis access, enabling millisecond-level matching.

---

### 3. Multi-Dimensional Rate Limiting

#### Dimensions
- Per-phone-number rate limiting
- Per-client rate limiting

#### Strategies
- Multiple time windows:
  - 1 minute
  - 1 hour
  - 24 hours

#### Implementation
- Sliding window algorithm based on **Redis ZSet**,  
  ensuring precision and consistency.

---

### 4. Reliable Delivery Receipts & Retry Mechanism

- **Callback Mechanism**  
  Final delivery status (success / failure) is pushed to client systems via HTTP callbacks.
- **Failure Retry**  
  Uses **RabbitMQ Delayed Exchange** to automatically retry failed callbacks.
- **Configurability**  
  Maximum retry attempts and retry intervals are fully configurable  
  to handle network instability.

---

## JVM & Performance Optimization

To handle high-concurrency scenarios (especially bulk marketing SMS),  
JVM tuning is applied to core services.

### Optimization Strategy

- **Expanded Young Generation**  
  SMS-related objects are mostly short-lived.  
  Enlarging the young generation reduces premature promotion to the old generation  
  and lowers Full GC frequency.

- **Garbage Collector**  
  **G1 GC** is used instead of CMS to provide better stability  
  and predictable pause times for large heaps.

### Sample JVM Parameters

```bash
-Xms4g -Xmx4g
-XX:NewSize=2048M
-XX:MaxNewSize=2048M
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
```

## System Scale & Performance Metrics

### Throughput
- **Peak QPS**: ~ **3000+**  
  (scales horizontally with cluster size)
- **Normal QPS**: **50 ~ 60**

### Deployment Architecture
- All core services are deployed with **multiple instances**
- Eliminates **Single Points of Failure (SPOF)**

### Container Resources
- **Core Services**:  
  `2C 4G` (2 CPU cores, 4GB RAM)

- **Middleware**:  
  Deployed as **independent clusters** with physical resource isolation

---

## Deployment

### Containerization
- All services are built as **Docker images**

### Orchestration
- **Kubernetes (K8s)** is used for resource scheduling and management

### Release Strategy
- Supports **Rolling Updates** for zero-downtime deployments
- Configured with **Horizontal Pod Autoscaling (HPA)**  
  based on CPU and memory metrics
