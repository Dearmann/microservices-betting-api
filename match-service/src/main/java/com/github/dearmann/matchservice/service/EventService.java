package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.exception.BadPathVariableException;
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

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadPathVariableException("Event not found ID - " + id);
        }
        return event.get();
    }

    public Event updateEvent(Long id, Event eventUpdated) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new BadPathVariableException("Event not found ID - " + id);
        }
        eventUpdated.setId(id);
        return eventRepository.save(eventUpdated);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);

        if (eventToDelete.isEmpty()) {
            throw new BadPathVariableException("Event not found ID - " + id);
        }
        eventRepository.delete(eventToDelete.get());
    }
}
