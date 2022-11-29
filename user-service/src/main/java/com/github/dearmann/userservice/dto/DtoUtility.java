package com.github.dearmann.userservice.dto;

import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoUtility {

    public UserResponse userToUserResponse(User user) {
            return UserResponse.builder()
                    .id(user.getKeycloakId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .createdTimestamp(user.getCreatedAtTimestamp())
                    .build();
    }
}
