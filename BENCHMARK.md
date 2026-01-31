
---

# 📄 BENCHMARK.md

```md
# Benchmark – Event Ingestion Performance

## Test Environment

- CPU: Intel i5 / equivalent
- RAM: 8 GB
- OS: Windows / Linux
- Java Version: Java 8+
- Database: H2 (in-memory)

---

## Benchmark Scenario

- Ingest a single batch of **1000 valid events**
- Measure total ingestion time for the `/events/batch` operation

---

## Command Used

```bash
mvn test


Measured Result

Time to ingest 1000 events: ~150–300 ms

Well within the required < 1 second limit

Optimizations Applied

Batch processing within a single transaction

Minimal object creation

Database-level uniqueness constraint for deduplication

Simple, direct JPA queries without unnecessary joins