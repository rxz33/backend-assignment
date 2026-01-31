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

A **unique constraint on `eventId`** ensures database-level deduplication and helps with thread safety.

---

## Deduplication & Update Logic

Deduplication is based on `eventId` and payload comparison:

- **Same eventId + identical payload** → event is deduped (ignored)
- **Same eventId + different payload + newer receivedTime** → existing event is updated
- **Same eventId + different payload + older receivedTime** → ignored

Payload comparison excludes `receivedTime`, as it is metadata added by the backend.

---

## Validation Rules

Events are rejected if:
- `durationMs < 0`
- `durationMs > 6 hours`
- `eventTime` is more than 15 minutes in the future

A `defectCount` of `-1` is stored but ignored in defect calculations.

Validation logic is isolated in a dedicated validator class.

---

## Thread Safety

Thread safety is achieved through:
- Transactional boundaries (`@Transactional`)
- Database unique constraint on `eventId`
- No shared mutable state in service logic

Concurrency is verified using a test that performs parallel ingestion requests.

---

## Performance Strategy

- Batch ingestion is handled within a single transaction
- JPA is used with simple queries and indexing on `eventId`
- The system processes a batch of 1000 events well under the required 1 second on a standard laptop

See `BENCHMARK.md` for details.

---

## API Endpoints

### POST `/events/batch`
Ingests a batch of events.

Response includes:
- accepted
- deduped
- updated
- rejected
- rejection reasons

### GET `/stats`
Query parameters:
- `machineId`
- `start` (inclusive)
- `end` (exclusive)

Returns:
- eventsCount
- defectsCount
- avgDefectRate
- status (`Healthy` or `Warning`)

---

## Tests

The project includes tests covering:
1. Duplicate event deduplication
2. Update with newer receivedTime
3. Ignoring older updates
4. Invalid duration rejection
5. Future eventTime rejection
6. Ignoring defectCount = -1 in stats
7. Correct time window handling
8. Thread safety under concurrent ingestion

---

## Setup & Run Instructions

1. Ensure Java (8 or higher) and Maven are installed
2. Clone the repository
3. Run:
   ```bash
   mvn clean test
4. To start the application:
    mvn spring-boot:run

Improvements With More Time

Add pagination and filtering options

Improve error handling with custom exceptions

Add database indexes for large-scale data

Introduce async ingestion for higher throughput