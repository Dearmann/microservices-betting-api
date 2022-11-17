package com.github.dearmann.commentservice.service;

import com.github.dearmann.commentservice.dto.DtoUtility;
import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.dto.response.CommentResponse;
import com.github.dearmann.commentservice.exception.BadEntityIdException;
import com.github.dearmann.commentservice.model.Comment;
import com.github.dearmann.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final DtoUtility dtoUtility;

    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = dtoUtility.commentRequestToComment(commentRequest, 0L);
        comment.setCreatedDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        comment = commentRepository.save(comment);

        return dtoUtility.commentToCommentResponse(comment);
    }

    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(dtoUtility::commentToCommentResponse)
                .toList();
    }

    public CommentResponse getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.commentToCommentResponse(comment.get());
    }

    public Comment getCommentEntityById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return comment.get();
    }

    public List<CommentResponse> getAllCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId)
                .stream()
                .map(dtoUtility::commentToCommentResponse)
                .toList();
    }

    public List<CommentResponse> getAllCommentsByMatchId(Long matchId) {
        return commentRepository.findByMatchId(matchId)
                .stream()
                .map(dtoUtility::commentToCommentResponse)
                .toList();
    }

    public CommentResponse updateComment(CommentRequest updatedCommentRequest, Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);

        if (commentById.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Comment updatedComment = dtoUtility.commentRequestToComment(updatedCommentRequest, id);

        // Can't edit user or match of a comment
        updatedComment.setUserId(commentById.get().getUserId());
        updatedComment.setMatchId(commentById.get().getMatchId());
        updatedComment = commentRepository.save(updatedComment);

        return dtoUtility.commentToCommentResponse(updatedComment);
    }

    public void deleteComment(Long id) {
        Optional<Comment> commentToDelete = commentRepository.findById(id);

        if (commentToDelete.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        commentRepository.delete(commentToDelete.get());
    }

    public void deleteCommentsByUserId(String userId) {
        List<Comment> commentsByUserId = commentRepository.findByUserId(userId);
        commentRepository.deleteAll(commentsByUserId);
    }

    public void deleteCommentsByMatchId(Long matchId) {
        List<Comment> commentsByMatchId = commentRepository.findByMatchId(matchId);
        commentRepository.deleteAll(commentsByMatchId);
    }
}
