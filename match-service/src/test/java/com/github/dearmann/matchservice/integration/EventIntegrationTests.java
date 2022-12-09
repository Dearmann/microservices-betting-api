package com.github.dearmann.matchservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.matchservice.BaseTest;
import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.repository.EventRepository;
import com.github.dearmann.matchservice.repository.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class EventIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private DtoUtility dtoUtility;
    private Game game;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        gameRepository.deleteAll();
        game = gameRepository.save(Game.builder()
                .id(0L)
                .name("League of Legends")
                .build());
    }

    @Test
    void shouldCreateEvent() throws Exception {
        EventRequest eventRequest = getEventRequest("Event");
        String eventRequestJSON = objectMapper.writeValueAsString(eventRequest);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(eventRequest.getName())))
                .andExpect(jsonPath("$.region", is(eventRequest.getRegion())))
                .andExpect(jsonPath("$.season", is(eventRequest.getSeason())))
                .andExpect(jsonPath("$.start", is(eventRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(eventRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.gameId", is(eventRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
        Assertions.assertEquals(1, eventRepository.findAll().size());
    }

    @Test
    void shouldGetAllEvents() throws Exception {
        List<Event> eventList = new ArrayList<>();
        eventList.add(dtoUtility.eventRequestToEvent(getEventRequest("Event-1"), 0L));
        eventList.add(dtoUtility.eventRequestToEvent(getEventRequest("Event-2"), 0L));
        eventRepository.saveAll(eventList);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(eventRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetEventById() throws Exception {
        EventRequest eventRequest = getEventRequest("Event");
        Event savedEvent = eventRepository.save(dtoUtility.eventRequestToEvent(eventRequest, 0L));

        mockMvc.perform(get("/events/{id}", savedEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(eventRequest.getName())))
                .andExpect(jsonPath("$.region", is(eventRequest.getRegion())))
                .andExpect(jsonPath("$.season", is(eventRequest.getSeason())))
                .andExpect(jsonPath("$.start", is(eventRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(eventRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.gameId", is(eventRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingEventById() throws Exception {
        mockMvc.perform(get("/events/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        EventRequest eventRequest = getEventRequest("Event");
        Event savedEvent = eventRepository.save(dtoUtility.eventRequestToEvent(eventRequest, 0L));
        EventRequest updatedEventRequest = getEventRequest("Event-updated");
        updatedEventRequest.setRegion("EU");
        updatedEventRequest.setSeason(2);
        updatedEventRequest.setStart(LocalDateTime.parse("2023-11-08T21:00:00"));
        updatedEventRequest.setEnd(LocalDateTime.parse("2023-12-08T21:00:00"));
        String updatedEventRequestJSON = objectMapper.writeValueAsString(updatedEventRequest);

        mockMvc.perform(put("/events/{id}", savedEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedEventRequest.getName())))
                .andExpect(jsonPath("$.region", is(updatedEventRequest.getRegion())))
                .andExpect(jsonPath("$.season", is(updatedEventRequest.getSeason())))
                .andExpect(jsonPath("$.start", is(updatedEventRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(updatedEventRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.gameId", is(eventRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingEvent() throws Exception {
        EventRequest updatedEventRequest = getEventRequest("Event-updated");
        updatedEventRequest.setRegion("EU");
        updatedEventRequest.setSeason(2);
        updatedEventRequest.setStart(LocalDateTime.parse("2023-11-08T21:00:00"));
        updatedEventRequest.setEnd(LocalDateTime.parse("2023-12-08T21:00:00"));
        updatedEventRequest.setGameId(Long.MAX_VALUE);
        String updatedEventRequestJSON = objectMapper.writeValueAsString(updatedEventRequest);

        mockMvc.perform(put("/events/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        EventRequest eventRequest = getEventRequest("Event");
        Event savedEvent = eventRepository.save(dtoUtility.eventRequestToEvent(eventRequest, 0L));

        mockMvc.perform(delete("/events/{id}", savedEvent.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingEvent() throws Exception {
        mockMvc.perform(delete("/events/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private EventRequest getEventRequest(String eventName) {
        return EventRequest.builder()
                .name(eventName)
                .region("Global")
                .season(1)
                .start(LocalDateTime.parse("2022-11-08T21:00:00"))
                .end(LocalDateTime.parse("2022-12-08T21:00:00"))
                .gameId(game.getId())
                .build();
    }
}