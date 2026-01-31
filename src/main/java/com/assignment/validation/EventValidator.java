package com.assignment.validation;

import com.assignment.dto.EventRequest;

import java.time.Duration;
import java.time.Instant;

public class EventValidator {

    private static final long MAX_DURATION_MS = Duration.ofHours(6).toMillis();
    private static final Duration FUTURE_TIME_LIMIT = Duration.ofMinutes(15);

    private EventValidator() {
        
    }

    public static void validate(EventRequest event) {
        if (event.getDurationMs() < 0 || event.getDurationMs() > MAX_DURATION_MS) {
            throw new IllegalArgumentException("INVALID_DURATION");
        }

        Instant now = Instant.now();
        if (event.getEventTime().isAfter(now.plus(FUTURE_TIME_LIMIT))) {
            throw new IllegalArgumentException("EVENT_TIME_IN_FUTURE");
        }
    }
}
