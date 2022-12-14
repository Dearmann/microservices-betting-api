package com.github.dearmann.commentservice.service;

import com.github.dearmann.commentservice.dto.DtoUtility;
import com.github.dearmann.commentservice.dto.request.CommentRequest;
import com.github.dearmann.commentservice.dto.response.CommentResponse;
import com.github.dearmann.commentservice.exception.BadEntityIdException;
import com.github.dearmann.commentservice.exception.CommentException;
import com.github.dearmann.commentservice.model.Comment;
import com.github.dearmann.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final DtoUtility dtoUtility;
    private final WebClient.Builder webClientBuilder;
    private final Integer maxMessageLength = 500;

    public CommentResponse createComment(CommentRequest commentRequest, String jwtUserId) {
        validateJWTSubject(commentRequest.getUserId(), jwtUserId);

        if (commentRequest.getMessage().length() > 500) {
            throw new CommentException("Message exceeding " + maxMessageLength + " characters", HttpStatus.BAD_REQUEST);
        }

        Comment comment = dtoUtility.commentRequestToComment(commentRequest, 0L);
        comment.setCreatedDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        if (commentRequest.getUserId() == null) {
            throw new BadEntityIdException("User ID is null", HttpStatus.BAD_REQUEST);
        }

        String username = webClientBuilder.build().get()
                .uri("http://user-service/users/username/" + commentRequest.getUserId())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        comment.setUsername(username);

        comment.setMessage(comment.getMessage().trim());
        if (comment.getMessage().isEmpty()) {
            comment.setMessage(null);
        }

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

    public CommentResponse updateComment(CommentRequest updatedCommentRequest, Long id, String jwtUserId) {
        validateJWTSubject(updatedCommentRequest.getUserId(), jwtUserId);

        Optional<Comment> commentById = commentRepository.findById(id);

        if (commentById.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        if (updatedCommentRequest.getMessage().length() > 500) {
            throw new CommentException("Message exceeding " + maxMessageLength + " characters", HttpStatus.BAD_REQUEST);
        }

        Comment updatedComment = dtoUtility.commentRequestToComment(updatedCommentRequest, id);
        updatedComment.setUsername(commentById.get().getUsername());
        updatedComment.setCreatedDateTime(commentById.get().getCreatedDateTime());

        // Can't edit user or match of a comment
        updatedComment.setUserId(commentById.get().getUserId());
        updatedComment.setMatchId(commentById.get().getMatchId());
        updatedComment = commentRepository.save(updatedComment);

        return dtoUtility.commentToCommentResponse(updatedComment);
    }

    public void deleteComment(Long id, String jwtUserId) {
        Optional<Comment> commentToDelete = commentRepository.findById(id);

        if (commentToDelete.isEmpty()) {
            throw new BadEntityIdException("Comment not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        validateJWTSubject(commentToDelete.get().getUserId(), jwtUserId);

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

    private void validateJWTSubject(String requestUserId, String jwtUserId) {
        if (!Objects.equals(requestUserId, jwtUserId)) {
            throw new CommentException("JWT subject is different from request user ID", HttpStatus.CONFLICT);
        }
    }
}
