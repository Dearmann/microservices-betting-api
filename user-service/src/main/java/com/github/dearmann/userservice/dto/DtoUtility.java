package com.github.dearmann.userservice.dto;

import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.model.User;
import com.github.dearmann.userservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DtoUtility {
    private final KeycloakService keycloakService;

    public User userRequestToUser(UserRequest userRequest, Long jpaId) {
        Optional<UserRepresentation> keycloakUser = keycloakService.getOneUserByUsername(userRequest.getUsername());

        if (keycloakUser.isPresent()) {
            return User.builder()
                    .jpaId(jpaId)
                    .keycloakId(keycloakUser.get().getId())
                    .username(userRequest.getUsername())
                    .build();
        }
        return User.builder()
                .jpaId(jpaId)
                .username(userRequest.getUsername())
                .build();
    }

    public UserResponse userToUserResponse(User user) {
        UserRepresentation keycloakUser = keycloakService.getUserById(user.getKeycloakId());

        if (keycloakUser != null) {
            return UserResponse.builder()
                    .jpaId(user.getJpaId())
                    .keycloakId(keycloakUser.getId())
                    .username(keycloakUser.getUsername())
                    .email(keycloakUser.getEmail())
                    .firstname(keycloakUser.getFirstName())
                    .lastname(keycloakUser.getLastName())
                    .createdAtTimestamp(keycloakUser.getCreatedTimestamp())
                    .roles(keycloakUser.getRealmRoles())
                    .build();
        }
        return UserResponse.builder()
                .jpaId(user.getJpaId())
                .username(user.getUsername())
                .build();
    }
}
