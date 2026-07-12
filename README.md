# 🚀 LinkedIn-Inspired Social Networking Platform

A **social networking platform** built using **Java Spring Boot Microservices** following cloud-native architecture and event-driven design principles.

The application demonstrates enterprise backend development practices including **API Gateway authentication, service discovery, graph databases, event-driven communication, containerization, and Kubernetes deployment**.

---

# 📌 Table of Contents

- Overview
- Architecture
- Technology Stack
- Microservices
- Features
- System Architecture
- Project Structure
- Getting Started
- Running with Docker Compose
- Running on Kubernetes
- API Flow
- Event-Driven Communication
- Security
- Databases
- Docker Images
- Future Enhancements
- Author

---

# 📖 Overview

The platform provides the core functionality of a professional social networking application similar to LinkedIn.

Users can:

- Register and login securely
- Create and manage posts
- Send and accept connection requests
- Build professional networks
- Receive real-time notifications
- Communicate through scalable microservices

The project follows modern backend architecture using independent microservices connected through REST APIs and Apache Kafka.

---

# 🏗️ Architecture

```
                        +----------------------+
                        |      Client App      |
                        +----------+-----------+
                                   |
                                   |
                        +----------v-----------+
                        |     API Gateway      |
                        | JWT Authentication   |
                        | Header Propagation   |
                        +----------+-----------+
                                   |
             ---------------------------------------------
             |            |             |                |
             |            |             |                |
     +-------v----+ +-----v------+ +----v---------+ +----v-------------+
     | User       | | Post       | | Connection   | | Notification     |
     | Service    | | Service    | | Service      | | Service          |
     +-------+----+ +------+-----+ +------+-------+ +---------+--------+
             |             |              |                   |
       PostgreSQL    PostgreSQL         Neo4j           PostgreSQL
             |             |                                  |
             +-------------+----------------------------------+
                           |
                     Apache Kafka
                    Event Messaging

```

Service Discovery is handled by **Netflix Eureka**.

---

# 🛠️ Technology Stack

| Category | Technologies |
|-----------|--------------|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| Microservices | Spring Cloud |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Security | Spring Security, JWT |
| Communication | OpenFeign |
| Messaging | Apache Kafka |
| Relational Database | PostgreSQL |
| Graph Database | Neo4j |
| Build Tool | Maven |
| Containerization | Docker |
| Orchestration | Kubernetes |
| Utilities | Lombok |

---

# 📦 Microservices

| Service | Port | Database | Description |
|----------|------|----------|-------------|
| Discovery Server | 8761 | — | Service Registry |
| API Gateway | 8080 | — | Authentication, Routing |
| User Service | 9094 | PostgreSQL | User Management |
| Post Service | 9095 | PostgreSQL | Posts & Feed |
| Connection Service | 9096 | Neo4j | Professional Connections |
| Notification Service | 9097 | PostgreSQL | Notifications |

---

# ✨ Features

## Authentication

- JWT based authentication
- Spring Security integration
- Stateless authentication
- Token validation at API Gateway

---

## API Gateway

- Centralized routing
- JWT validation
- Request filtering
- Automatic propagation of `X-User-Id`
- Authentication before forwarding requests

---

## User Service

- User Registration
- Login
- User Profile
- User Information APIs

---

## Post Service

- Create Post
- Like Post
- Fetch Posts
- Event publishing after post creation
- Event publishing after post likes

---

## Connection Service

Powered by **Neo4j** graph database.

Relationship types:

```
(User)-[:REQUESTED_TO]->(User)

(User)-[:CONNECTED_TO]->(User)
```

Features:

- Send Connection Request
- Accept Connection
- Reject Request
- View Connections

---

## Notification Service

Receives Kafka events for:

- New Connection Request
- Connection Accepted
- New Post
- Post Like

Stores notifications for users.

---

# 🔄 Event-Driven Communication

Apache Kafka enables asynchronous communication.

Example Events

```
Post Created
      |
      v
Kafka Topic
      |
      v
Notification Service
```

```
Connection Accepted
      |
      v
Kafka Topic
      |
      v
Notification Service
```

Benefits:

- Loose coupling
- Scalability
- Better fault tolerance
- Independent service deployment

---

# 🔐 Security

Authentication flow:

```
Client
   |
   | JWT Token
   |
API Gateway
   |
Validate JWT
   |
Extract UserId
   |
Add Header

X-User-Id

   |
Forward Request
   |
Downstream Services
```

Only authenticated requests are forwarded to backend services.

---

# 🗄️ Databases

## PostgreSQL

Used by:

- User Service
- Post Service
- Notification Service

---

## Neo4j

Used by:

- Connection Service

Graph relationships:

```
CONNECTED_TO

REQUESTED_TO
```

Graph database allows efficient traversal of user relationships and network queries.

---

# 📂 Project Structure

```
linkedin-app/
│
├── api-gateway/
├── discovery-server/
├── user-service/
├── post-service/
├── connection-service/
├── notification-service/
│
├── docker-compose.yml
├── k8s/
│   ├── api-gateway.yaml
│   ├── discovery-server.yaml
│   ├── kafka.yaml
│   ├── postgres.yaml
│   ├── neo4j.yaml
│   └── ...
│
└── README.md
```

---

# 🚀 Getting Started

## Prerequisites

- Java 21
- Maven
- Docker
- Docker Compose
- Kubernetes (optional)
- kubectl (optional)

---

# 🐳 Running with Docker Compose

Start the complete application

```bash
docker compose up -d
```

Stop all services

```bash
docker compose down
```

View logs

```bash
docker compose logs -f
```

---

# ☸️ Running on Kubernetes

Deploy all resources

```bash
kubectl apply -f k8s/
```

Check pods

```bash
kubectl get pods
```

Check services

```bash
kubectl get svc
```

Delete deployment

```bash
kubectl delete -f k8s/
```

---

# 🌐 Service URLs

| Service | URL |
|----------|-----|
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| User Service | http://localhost:9094 |
| Post Service | http://localhost:9095 |
| Connection Service | http://localhost:9096 |
| Notification Service | http://localhost:9097 |

---

# 🔁 Request Flow

```
Client
   |
   |
JWT Request
   |
API Gateway
   |
JWT Validation
   |
Forward Request
   |
Microservice
   |
Database
```

---

# 📨 Kafka Events

Example Topics

```
postCreatedTopic

postLikedTopic

sendConnectionRequestTopic

acceptConnectionRequestTopic
```

---

# 🐳 Docker Images

| Image | Description |
|--------|-------------|
| `deejnv007/linkedin-app-discovery-server` | Discovery Server |
| `deejnv007/linkedin-app-api-gateway` | API Gateway |
| `deejnv007/linkedin-app-user-service` | User Service |
| `deejnv007/linkedin-app-post-service` | Post Service |
| `deejnv007/linkedin-app-connection-service` | Connection Service |
| `deejnv007/linkedin-app-notification-service` | Notification Service |

---

# 📈 Future Enhancements

- News Feed Algorithm
- Comments
- Messaging
- File Uploads
- Search Service (ElasticSearch)
- Redis Caching
- Rate Limiting
- Observability with Prometheus & Grafana
- Distributed Tracing
- CI/CD using GitHub Actions
- Helm Charts

---

# 👨‍💻 Author

**Deepansu Gupta**

Java Backend Developer

- 💼 LinkedIn: https://www.linkedin.com/in/deepansu-gupta
- 💻 GitHub: https://github.com/deejnv007

---

# ⭐ If you found this project useful

Give this repository a ⭐ on GitHub and feel free to contribute!
