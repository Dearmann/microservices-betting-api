package com.github.dearmann.userservice.dto;

import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class DtoUtility {

    public User userRequestToUser(UserRequest userRequest, Long id) {
        return User.builder()
                .id(id)
                .role(userRequest.getRole())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .build();
    }

    public UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .role(user.getRole())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                // TODO: fetch (in service layer) bets, comments and ratings from other microservices.
                //.bets()
                //.comments()
                //.ratings()
                .build();
    }
}
