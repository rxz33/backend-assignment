# BENCHMARK – Event Ingestion Performance

## Laptop Specifications

- CPU: AMD Ryzen 5 7520U (2.80 GHz)
- RAM: 8 GB
- OS: Windows 11 (64-bit)
- Java Version: Java 17
- Database: H2 (in-memory)

---

## Benchmark Scenario

- Ingest one batch of **1000 events**
- Endpoint: `POST /events/batch`
- Events include valid events, duplicates, and defectCount = -1 cases

---

## Command Used

```bash
./mvnw.cmd clean test
```

---

## Measured Result

- Time to ingest 1000 events: ~400 ms
- Result: Under the required 1 second limit 

---

## Optimizations

- Batch processing instead of individual event ingestion
- Deduplication using eventId