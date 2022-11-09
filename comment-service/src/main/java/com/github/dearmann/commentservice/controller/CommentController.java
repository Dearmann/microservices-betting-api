package com.github.dearmann.commentservice.controller;

import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.dto.response.CommentResponse;
import com.github.dearmann.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@RequestBody CommentRequest commentRequest) {
         return commentService.createComment(commentRequest);
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public CommentResponse getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping("/by-userid/{userId}")
    public List<CommentResponse> getAllCommentsByUserId(@PathVariable Long userId) {
        return commentService.getAllCommentsByUserId(userId);
    }

    @GetMapping("/by-matchid/{matchId}")
    public List<CommentResponse> getAllCommentsByMatchId(@PathVariable Long matchId) {
        return commentService.getAllCommentsByMatchId(matchId);
    }

    @PutMapping("/{id}")
    public CommentResponse updateComment(@RequestBody CommentRequest updatedCommentRequest, @PathVariable Long id) {
        return commentService.updateComment(updatedCommentRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @DeleteMapping("/by-userid/{userId}")
    public void deleteCommentsByUserId(@PathVariable Long userId) {
        commentService.deleteCommentsByUserId(userId);
    }

    @DeleteMapping("/by-matchid/{matchId}")
    public void deleteCommentsByMatchId(@PathVariable Long matchId) {
        commentService.deleteCommentsByMatchId(matchId);
    }

}
