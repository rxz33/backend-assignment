package com.assignment;

import com.assignment.dto.EventRequest;
import com.assignment.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ConcurrencyTest {

    @Autowired
    private EventService eventService;

    private EventRequest event(String id) {
        EventRequest r = new EventRequest();
        r.setEventId(id);
        r.setDurationMs(1000L);
        r.setDefectCount(0);
        r.setEventTime(Instant.now());
        r.setMachineId("MC");
        return r;
    }

    @Test
    void concurrentIngest_shouldNotDuplicate() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                eventService.ingestBatch(
                        List.of(event("CONCURRENT"))
                );
                return null;
            });
        }

        executor.invokeAll(tasks);
        executor.shutdown();

        assertTrue(true);
    }
}
