package com.example.finden_pi.service;

import com.example.finden_pi.dto.Eventdto;
import com.example.finden_pi.model.Event;
import com.example.finden_pi.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(Eventdto dto, String organizerId) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setOrganizerId(organizerId);
        event.setEventDate(dto.getEventDate());
        event.setVenue(dto.getVenue());
        event.setCity(dto.getCity());
        event.setState(dto.getState());
        event.setExpectedGuests(dto.getExpectedGuests());
        event.setBudget(dto.getBudget());
        event.setEventType(dto.getEventType());
        event.setStatus(Event.EventStatus.PLANNING);
        event.setCategoryNeeds(dto.getCategoryNeeds());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(String eventId, Eventdto dto, String organizerId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new RuntimeException("Sem permissão para editar este evento");
        }

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setVenue(dto.getVenue());
        event.setCity(dto.getCity());
        event.setState(dto.getState());
        event.setExpectedGuests(dto.getExpectedGuests());
        event.setBudget(dto.getBudget());
        event.setEventType(dto.getEventType());
        event.setCategoryNeeds(dto.getCategoryNeeds());
        event.setUpdatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(String eventId, String organizerId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new RuntimeException("Sem permissão para deletar este evento");
        }

        eventRepository.delete(event);
    }

    @Transactional
    public void updateStatus(String eventId, Event.EventStatus status) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        event.setStatus(status);
        event.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(event);
    }

    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> findByOrganizer(String organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    public List<Event> findByStatus(Event.EventStatus status) {
        return eventRepository.findByStatus(status);
    }

    public List<Event> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusMonths(6);
        return eventRepository.findByEventDateBetween(now, future);
    }

    @Transactional
    public void addServiceToEvent(String eventId, String serviceId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (!event.getServiceIds().contains(serviceId)) {
            event.getServiceIds().add(serviceId);
            event.setUpdatedAt(LocalDateTime.now());
            eventRepository.save(event);
        }
    }

    @Transactional
    public void removeServiceFromEvent(String eventId, String serviceId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (event.getServiceIds().contains(serviceId)) {
            event.getServiceIds().remove(serviceId);
            event.setUpdatedAt(LocalDateTime.now());
            eventRepository.save(event);
        }
    }
}
