package com.github.dearmann.matchservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.matchservice.BaseTest;
import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.model.*;
import com.github.dearmann.matchservice.repository.EventRepository;
import com.github.dearmann.matchservice.repository.GameRepository;
import com.github.dearmann.matchservice.repository.MatchRepository;
import com.github.dearmann.matchservice.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    private Game game;
    private Event event;
    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        eventRepository.deleteAll();
        gameRepository.deleteAll();
        game = gameRepository.save(Game.builder()
                .id(0L)
                .name("League of Legends")
                .build());
        event = eventRepository.save(Event.builder()
                .id(0L)
                .name("Worlds")
                .region("Global")
                .season(1)
                .start(LocalDateTime.parse("2022-11-08T21:00:00"))
                .end(LocalDateTime.parse("2022-12-08T21:00:00"))
                .game(game)
                .build());
        team1 = teamRepository.save(Team.builder()
                .id(0L)
                .name("FNC")
                .game(game)
                .build());
        team2 = teamRepository.save(Team.builder()
                .id(0L)
                .name("G2")
                .game(game)
                .build());
    }

    @Test
    void shouldCreateMatch() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        String matchRequestJSON = objectMapper.writeValueAsString(matchRequest);

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.winner", is(matchRequest.getWinner().toString())))
                .andExpect(jsonPath("$.start", is(matchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(matchRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andDo(print());
        Assertions.assertEquals(1, matchRepository.findAll().size());
    }

    @Test
    void shouldGetAllMatches() throws Exception {
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
                .andExpect(jsonPath("$.winner", is(matchRequest.getWinner().toString())))
                .andExpect(jsonPath("$.start", is(matchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(matchRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingMatchById() throws Exception {
        mockMvc.perform(get("/matches/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateMatch() throws Exception {
        MatchRequest matchRequest = getMatchRequest();
        Match savedMatch = matchRepository.save(dtoUtility.matchRequestToMatch(matchRequest, 0L));
        MatchRequest updatedMatchRequest = getMatchRequest();
        updatedMatchRequest.setWinner(Winner.TEAM_1);
        updatedMatchRequest.setStart(LocalDateTime.parse("2022-11-25T16:00:00"));
        updatedMatchRequest.setEnd(LocalDateTime.parse("2022-11-25T17:00:00"));
        updatedMatchRequest.setTeam1Id(matchRequest.getTeam2Id());
        updatedMatchRequest.setTeam2Id(matchRequest.getTeam1Id());
        String updatedMatchRequestJSON = objectMapper.writeValueAsString(updatedMatchRequest);

        mockMvc.perform(put("/matches/{id}", savedMatch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMatchRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner", is(updatedMatchRequest.getWinner().toString())))
                .andExpect(jsonPath("$.start", is(updatedMatchRequest.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(updatedMatchRequest.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.eventId", is(matchRequest.getEventId().intValue())))
                .andExpect(jsonPath("$.team1.id", is(matchRequest.getTeam1Id().intValue())))
                .andExpect(jsonPath("$.team2.id", is(matchRequest.getTeam2Id().intValue())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingMatch() throws Exception {
        MatchRequest updatedMatchRequest = getMatchRequest();
        updatedMatchRequest.setWinner(Winner.TEAM_1);
        updatedMatchRequest.setStart(LocalDateTime.parse("2022-11-25T16:00:00"));
        updatedMatchRequest.setEnd(LocalDateTime.parse("2022-11-25T17:00:00"));
        updatedMatchRequest.setEventId(Long.MAX_VALUE);
        updatedMatchRequest.setTeam1Id(Long.MAX_VALUE);
        updatedMatchRequest.setTeam2Id(Long.MAX_VALUE);
        String updatedMatchRequestJSON = objectMapper.writeValueAsString(updatedMatchRequest);

        mockMvc.perform(put("/matches/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMatchRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingMatch() throws Exception {
        mockMvc.perform(delete("/matches/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private MatchRequest getMatchRequest() {
        return MatchRequest.builder()
                .winner(Winner.TBD)
                .start(LocalDateTime.parse("2022-11-08T21:00:00"))
                .end(LocalDateTime.parse("2022-11-08T22:00:00"))
                .eventId(event.getId())
                .team1Id(team1.getId())
                .team2Id(team2.getId())
                .build();
    }
}