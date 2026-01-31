package com.assignment.service;

import com.assignment.dto.BatchIngestResponse;
import com.assignment.dto.EventRequest;
import com.assignment.model.EventEntity;
import com.assignment.repository.EventRepository;
import com.assignment.validation.EventValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ---------------- INGEST ----------------

    @Transactional
    public BatchIngestResponse ingestBatch(List<EventRequest> requests) {

        BatchIngestResponse response = new BatchIngestResponse();

        for (EventRequest request : requests) {
            try {
                EventValidator.validate(request);

                Instant receivedTime = Instant.now();

                Optional<EventEntity> existingOpt =
                        eventRepository.findByEventId(request.getEventId());

                if (!existingOpt.isPresent()) {
                    EventEntity entity = new EventEntity(
                            request.getEventId(),
                            request.getEventTime(),
                            receivedTime,
                            request.getMachineId(),
                            request.getDurationMs(),
                            request.getDefectCount()
                    );
                    eventRepository.save(entity);
                    response.incrementAccepted();
                } else {
                    EventEntity existing = existingOpt.get();

                    EventEntity incoming = new EventEntity(
                            request.getEventId(),
                            request.getEventTime(),
                            receivedTime,
                            request.getMachineId(),
                            request.getDurationMs(),
                            request.getDefectCount()
                    );

                    if (incoming.hasSamePayload(existing)) {
                        response.incrementDeduped();
                    } else if (incoming.isNewerThan(existing)) {
                        existing.updateFrom(incoming);
                        eventRepository.save(existing);
                        response.incrementUpdated();
                    } else {
                        response.incrementDeduped();
                    }
                }

            } catch (IllegalArgumentException ex) {
                response.incrementRejected();
                response.addRejection(request.getEventId(), ex.getMessage());
            }
        }

        return response;
    }

    // ---------------- STATS ----------------

    @Transactional(readOnly = true)
    public StatsResult getStats(String machineId, Instant start, Instant end) {

        List<EventEntity> events =
                eventRepository.findByMachineAndTimeWindow(machineId, start, end);

        int eventsCount = events.size();
        int defectsCount = 0;

        for (EventEntity event : events) {
            if (event.getDefectCount() >= 0) {
                defectsCount += event.getDefectCount();
            }
        }

        double windowHours =
                (end.getEpochSecond() - start.getEpochSecond()) / 3600.0;

        double avgDefectRate =
                windowHours > 0 ? defectsCount / windowHours : 0.0;

        String status = avgDefectRate < 2.0 ? "Healthy" : "Warning";

        return new StatsResult(
                machineId,
                start,
                end,
                eventsCount,
                defectsCount,
                avgDefectRate,
                status
        );
    }
}
