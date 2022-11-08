package com.github.dearmann.matchservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.matchservice.BaseTest;
import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.model.Team;
import com.github.dearmann.matchservice.repository.GameRepository;
import com.github.dearmann.matchservice.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TeamIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @Test
    void shouldCreateTeam() throws Exception {
        TeamRequest teamRequest = getTeamRequest();
        String teamRequestJSON = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(teamRequest.getName())))
                .andExpect(jsonPath("$.gameId", is(teamRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.matchesAsTeam1", is(Collections.emptyList())))
                .andExpect(jsonPath("$.matchesAsTeam2", is(Collections.emptyList())))
                .andDo(print());
        Assertions.assertEquals(1, teamRepository.findAll().size());
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        List<Team> teamList = new ArrayList<>();
        teamList.add(dtoUtility.teamRequestToTeam(getTeamRequest(), 0L));
        teamList.add(dtoUtility.teamRequestToTeam(getTeamRequest(), 0L));
        teamRepository.saveAll(teamList);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(teamRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetTeamById() throws Exception {
        TeamRequest teamRequest = getTeamRequest();
        Team savedTeam = teamRepository.save(dtoUtility.teamRequestToTeam(teamRequest, 0L));

        mockMvc.perform(get("/teams/{id}", savedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(teamRequest.getName())))
                .andExpect(jsonPath("$.gameId", is(teamRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.matchesAsTeam1", is(Collections.emptyList())))
                .andExpect(jsonPath("$.matchesAsTeam2", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllTeams() throws Exception {
        mockMvc.perform(get("/teams/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateTeam() throws Exception {
        TeamRequest teamRequest = getTeamRequest();
        Team savedTeam = teamRepository.save(dtoUtility.teamRequestToTeam(teamRequest, 0L));
        TeamRequest updatedTeamRequest = getTeamRequest();
        updatedTeamRequest.setName("Updated test team");
//        updatedTeamRequest.setGameId(11L);
        String updatedTeamRequestJSON = objectMapper.writeValueAsString(updatedTeamRequest);

        mockMvc.perform(put("/teams/{id}", savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTeamRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedTeamRequest.getName())))
                .andExpect(jsonPath("$.gameId", is(teamRequest.getGameId().intValue())))
                .andExpect(jsonPath("$.matchesAsTeam1", is(Collections.emptyList())))
                .andExpect(jsonPath("$.matchesAsTeam2", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingTeam() throws Exception {
        TeamRequest updatedTeamRequest = getTeamRequest();
        updatedTeamRequest.setName("Updated test team");
        updatedTeamRequest.setGameId(11L);
        String updatedTeamRequestJSON = objectMapper.writeValueAsString(updatedTeamRequest);

        mockMvc.perform(put("/teams/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTeamRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteTeam() throws Exception {
        TeamRequest teamRequest = getTeamRequest();
        Team savedTeam = teamRepository.save(dtoUtility.teamRequestToTeam(teamRequest, 0L));

        mockMvc.perform(delete("/teams/{id}", savedTeam.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingTeam() throws Exception {
        mockMvc.perform(delete("/teams/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private TeamRequest getTeamRequest() {
        Game game = Game.builder()
                .id(0L)
                .name("League of Legends")
                .build();
        game = gameRepository.save(game);

        return TeamRequest.builder()
                .name("Test team")
                .gameId(game.getId())
                .build();
    }
}