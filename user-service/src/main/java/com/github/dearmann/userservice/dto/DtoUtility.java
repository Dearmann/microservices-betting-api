package com.github.dearmann.userservice.dto;

import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.model.User;
import com.github.dearmann.userservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DtoUtility {
    private final KeycloakService keycloakService;

    public User userRequestToUser(UserRequest userRequest, Long jpaId) {
        return User.builder()
                .id(jpaId)
                .username(userRequest.getUsername())
                .build();
    }

    public UserResponse userToUserResponse(User user) {
        List<UserRepresentation> keycloakUser = keycloakService.getUserByUsername(user.getUsername());

        if (keycloakUser.size() == 1) {
            return UserResponse.builder()
                    .jpaId(user.getId())
                    .keycloakId(keycloakUser.get(0).getId())
                    .username(keycloakUser.get(0).getUsername())
                    .email(keycloakUser.get(0).getEmail())
                    .firstname(keycloakUser.get(0).getFirstName())
                    .lastname(keycloakUser.get(0).getLastName())
                    .roles(keycloakUser.get(0).getRealmRoles())
                    .createdAtTimestamp(keycloakUser.get(0).getCreatedTimestamp())
                    .build();
        }
        return UserResponse.builder()
                .jpaId(user.getId())
                .username(user.getUsername())
                .build();
    }
}
