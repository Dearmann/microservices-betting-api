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

    @PutMapping("/{id}")
    public CommentResponse updateComment(@PathVariable Long id, @RequestBody CommentRequest updatedCommentRequest) {
        return commentService.updateComment(id, updatedCommentRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

}
