Here is your **README.md** content as plain text:

---

# Order Management System (Hexagonal Architecture)

A clean architecture **Spring Boot** backend project implementing an Order Management System using:

* **Hexagonal Architecture (Ports & Adapters)**
* **Domain-Driven Design (DDD) principles**
* **Spring Boot**
* **MapStruct**
* **Vavr (Option, Either, functional style)**
* **JPA / Hibernate**
* **Bean Validation**
* **Global Exception Handling**

---

## Project Overview

This project demonstrates a production-style backend structure separating:

* **Domain Layer** → Business logic
* **Application Layer** → Use cases / Services
* **Infrastructure Layer** → Database, mappers, persistence
* **Adapter Layer** → Web controllers (REST APIs)
* **Shared Layer** → DTOs, mappers, common responses

The goal is to maintain:

* Clean separation of concerns
* Testable domain logic
* Minimal framework dependency in core domain
* Clear dependency direction (outside → inside)

---

## Architecture Structure

```
ordermanager
│
├── domain
│   ├── model
│   ├── repository (ports)
│   └── exceptions
│
├── application
│   └── service
│
├── adapter
│   └── in
│       └── web (controllers, exception handlers)
│
├── infrastructure
│   ├── persistence (JPA entities, repositories)
│   └── mapper (MapStruct mappers)
│
└── shared
    ├── dto
    └── web (ApiError, responses)
```

---

## Features

### User

* Create user
* Manage user addresses
* Fetch user details

### Item

* Create item
* Update item
* Manage availability

### Order

* Create order
* Update order status
* Add order items
* Calculate total price

---

##  Technologies Used

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* MapStruct
* Vavr
* Lombok
* H2 / MSSQL (configurable)
* Maven

---

##  Functional Programming (Vavr)

The project uses **Vavr’s `Option`** instead of Java’s `Optional` in the domain and service layers.

Example:

```java
return orderService.createOrder(order)
        .map(orderMapper::toResponse)
        .map(ResponseEntity::ok)
        .getOrElse(ResponseEntity.badRequest().build());
```

Benefits:

* Null safety
* Functional chaining
* Cleaner error handling

---

##  Exception Handling

Global exception handling is implemented using:

* `@RestControllerAdvice`
* Custom exceptions:

    * `EntityNotFoundException`
    * `InsufficientStockException`
    * Validation exceptions

Error response format:

```json
{
  "timestamp": "2026-02-20T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/orders",
  "violations": [
    {
      "field": "quantity",
      "message": "must be greater than 0"
    }
  ]
}
```

---

##  Mapping Strategy

* DTO ↔ Domain → MapStruct
* Domain ↔ Entity → MapStruct
* Explicit constructor usage in domain models
* Avoided ambiguous constructors by defining clear mapping rules

---

##  Validation

Uses:

* `@Valid`
* `@NotNull`
* `@NotBlank`
* `@Positive`

Validation errors are formatted through `ApiError`.

---
You're right — that was my mistake.

If your project **is not Maven**, then it’s almost certainly **Gradle** (since Spring Initializr defaults to either Maven or Gradle).

Here’s the corrected README section for a **Gradle-based** project 👇

---

# Order Management System (Hexagonal Architecture)

A Spring Boot backend project built using:

* Hexagonal Architecture (Ports & Adapters)
* Domain-Driven Design principles
* Spring Boot
* MapStruct
* Vavr
* JPA / Hibernate
* Gradle

---

## ⚙️ Technologies Used

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* MapStruct
* Vavr
* Lombok
* H2 / MSSQL
* **Gradle**

---

##  Running the Project (Gradle)

### 1️ Build the project

If using Gradle Wrapper:

```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

---

### 2️ Run the application

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```
---

## Design Principles Applied

* Dependency Inversion Principle
* Single Responsibility Principle
* Separation of Concerns
* Explicit domain modeling
* Clean API contracts
* Functional style error handling

---

## Future Improvements

* Unit tests for domain layer
* Integration tests
* Swagger / OpenAPI documentation
* Authentication & Authorization (JWT)
* Pagination support
* Caching
* Docker support