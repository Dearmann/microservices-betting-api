package com.github.dearmann.matchservice.service;

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
    private final GameService gameService;

    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = eventRequestToEvent(eventRequest);
        eventRepository.save(event);

        return eventToEventResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::eventToEventResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        return eventToEventResponse(event.get());
    }

    public EventResponse updateEvent(Long id, EventRequest updatedEventRequest) {
        Optional<Event> eventById = eventRepository.findById(id);

        if (eventById.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }

        Event updatedEvent = eventRequestToEvent(updatedEventRequest);
        eventRepository.save(updatedEvent);

        return eventToEventResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);

        if (eventToDelete.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        eventRepository.delete(eventToDelete.get());
    }

    private Event eventRequestToEvent(EventRequest eventRequest) {
        return Event.builder()
                .name(eventRequest.getName())
                .region(eventRequest.getRegion())
                .season(eventRequest.getSeason())
                .start(eventRequest.getStart())
                .end(eventRequest.getEnd())
                .game(gameService.getGameById(eventRequest.getGameId()))
                .build();
    }

    private EventResponse eventToEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .region(event.getRegion())
                .season(event.getSeason())
                .start(event.getStart())
                .end(event.getEnd())
                .gameId(event.getGame().getId())
                .build();
    }
}
