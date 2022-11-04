package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final DtoUtility dtoUtility;

    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = dtoUtility.eventRequestToEvent(eventRequest, null);
        eventRepository.save(event);

        return dtoUtility.eventToEventResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(dtoUtility::eventToEventResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        return dtoUtility.eventToEventResponse(event.get());
    }

    public Event getEventEntityById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        return event.get();
    }

    public EventResponse updateEvent(Long id, EventRequest updatedEventRequest) {
        Optional<Event> eventById = eventRepository.findById(id);

        if (eventById.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }

        Event updatedEvent = dtoUtility.eventRequestToEvent(updatedEventRequest, id);
        eventRepository.save(updatedEvent);

        return dtoUtility.eventToEventResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);

        if (eventToDelete.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        eventRepository.delete(eventToDelete.get());
    }
}
