package com.example.finden_pi.repository;

import com.example.finden_pi.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findByOrganizerId(String organizerId);

    List<Event> findByStatus(Event.EventStatus status);

    List<Event> findByEventType(Event.EventType eventType);

    List<Event> findByEventDateBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findByOrganizerIdAndStatus(String organizerId, Event.EventStatus status);

    Long countByOrganizerId(String organizerId);
}
