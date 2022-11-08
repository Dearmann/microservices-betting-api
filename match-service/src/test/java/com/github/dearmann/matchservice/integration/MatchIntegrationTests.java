package com.github.dearmann.matchservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.matchservice.BaseTest;
import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.model.Match;
import com.github.dearmann.matchservice.model.Team;
import com.github.dearmann.matchservice.repository.EventRepository;
import com.github.dearmann.matchservice.repository.GameRepository;
import com.github.dearmann.matchservice.repository.MatchRepository;
import com.github.dearmann.matchservice.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
class MatchIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        matchRepository.deleteAll();
    }

    @Test
    void shouldCreateMatch() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        String matchRequestJSON = objectMapper.writeValueAsString(matchRequest);

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andExpect(jsonPath("$.team1Won", is(matchRequest.getTeam1Won())))
                .andExpect(jsonPath("$.start", is(matchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.estimatedEnd", is(matchRequest.getEstimatedEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.matchOver", is(matchRequest.getMatchOver())))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andDo(print());
        Assertions.assertEquals(1, matchRepository.findAll().size());
    }

    @Test
    void shouldGetAllMatchs() throws Exception {
        List<Match> matchList = new ArrayList<>();
        matchList.add(dtoUtility.matchRequestToMatch(getMatchRequest(), 0L));
        matchList.add(dtoUtility.matchRequestToMatch(getMatchRequest(), 0L));
        matchRepository.saveAll(matchList);

        mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(matchRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetMatchById() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        Match savedMatch = matchRepository.save(dtoUtility.matchRequestToMatch(matchRequest, 0L));

        mockMvc.perform(get("/matches/{id}", savedMatch.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andExpect(jsonPath("$.team1Won", is(matchRequest.getTeam1Won())))
                .andExpect(jsonPath("$.start", is(matchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.estimatedEnd", is(matchRequest.getEstimatedEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.matchOver", is(matchRequest.getMatchOver())))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllMatchs() throws Exception {
        mockMvc.perform(get("/matches/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateMatch() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        Match savedMatch = matchRepository.save(dtoUtility.matchRequestToMatch(matchRequest, 0L));
        MatchRequest updatedMatchRequest = getMatchRequest();
        updatedMatchRequest.setTeam1Won(true);
        updatedMatchRequest.setStart(LocalDateTime.parse("2023-11-08T21:00:00"));
        updatedMatchRequest.setEstimatedEnd(LocalDateTime.parse("2023-12-08T21:00:00"));
        updatedMatchRequest.setMatchOver(true);
        String updatedMatchRequestJSON = objectMapper.writeValueAsString(updatedMatchRequest);

        mockMvc.perform(put("/matches/{id}", savedMatch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMatchRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andExpect(jsonPath("$.team1Won", is(updatedMatchRequest.getTeam1Won())))
                .andExpect(jsonPath("$.start", is(updatedMatchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.estimatedEnd", is(updatedMatchRequest.getEstimatedEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.matchOver", is(updatedMatchRequest.getMatchOver())))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingMatch() throws Exception {
        MatchRequest updatedMatchRequest = getMatchRequest();
        updatedMatchRequest.setTeam1Id(11L);
        updatedMatchRequest.setTeam2Id(12L);
        updatedMatchRequest.setTeam1Won(true);
        updatedMatchRequest.setStart(LocalDateTime.parse("2023-11-08T21:00:00"));
        updatedMatchRequest.setEstimatedEnd(LocalDateTime.parse("2023-12-08T21:00:00"));
        updatedMatchRequest.setMatchOver(true);
        updatedMatchRequest.setEventId(13L);
        String updatedMatchRequestJSON = objectMapper.writeValueAsString(updatedMatchRequest);

        mockMvc.perform(put("/matches/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMatchRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteMatch() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        Match savedMatch = matchRepository.save(dtoUtility.matchRequestToMatch(matchRequest, 0L));

        mockMvc.perform(delete("/matches/{id}", savedMatch.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingMatch() throws Exception {
        mockMvc.perform(delete("/matches/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private MatchRequest getMatchRequest() {
        Game game = Game.builder()
                .id(0L)
                .name("League of Legends")
                .build();
        game = gameRepository.save(game);
        Event event = Event.builder()
                .id(0L)
                .name("Test event")
                .region("Global")
                .season(1)
                .start(LocalDateTime.parse("2022-11-08T21:00:00"))
                .end(LocalDateTime.parse("2022-12-08T21:00:00"))
                .game(game)
                .build();
        event = eventRepository.save(event);
        Team team1 = Team.builder()
                .id(0L)
                .name("Team 1")
                .game(game)
                .build();
        team1 = teamRepository.save(team1);
        Team team2 = Team.builder()
                .id(0L)
                .name("Team 2")
                .game(game)
                .build();
        team2 = teamRepository.save(team2);

        return MatchRequest.builder()
                .team1Id(team1.getId())
                .team2Id(team2.getId())
                .team1Won(false)
                .start(LocalDateTime.parse("2022-11-08T21:00:00"))
                .estimatedEnd(LocalDateTime.parse("2022-12-08T21:00:00"))
                .matchOver(false)
                .eventId(event.getId())
                .build();
    }
}