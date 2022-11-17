package com.github.dearmann.commentservice.dto;

import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.dto.response.CommentResponse;
import com.github.dearmann.commentservice.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class DtoUtility {

    public Comment commentRequestToComment(CommentRequest commentRequest, Long id) {
        return Comment.builder()
                .id(id)
                .userId(commentRequest.getUserId())
                .matchId(commentRequest.getMatchId())
                .message(commentRequest.getMessage())
                .build();
    }

    public CommentResponse commentToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .username(comment.getUsername())
                .matchId(comment.getMatchId())
                .message(comment.getMessage())
                .createdDateTime(comment.getCreatedDateTime())
                .build();
    }
}
