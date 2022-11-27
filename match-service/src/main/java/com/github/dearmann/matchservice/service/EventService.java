package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final DtoUtility dtoUtility;
    private final MatchService matchService;

    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = dtoUtility.eventRequestToEvent(eventRequest, 0L);
        event = eventRepository.save(event);

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
            throw new BadEntityIdException("Event not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.eventToEventResponse(event.get());
    }

    public Event getEventEntityById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return event.get();
    }

    public List<EventResponse> getAllEventsByGameId(Long gameId) {
        return eventRepository.findByGameId(gameId)
                .stream()
                .map(dtoUtility::eventToEventResponse)
                .toList();
    }

    public EventResponse updateEvent(EventRequest updatedEventRequest, Long id) {
        Optional<Event> eventById = eventRepository.findById(id);

        if (eventById.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Event updatedEvent = dtoUtility.eventRequestToEvent(updatedEventRequest, id);

        // Can't edit game of an event
        updatedEvent.setGame(eventById.get().getGame());
        updatedEvent = eventRepository.save(updatedEvent);

        return dtoUtility.eventToEventResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);

        if (eventToDelete.isEmpty()) {
            throw new BadEntityIdException("Event not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        eventToDelete.get().getMatches().forEach(match -> {
            matchService.deleteMatch(match.getId());
        });
        eventRepository.delete(eventToDelete.get());
    }
}
