package com.github.dearmann.rateservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.rateservice.BaseTest;
import com.github.dearmann.rateservice.dto.DtoUtility;
import com.github.dearmann.rateservice.dto.request.RatingRequest;
import com.github.dearmann.rateservice.model.Rating;
import com.github.dearmann.rateservice.repository.RatingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RatingIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
    }

    @Test
    void shouldCreateRating() throws Exception {
        RatingRequest ratingRequest = getRatingRequest();
        String ratingRequestJSON = objectMapper.writeValueAsString(ratingRequest);

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ratingRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(ratingRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(ratingRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.rating", is(ratingRequest.getRating())))
                .andDo(print());
        Assertions.assertEquals(1, ratingRepository.findAll().size());
    }

    @Test
    void shouldGetAllRatings() throws Exception {
        List<Rating> ratingList = new ArrayList<>();
        ratingList.add(dtoUtility.ratingRequestToRating(getRatingRequest(), 0L));
        ratingList.add(dtoUtility.ratingRequestToRating(getRatingRequest(), 0L));
        ratingRepository.saveAll(ratingList);

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(ratingRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetRatingById() throws Exception {
        RatingRequest ratingRequest = getRatingRequest();
        Rating savedRating = ratingRepository.save(dtoUtility.ratingRequestToRating(ratingRequest, 0L));

        mockMvc.perform(get("/ratings/{id}", savedRating.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(ratingRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(ratingRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.rating", is(ratingRequest.getRating())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllRatings() throws Exception {
        mockMvc.perform(get("/ratings/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateRating() throws Exception {
        RatingRequest ratingRequest = getRatingRequest();
        Rating savedRating = ratingRepository.save(dtoUtility.ratingRequestToRating(ratingRequest, 0L));
        RatingRequest updatedRatingRequest = getRatingRequest();
        updatedRatingRequest.setUserId(11L);
        updatedRatingRequest.setMatchId(12L);
        updatedRatingRequest.setRating(1);
        String updatedRatingRequestJSON = objectMapper.writeValueAsString(updatedRatingRequest);

        mockMvc.perform(put("/ratings/{id}", savedRating.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRatingRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(ratingRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(ratingRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.rating", is(updatedRatingRequest.getRating())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingRating() throws Exception {
        RatingRequest updatedRatingRequest = getRatingRequest();
        updatedRatingRequest.setUserId(11L);
        updatedRatingRequest.setMatchId(12L);
        updatedRatingRequest.setRating(1);
        String updatedRatingRequestJSON = objectMapper.writeValueAsString(updatedRatingRequest);

        mockMvc.perform(put("/ratings/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRatingRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteRating() throws Exception {
        RatingRequest ratingRequest = getRatingRequest();
        Rating savedRating = ratingRepository.save(dtoUtility.ratingRequestToRating(ratingRequest, 0L));

        mockMvc.perform(delete("/ratings/{id}", savedRating.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingRating() throws Exception {
        mockMvc.perform(delete("/ratings/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private RatingRequest getRatingRequest() {
        return RatingRequest.builder()
                .userId(1L)
                .matchId(2L)
                .rating(5)
                .build();
    }
}