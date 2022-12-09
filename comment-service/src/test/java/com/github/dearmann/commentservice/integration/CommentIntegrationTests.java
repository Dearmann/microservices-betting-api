package com.github.dearmann.commentservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.commentservice.BaseTest;
import com.github.dearmann.commentservice.dto.DtoUtility;
import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.model.Comment;
import com.github.dearmann.commentservice.repository.CommentRepository;
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
    void shouldGetAllComments() throws Exception {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(dtoUtility.commentRequestToComment(getCommentRequest("userID"), 0L));
        commentList.add(dtoUtility.commentRequestToComment(getCommentRequest("userID"), 0L));
        commentRepository.saveAll(commentList);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(commentRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetCommentById() throws Exception {
        CommentRequest commentRequest = getCommentRequest("userID");
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));

        mockMvc.perform(get("/comments/{id}", savedComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(commentRequest.getUserId())))
                .andExpect(jsonPath("$.matchId", is(commentRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.message", is(commentRequest.getMessage())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingCommentById() throws Exception {
        mockMvc.perform(get("/comments/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest("userID");
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));
        CommentRequest updatedCommentRequest = getCommentRequest("userID-updated");
        updatedCommentRequest.setMatchId(Long.MAX_VALUE);
        updatedCommentRequest.setMessage("Updated test comment.");
        String updatedCommentRequestJSON = objectMapper.writeValueAsString(updatedCommentRequest);

        mockMvc.perform(put("/comments/{id}", savedComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCommentRequestJSON)
                        .header("user-id", updatedCommentRequest.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(commentRequest.getUserId())))
                .andExpect(jsonPath("$.matchId", is(commentRequest.getMatchId().intValue())))
                .andExpect(jsonPath("$.message", is(updatedCommentRequest.getMessage())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingComment() throws Exception {
        CommentRequest updatedCommentRequest = getCommentRequest("userID-updated");
        updatedCommentRequest.setMatchId(Long.MAX_VALUE);
        updatedCommentRequest.setMessage("Updated test comment.");
        String updatedCommentRequestJSON = objectMapper.writeValueAsString(updatedCommentRequest);

        mockMvc.perform(put("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCommentRequestJSON)
                        .header("user-id", updatedCommentRequest.getUserId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        CommentRequest commentRequest = getCommentRequest("userID");
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));

        mockMvc.perform(delete("/comments/{id}", savedComment.getId())
                        .header("user-id", commentRequest.getUserId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingComment() throws Exception {
        mockMvc.perform(delete("/comments/{id}", 1L)
                        .header("user-id", "userID"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldReturnBadRequestWhenMissingHeader() throws Exception {
        CommentRequest commentRequest = getCommentRequest("userID");
        Comment savedComment = commentRepository.save(dtoUtility.commentRequestToComment(commentRequest, 0L));
        String commentRequestJSON = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentRequestJSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/comments/{id}", savedComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentRequestJSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/comments/{id}", savedComment.getId()))
                .andExpect(status().isBadRequest());
    }

    private CommentRequest getCommentRequest(String userId) {
        return CommentRequest.builder()
                .userId(userId)
                .matchId(2L)
                .message("Test comment.")
                .build();
    }
}