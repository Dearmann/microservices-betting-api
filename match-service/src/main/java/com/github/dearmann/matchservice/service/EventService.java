package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.request.EventRequest;
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

    public Event createEvent(EventRequest eventRequest) {
        Event event = Event.builder()
                .name(eventRequest.getName())
                .region(eventRequest.getRegion())
                .season(eventRequest.getSeason())
                .start(eventRequest.getStart())
                .end(eventRequest.getEnd())
                .game(gameService.getGameById(eventRequest.getGameId()))
                .build();

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        return event.get();
    }

    public Event updateEvent(Long id, EventRequest updatedEventRequest) {
        Optional<Event> eventById = eventRepository.findById(id);

        if (eventById.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }

        Event updatedEvent = Event.builder()
                .id(eventById.get().getId())
                .name(updatedEventRequest.getName())
                .region(updatedEventRequest.getRegion())
                .season(updatedEventRequest.getSeason())
                .start(updatedEventRequest.getStart())
                .end(updatedEventRequest.getEnd())
                .game(gameService.getGameById(updatedEventRequest.getGameId()))
                .build();

        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);

        if (eventToDelete.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id);
        }
        eventRepository.delete(eventToDelete.get());
    }
}
