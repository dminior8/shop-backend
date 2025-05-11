# E-commerce System

This repository consists of **three independent microservices**: `Product Service`, `Cart Service`, and `Order Service`, along with a shared `Common` library. Together, they implement a scalable and well-structured e-commerce backend system. The architecture is built around **Domain-Driven Design (DDD)**, **CQRS (Command Query Responsibility Segregation)**, and **Clean Architecture** principles, ensuring clear separation of concerns, modularity, and maintainability.

The system supports real-time communication between services using **Redis** for caching and session expiration (e.g., for cart expiration) and **RabbitMQ** for asynchronous event-based messaging (e.g., order creation after checkout). All services communicate over REST with **OpenFeign** clients for seamless service-to-service interaction.

## Table of Contents
- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Building and Running the Application](#building-and-running-the-application)
- [Endpoints](#endpoints)
- [Messaging](#messaging)
- [License](#license)

## Overview

The microservices system covers the following core functionalities:

- **Product Service**: Manages product catalog, including product availability, details, and pricing. Exposes product data for other services (e.g., cart) to consume.
- **Cart Service**: Allows users to create carts, add/remove products, and checkout. Each cart automatically **expires after 15 minutes of inactivity**, using **Redis TTL**.
- **Order Service**: Listens to checkout events via **RabbitMQ**, creates orders, and persists them in the database.

The **Common Library** is used to share DTOs, utilities, and mapping logic across services, ensuring consistency and reducing duplication.

## System Architecture

The system is designed with **microservices principles** and follows **Clean Architecture**:

- **Domain Layer**: Pure business logic with no external dependencies.
- **Application Layer**: Use cases (commands, queries) and handlers implementing business processes.
- **Infrastructure Layer**: Database persistence, Redis caching, message brokers (RabbitMQ), Feign clients, etc.
- **API Layer**: REST controllers exposing endpoints for external interaction.

The system is **asynchronous** where needed (e.g., checkout triggers order creation via RabbitMQ) and **synchronous** for immediate data retrieval (e.g., fetching product data via Feign).

## Technologies Used

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Cloud OpenFeign** (service-to-service communication)
- **Spring Data JPA** (PostgreSQL integration)
- **Spring Cache & Redis** (caching and TTL-based cart expiration)
- **RabbitMQ** (event-based communication between Cart and Order services)
- **Docker & Docker Compose** (environment setup)
- **PostgreSQL** (data persistence)
- **Gradle** (build tool)

## Setup

### Prerequisites

- Java 21 (JDK 21)
- Gradle 8.x
- Docker & Docker Compose
- PostgreSQL 14+
- RabbitMQ 3.x+
- Redis 7.x+

### Clone the repository

```bash
git clone https://github.com/dminior8/shop-backend.git
```

```bash
cd shop-backend
```

## Building and Running the Application
The project includes a Docker Compose file to orchestrate the entire environment, including PostgreSQL, Redis, RabbitMQ, and all services.

```bash
docker-compose up --build
```

This will start:
- `product-service` at http://localhost:8081
- `cart-service` at http://localhost:8082
- `order-service` at http://localhost:8083
- `PostgreSQL` at http://localhost:5432
- `Redis` at http://localhost:6379
- `RabbitMQ` (dashboard: http://localhost:15672)


## Endpoints

### Product Service

#### Commands (Write Operations)
| Method   | URL                                      | Description                                                                |
|----------|------------------------------------------|----------------------------------------------------------------------------|
| `POST`   | `/api/v1/products/{productId}/reserve`   | Reserves a product quantity for a cart.                                    |
| `DELETE` | `/api/v1/products/{productId}/release`   | Releases partially reserved product quantity.                              |
| `DELETE` | `/api/v1/products/release-by-cart/{cartId}` | Releases all product reservations for a cart.                           |

#### Queries (Read Operations)
| Method   | URL                          | Description                                                                 |
|----------|------------------------------|-----------------------------------------------------------------------------|
| `GET`    | `/api/v1/products/{id}`      | Retrieves product details by ID.                                            |

---

### Cart Service

#### Commands (Write Operations)
| Method   | URL                                      | Description                                                                |
|----------|------------------------------------------|----------------------------------------------------------------------------|
| `POST`   | `/api/v1/user/{userId}/cart`            | Creates a new cart for the user.                                            |
| `POST`   | `/api/v1/user/{userId}/cart/add-product` | Adds a product to the cart (requires productId and quantity).              |
| `DELETE` | `/api/v1/user/{userId}/cart/remove-product` | Removes a product from the cart (requires productId and quantity).      |
| `POST`   | `/api/v1/user/{userId}/cart/checkout`    | Finalizes cart checkout and initiates order creation.                      |

#### Queries (Read Operations)
| Method   | URL                                      | Description                                                                |
|----------|------------------------------------------|----------------------------------------------------------------------------|
| `GET`    | `/api/v1/user/{userId}/cart`            | Retrieves active cart for the user.                                         |
| `GET`    | `/api/v1/user/{userId}/cart/{cartId}`   | Retrieves specific cart by ID.                                              |
| `GET`    | `/api/v1/user/{userId}/cart/total`      | Calculates total value of the cart.                                         |

---

## Messaging

### Key Exchanges & Routing:
- **cart.events.exchange** (Topic):
  - `product.reserved` - Product reservation confirmed
  - `product.removed` - Product removed from cart
  - `cart.created` - New cart created
  - `cart.expired` - Cart expired due to inactivity

- **order.events.exchange** (Direct):
  - `cart.checkedout` - Cart checkout completed (triggers order creation)

### Queue Bindings:
- `product.reserved.queue` ← product.reserved
- `product.removed.queue` ← product.removed  
- `cart.created.queue` ← cart.created
- `cart.expired.queue` ← cart.expired
- `cart.checkedout.queue` ← cart.checkedout


## Highlights
- CQRS implementation: Commands and queries are completely separated for clarity and scalability.
- TTL Cart Expiration: Redis automatically deletes inactive carts after 15 minutes, ensuring optimal cache usage and session safety.
- Distributed System Design: RabbitMQ decouples cart checkout from order creation, enabling async, scalable processing.
- Service-to-Service Communication: Feign Clients enable type-safe, declarative REST calls between services.
- High Cohesion & Low Coupling: Each microservice owns its own data and logic, while common ensures shared structures and mappings.

## License
This project is licensed under the [MIT License](https://github.com/dminior8/shop-backend/blob/main/LICENSE) - see the LICENSE file for details.
