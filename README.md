# Backend Intern Assignment – Event Processing System

## Overview
This project implements a backend system that ingests machine events, deduplicates and updates them based on defined rules, and provides statistical insights over a given time window.

The solution is implemented using **Java + Spring Boot** and is designed to be:
- Correct and spec-compliant
- Thread-safe
- Easy to understand and explain
- Runnable locally with minimal setup

---

## Architecture

The application follows a simple layered architecture:

- **Controller**: Handles HTTP requests and responses
- **Service**: Contains all business logic (validation, deduplication, updates, stats)
- **Repository**: Manages database access using Spring Data JPA
- **Model**: Represents stored event entities
- **DTOs**: Represent API input and output objects
- **Validation**: Isolated validation rules

This separation keeps responsibilities clear and makes the code easy to test and reason about.

---

## Data Model

Each event is stored as an `EventEntity` with the following key fields:
- `eventId` (unique)
- `eventTime`
- `receivedTime` (set by backend)
- `machineId`
- `durationMs`
- `defectCount`

A **unique constraint on `eventId`** is used to enforce deduplication at the database level and assist with thread safety during concurrent ingestion.

---

## Deduplication & Update Logic

Deduplication is based on `eventId` and payload comparison:

- **Same eventId + identical payload** → event is deduped (ignored)
- **Same eventId + different payload + newer receivedTime** → existing event is updated
- **Same eventId + different payload + older receivedTime** → update is ignored

Payload comparison excludes `receivedTime`, as it is backend-generated metadata and not part of the logical event identity.

---

## Validation Rules

Events are rejected if:
- `durationMs < 0`
- `durationMs > 6 hours`
- `eventTime` is more than 15 minutes in the future

A `defectCount` of `-1` represents an unknown defect count. Such events are stored but ignored in defect-related calculations.

Validation logic is isolated in a dedicated validator class.

---

## Thread Safety

Thread safety is achieved through:
- Transactional boundaries using `@Transactional`
- Database-level unique constraint on `eventId`
- Stateless service logic with no shared mutable in-memory state

Concurrency behavior is verified using tests that simulate parallel ingestion requests.

---

## Performance Strategy

- Events are ingested in batches within a single transaction
- Simple JPA queries are used with indexing on `eventId`
- No unnecessary synchronization or locking is used
- The system processes a batch of **1000 events under 1 second** on a standard laptop

See `BENCHMARK.md` for details.

---

## API Endpoints

### POST `/events/batch`
Ingests a batch of events.

Response includes:
- `accepted`
- `deduped`
- `updated`
- `rejected`
- rejection reasons

### GET `/stats`
Query parameters:
- `machineId`
- `start` (inclusive)
- `end` (exclusive)

Returns:
- `eventsCount`
- `defectsCount`
- `avgDefectRate`
- `status` (`Healthy` or `Warning`)

---

## Tests

The project includes tests covering:
1. Deduplication of identical events
2. Updates with newer `receivedTime`
3. Ignoring older updates
4. Rejection of invalid duration
5. Rejection of future `eventTime`
6. Ignoring `defectCount = -1` in stats
7. Correct handling of start/end time boundaries
8. Thread safety under concurrent ingestion

---

## Edge Cases & Assumptions

- `receivedTime` from the request payload is ignored and always set by the backend
- Validation is applied before deduplication
- Events with `defectCount = -1` are excluded from defect calculations
- Deduplication is strictly based on `eventId`
- Concurrent updates are resolved using transactional semantics and database constraints

These decisions were made to keep the system deterministic, simple, and aligned with the problem statement.

---

## Setup & Run Instructions

1. Ensure **Java 17** is installed
2. Clone the repository
3. Run tests:
   ```bash
   ./mvnw.cmd clean test

