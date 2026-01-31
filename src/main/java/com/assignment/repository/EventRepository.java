package com.assignment.repository;

import com.assignment.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByEventId(String eventId);

    @Query(
        "SELECT e FROM EventEntity e " +
        "WHERE e.machineId = :machineId " +
        "AND e.eventTime >= :start " +
        "AND e.eventTime < :end"
    )
    List<EventEntity> findByMachineAndTimeWindow(
            @Param("machineId") String machineId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}
