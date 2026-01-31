package com.assignment.service;

import java.time.Instant;

public class StatsResult {

    private String machineId;
    private Instant start;
    private Instant end;
    private int eventsCount;
    private int defectsCount;
    private double avgDefectRate;
    private String status;

    public StatsResult(
            String machineId,
            Instant start,
            Instant end,
            int eventsCount,
            int defectsCount,
            double avgDefectRate,
            String status
    ) {
        this.machineId = machineId;
        this.start = start;
        this.end = end;
        this.eventsCount = eventsCount;
        this.defectsCount = defectsCount;
        this.avgDefectRate = avgDefectRate;
        this.status = status;
    }

    public String getMachineId() {
        return machineId;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public int getEventsCount() {
        return eventsCount;
    }

    public int getDefectsCount() {
        return defectsCount;
    }

    public double getAvgDefectRate() {
        return avgDefectRate;
    }

    public String getStatus() {
        return status;
    }
}
