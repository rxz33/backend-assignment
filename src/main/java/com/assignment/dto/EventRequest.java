package com.assignment.dto;

import java.time.Instant;

public class EventRequest {

    private String eventId;
    private Instant eventTime;
    private String machineId;
    private long durationMs;
    private int defectCount;

    // ---- getters ----
    public String getEventId() {
        return eventId;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public String getMachineId() {
        return machineId;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public int getDefectCount() {
        return defectCount;
    }

    // ---- setters (IMPORTANT FIX) ----
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }
}
