package com.assignment.controller;

import com.assignment.dto.BatchIngestResponse;
import com.assignment.dto.EventRequest;
import com.assignment.service.EventService;
import com.assignment.service.StatsResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events/batch")
    public BatchIngestResponse ingestBatch(
            @RequestBody List<EventRequest> requests
    ) {
        return eventService.ingestBatch(requests);
    }

    @GetMapping("/stats")
    public StatsResult getStats(
            @RequestParam String machineId,
            @RequestParam Instant start,
            @RequestParam Instant end
    ) {
        return eventService.getStats(machineId, start, end);
    }
}
