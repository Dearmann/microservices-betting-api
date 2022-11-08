package com.github.dearmann.betservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.betservice.BaseTest;
import com.github.dearmann.betservice.dto.DtoUtility;
import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.model.Bet;
import com.github.dearmann.betservice.repository.BetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BetIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BetRepository betRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        betRepository.deleteAll();
    }

    @Test
    void shouldCreateBet() throws Exception {
        BetRequest betRequest = getBetRequest();
        String betRequestJSON = objectMapper.writeValueAsString(betRequest);

        mockMvc.perform(post("/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(betRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(betRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(betRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.predictedTeamId", is(betRequest.getPredictedTeamId().intValue())))
                .andExpect(jsonPath("$.correctPrediction", is(false)))
                .andExpect(jsonPath("$.matchFinished", is(false)))
                .andDo(print());
        Assertions.assertEquals(1, betRepository.findAll().size());
    }

    @Test
    void shouldGetAllBets() throws Exception {
        List<Bet> betList = new ArrayList<>();
        betList.add(dtoUtility.betRequestToBet(getBetRequest(), 0L));
        betList.add(dtoUtility.betRequestToBet(getBetRequest(), 0L));
        betRepository.saveAll(betList);

        mockMvc.perform(get("/bets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(betRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetBetById() throws Exception {
        BetRequest betRequest = getBetRequest();
        Bet savedBet = betRepository.save(dtoUtility.betRequestToBet(betRequest, 0L));

        mockMvc.perform(get("/bets/{id}", savedBet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(betRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(betRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.predictedTeamId", is(betRequest.getPredictedTeamId().intValue())))
                .andExpect(jsonPath("$.correctPrediction", is(false)))
                .andExpect(jsonPath("$.matchFinished", is(false)))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllBets() throws Exception {
        mockMvc.perform(get("/bets/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateBet() throws Exception {
        BetRequest betRequest = getBetRequest();
        Bet savedBet = betRepository.save(dtoUtility.betRequestToBet(betRequest, 0L));
        BetRequest updatedBetRequest = getBetRequest();
        updatedBetRequest.setUserId(11L);
        updatedBetRequest.setMatchId(12L);
        updatedBetRequest.setPredictedTeamId(13L);
        String updatedBetRequestJSON = objectMapper.writeValueAsString(updatedBetRequest);

        mockMvc.perform(put("/bets/{id}", savedBet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBetRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(betRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(betRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.predictedTeamId", is(updatedBetRequest.getPredictedTeamId().intValue())))
                .andExpect(jsonPath("$.correctPrediction", is(false)))
                .andExpect(jsonPath("$.matchFinished", is(false)))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingBet() throws Exception {
        BetRequest updatedBetRequest = getBetRequest();
        updatedBetRequest.setUserId(11L);
        updatedBetRequest.setMatchId(12L);
        updatedBetRequest.setPredictedTeamId(13L);
        String updatedBetRequestJSON = objectMapper.writeValueAsString(updatedBetRequest);

        mockMvc.perform(put("/bets/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBetRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteBet() throws Exception {
        BetRequest betRequest = getBetRequest();
        Bet savedBet = betRepository.save(dtoUtility.betRequestToBet(betRequest, 0L));

        mockMvc.perform(delete("/bets/{id}", savedBet.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingBet() throws Exception {
        mockMvc.perform(delete("/bets/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private BetRequest getBetRequest() {
        return BetRequest.builder()
                .userId(1L)
                .matchId(2L)
                .predictedTeamId(3L)
                .build();
    }
}