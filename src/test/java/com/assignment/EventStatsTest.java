package com.assignment;

import com.assignment.dto.EventRequest;
import com.assignment.service.EventService;
import com.assignment.service.StatsResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventStatsTest {

    @Autowired
    private EventService eventService;

    private EventRequest event(
            String id,
            int defect,
            Instant time
    ) {
        EventRequest r = new EventRequest();
        r.setEventId(id);
        r.setDurationMs(1000L);
        r.setDefectCount(defect);
        r.setEventTime(time);
        r.setMachineId("M2");
        return r;
    }

    @Test
    void defectMinusOne_shouldBeIgnored() {
        Instant start = Instant.now();
        Instant mid = start.plusSeconds(60);
        Instant end = start.plusSeconds(3600);

        eventService.ingestBatch(List.of(
                event("A", -1, mid),
                event("B", 2, mid)
        ));

        StatsResult stats =
                eventService.getStats("M2", start, end);

        assertEquals(2, stats.getEventsCount());
        assertEquals(2, stats.getDefectsCount());
        assertEquals("Warning", stats.getStatus());
    }
}
