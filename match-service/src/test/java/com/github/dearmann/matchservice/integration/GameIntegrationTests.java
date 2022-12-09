package com.github.dearmann.matchservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.matchservice.BaseTest;
import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.repository.GameRepository;
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
class GameIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
    }

    @Test
    void shouldCreateGame() throws Exception {
        GameRequest gameRequest = getGameRequest("Game");
        String gameRequestJSON = objectMapper.writeValueAsString(gameRequest);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(gameRequest.getName())))
                .andExpect(jsonPath("$.logoUrl", is(gameRequest.getLogoUrl())))
                .andExpect(jsonPath("$.eventIds", is(Collections.emptyList())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
        Assertions.assertEquals(1, gameRepository.findAll().size());
    }

    @Test
    void shouldGetAllGames() throws Exception {
        List<Game> gameList = new ArrayList<>();
        gameList.add(dtoUtility.gameRequestToGame(getGameRequest("Game-1"), 0L));
        gameList.add(dtoUtility.gameRequestToGame(getGameRequest("Game-2"), 0L));
        gameRepository.saveAll(gameList);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(gameRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetGameById() throws Exception {
        GameRequest gameRequest = getGameRequest("Game");
        Game savedGame = gameRepository.save(dtoUtility.gameRequestToGame(gameRequest, 0L));

        mockMvc.perform(get("/games/{id}", savedGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(gameRequest.getName())))
                .andExpect(jsonPath("$.logoUrl", is(gameRequest.getLogoUrl())))
                .andExpect(jsonPath("$.eventIds", is(Collections.emptyList())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingGameById() throws Exception {
        mockMvc.perform(get("/games/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateGame() throws Exception {
        GameRequest gameRequest = getGameRequest("Game");
        Game savedGame = gameRepository.save(dtoUtility.gameRequestToGame(gameRequest, 0L));
        GameRequest updatedGameRequest = getGameRequest("Game-updated");
        String updatedGameRequestJSON = objectMapper.writeValueAsString(updatedGameRequest);

        mockMvc.perform(put("/games/{id}", savedGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedGameRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedGameRequest.getName())))
                .andExpect(jsonPath("$.logoUrl", is(updatedGameRequest.getLogoUrl())))
                .andExpect(jsonPath("$.eventIds", is(Collections.emptyList())))
                .andExpect(jsonPath("$.teamIds", is(Collections.emptyList())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingGame() throws Exception {
        GameRequest updatedGameRequest = getGameRequest("Game-updated");
        String updatedGameRequestJSON = objectMapper.writeValueAsString(updatedGameRequest);

        mockMvc.perform(put("/games/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedGameRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteGame() throws Exception {
        GameRequest gameRequest = getGameRequest("Game");
        Game savedGame = gameRepository.save(dtoUtility.gameRequestToGame(gameRequest, 0L));

        mockMvc.perform(delete("/games/{id}", savedGame.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingGame() throws Exception {
        mockMvc.perform(delete("/games/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private GameRequest getGameRequest(String gameName) {
        return GameRequest.builder()
                .name(gameName)
                .build();
    }
}