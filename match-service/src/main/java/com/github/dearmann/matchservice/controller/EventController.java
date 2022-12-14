package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody EventRequest eventRequest) {
         return eventService.createEvent(eventRequest);
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/by-game/{gameId}")
    public List<EventResponse> getAllEventsByGameId(@PathVariable Long gameId) {
        return eventService.getAllEventsByGameId(gameId);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@Valid @RequestBody EventRequest updatedEventRequest, @PathVariable Long id) {
        return eventService.updateEvent(updatedEventRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

}
