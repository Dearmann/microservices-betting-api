package com.github.dearmann.userservice.service;

import com.github.dearmann.userservice.dto.DtoUtility;
import com.github.dearmann.userservice.dto.response.BetResponse;
import com.github.dearmann.userservice.dto.response.CommentResponse;
import com.github.dearmann.userservice.dto.response.RatingResponse;
import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.exception.BadEntityIdException;
import com.github.dearmann.userservice.model.User;
import com.github.dearmann.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DtoUtility dtoUtility;
    private final WebClient.Builder webClientBuilder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(dtoUtility::userToUserResponse)
                .toList();
    }

    public String getUsernameById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        return user.get().getUsername();
    }

    public UserResponse getUserInteractionsById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        UserResponse userResponse = dtoUtility.userToUserResponse(user.get());

        BetResponse[] betArray = webClientBuilder.build().get()
                .uri("http://bet-service/bets/by-userid/" + id)
                .retrieve()
                .bodyToMono(BetResponse[].class)
                .block();
        userResponse.setBets(Arrays.stream(betArray != null ? betArray : new BetResponse[0]).toList());

        CommentResponse[] commentArray = webClientBuilder.build().get()
                .uri("http://comment-service/comments/by-userid/" + id)
                .retrieve()
                .bodyToMono(CommentResponse[].class)
                .block();
        userResponse.setComments(Arrays.stream(commentArray != null ? commentArray : new CommentResponse[0]).toList());

        RatingResponse[] ratingArray = webClientBuilder.build().get()
                .uri("http://rate-service/ratings/by-userid/" + id)
                .retrieve()
                .bodyToMono(RatingResponse[].class)
                .block();
        userResponse.setRatings(Arrays.stream(ratingArray != null ? ratingArray : new RatingResponse[0]).toList());

        return userResponse;
    }

    public UserResponse getUserInteractionsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new BadEntityIdException("User not found Username - " + username, HttpStatus.NOT_FOUND);
        }

        UserResponse userResponse = dtoUtility.userToUserResponse(user.get());
        String id = user.get().getKeycloakId();

        BetResponse[] betArray = webClientBuilder.build().get()
                .uri("http://bet-service/bets/by-userid/" + id)
                .retrieve()
                .bodyToMono(BetResponse[].class)
                .block();
        userResponse.setBets(Arrays.stream(betArray != null ? betArray : new BetResponse[0]).toList());

        CommentResponse[] commentArray = webClientBuilder.build().get()
                .uri("http://comment-service/comments/by-userid/" + id)
                .retrieve()
                .bodyToMono(CommentResponse[].class)
                .block();
        userResponse.setComments(Arrays.stream(commentArray != null ? commentArray : new CommentResponse[0]).toList());

        RatingResponse[] ratingArray = webClientBuilder.build().get()
                .uri("http://rate-service/ratings/by-userid/" + id)
                .retrieve()
                .bodyToMono(RatingResponse[].class)
                .block();
        userResponse.setRatings(Arrays.stream(ratingArray != null ? ratingArray : new RatingResponse[0]).toList());

        return userResponse;
    }

    public void deleteUserInteractions(String id) {
        Optional<User> userToDeleteInteractions = userRepository.findById(id);

        if (userToDeleteInteractions.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        webClientBuilder.build().delete()
                .uri("http://bet-service/bets/by-userid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        webClientBuilder.build().delete()
                .uri("http://comment-service/comments/by-userid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        webClientBuilder.build().delete()
                .uri("http://rate-service/ratings/by-userid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
