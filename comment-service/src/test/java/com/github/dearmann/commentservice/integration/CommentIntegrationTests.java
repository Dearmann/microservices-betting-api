package com.github.dearmann.commentservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.commentservice.BaseTest;
import com.github.dearmann.commentservice.dto.DtoUtility;
import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.model.Comment;
import com.github.dearmann.commentservice.repository.CommentRepository;
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
class CommentIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
    }

    @Test
    void shouldCreateComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest();
        String commentRequestJSON = objectMapper.writeValueAsString(commentRequest);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(commentRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(commentRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.message", is(commentRequest.getMessage())))
                .andDo(print());
        Assertions.assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    void shouldGetAllComments() throws Exception {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(dtoUtility.commentRequestToComment(getCommentRequest(), 0L));
        commentList.add(dtoUtility.commentRequestToComment(getCommentRequest(), 0L));
        commentRepository.saveAll(commentList);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(commentRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetCommentById() throws Exception {
        CommentRequest commentRequest = getCommentRequest();
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));

        mockMvc.perform(get("/comments/{id}", savedComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(commentRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(commentRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.message", is(commentRequest.getMessage())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllComments() throws Exception {
        mockMvc.perform(get("/comments/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest();
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));
        CommentRequest updatedCommentRequest = getCommentRequest();
        updatedCommentRequest.setUserId(11L);
        updatedCommentRequest.setMatchId(12L);
        updatedCommentRequest.setMessage("Updated test comment.");
        String updatedCommentRequestJSON = objectMapper.writeValueAsString(updatedCommentRequest);

        mockMvc.perform(put("/comments/{id}", savedComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCommentRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(commentRequest.getUserId().intValue())))
                .andExpect(jsonPath("$.matchId", is(commentRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.message", is(updatedCommentRequest.getMessage())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingComment() throws Exception {
        CommentRequest updatedCommentRequest = getCommentRequest();
        updatedCommentRequest.setUserId(11L);
        updatedCommentRequest.setMatchId(12L);
        updatedCommentRequest.setMessage("Updated test comment.");
        String updatedCommentRequestJSON = objectMapper.writeValueAsString(updatedCommentRequest);

        mockMvc.perform(put("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCommentRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest();
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));

        mockMvc.perform(delete("/comments/{id}", savedComment.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingComment() throws Exception {
        mockMvc.perform(delete("/comments/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private CommentRequest getCommentRequest() {
        return CommentRequest.builder()
                .userId(1L)
                .matchId(2L)
                .message("Test comment.")
                .build();
    }
}