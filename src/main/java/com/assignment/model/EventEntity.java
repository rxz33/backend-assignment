package com.assignment.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "events",
        uniqueConstraints = @UniqueConstraint(columnNames = "eventId")
)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String eventId;

    @Column(nullable = false)
    private Instant eventTime;

    @Column(nullable = false)
    private Instant receivedTime;

    @Column(nullable = false)
    private String machineId;

    @Column(nullable = false)
    private long durationMs;

    @Column(nullable = false)
    private int defectCount;

    protected EventEntity() {
        // JPA
    }

    public EventEntity(
            String eventId,
            Instant eventTime,
            Instant receivedTime,
            String machineId,
            long durationMs,
            int defectCount
    ) {
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.receivedTime = receivedTime;
        this.machineId = machineId;
        this.durationMs = durationMs;
        this.defectCount = defectCount;
    }

    // -------- getters --------
    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public Instant getReceivedTime() {
        return receivedTime;
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

    // -------- business helpers --------

    public boolean hasSamePayload(EventEntity other) {
        if (other == null) return false;

        return this.eventId.equals(other.getEventId())
                && this.eventTime.equals(other.getEventTime())
                && this.machineId.equals(other.getMachineId())
                && this.durationMs == other.getDurationMs()
                && this.defectCount == other.getDefectCount();
    }

    public boolean isNewerThan(EventEntity other) {
        return this.receivedTime.isAfter(other.getReceivedTime());
    }

    // -------- update method (VS Code safe) --------
    public void updateFrom(EventEntity other) {
        this.eventTime = other.getEventTime();
        this.receivedTime = other.getReceivedTime();
        this.machineId = other.getMachineId();
        this.durationMs = other.getDurationMs();
        this.defectCount = other.getDefectCount();
    }
}
