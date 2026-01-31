package com.assignment;

import com.assignment.dto.BatchIngestResponse;
import com.assignment.dto.EventRequest;
import com.assignment.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventIngestTest {

    @Autowired
    private EventService eventService;

    private EventRequest event(
            String id,
            long duration,
            int defects,
            Instant time
    ) {
        EventRequest r = new EventRequest();
        r.setEventId(id);
        r.setDurationMs(duration);
        r.setDefectCount(defects);
        r.setEventTime(time);
        r.setMachineId("M1");
        return r;
    }

    @Test
    void duplicateEvent_shouldBeDeduped() {
        Instant now = Instant.now();

        EventRequest e1 = event("E1", 1000, 0, now);
        EventRequest e2 = event("E1", 1000, 0, now);

        BatchIngestResponse res =
                eventService.ingestBatch(List.of(e1, e2));

        assertEquals(1, res.getAccepted());
        assertEquals(1, res.getDeduped());
        assertEquals(0, res.getRejected());
    }

    @Test
    void invalidDuration_shouldBeRejected() {
        EventRequest bad =
                event("E2", -5, 0, Instant.now());

        BatchIngestResponse res =
                eventService.ingestBatch(List.of(bad));

        assertEquals(1, res.getRejected());
    }
}
