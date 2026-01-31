package com.assignment.dto;

import java.util.ArrayList;
import java.util.List;

public class BatchIngestResponse {

    private int accepted;
    private int deduped;
    private int updated;
    private int rejected;

    private List<Rejection> rejections = new ArrayList<>();

    public int getAccepted() {
        return accepted;
    }

    public int getDeduped() {
        return deduped;
    }

    public int getUpdated() {
        return updated;
    }

    public int getRejected() {
        return rejected;
    }

    public List<Rejection> getRejections() {
        return rejections;
    }

    // increment helpers
    public void incrementAccepted() {
        this.accepted++;
    }

    public void incrementDeduped() {
        this.deduped++;
    }

    public void incrementUpdated() {
        this.updated++;
    }

    public void incrementRejected() {
        this.rejected++;
    }

    public void addRejection(String eventId, String reason) {
        this.rejections.add(new Rejection(eventId, reason));
    }

    // inner class for rejection details
    public static class Rejection {
        private String eventId;
        private String reason;

        public Rejection(String eventId, String reason) {
            this.eventId = eventId;
            this.reason = reason;
        }

        public String getEventId() {
            return eventId;
        }

        public String getReason() {
            return reason;
        }
    }
}
