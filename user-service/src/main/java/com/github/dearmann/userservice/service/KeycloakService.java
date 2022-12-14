package com.github.dearmann.userservice.service;

import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KeycloakService {

    private final RealmResource realmResource;
    private final UserService userService;

    public Integer createUser(UserRequest userRequest){
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setCredentials(Collections.singletonList(createCredentials(userRequest.getPassword())));
        user.setEnabled(true);

        Response response = realmResource.users().create(user);
        return response.getStatus();
    }

    public List<UserRepresentation> getAllUsers(){
        return realmResource.users().list();
    }

    public List<UserRepresentation> getUserByUsername(String username){
        return realmResource.users().search(username, true);
    }

    public Optional<UserRepresentation> getOneUserByUsername(String username){
        List<UserRepresentation> userByUsername = realmResource.users().search(username, true);
        if (userByUsername.size() == 1) {
            return Optional.ofNullable(userByUsername.get(0));
        }
        return Optional.empty();
    }

    public UserRepresentation getUserById(String userId){
        return realmResource.users().get(userId).toRepresentation();
    }

    public void updateUser(UserRequest userRequest, String userId, String jwtUserId){
        validateJWTSubject(userId, jwtUserId);

        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());

        realmResource.users().get(userId).update(user);
    }

    public void deleteUser(String userId, String jwtUserId){
        validateJWTSubject(userId, jwtUserId);
        userService.deleteUserInteractions(userId);
        realmResource.users().get(userId).remove();
    }

    public void privilegedDeleteUser(String userId) {
        userService.deleteUserInteractions(userId);
        realmResource.users().get(userId).remove();
    }

    public void sendEmailVerificationLink(String userId){
        realmResource.users().get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String userId, String jwtUserId){
        validateJWTSubject(userId, jwtUserId);
        realmResource.users().get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    private CredentialRepresentation createCredentials(String password) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        return credentials;
    }

    private void validateJWTSubject(String requestUserId, String jwtUserId) {
        if (!Objects.equals(requestUserId, jwtUserId)) {
            throw new UserException("JWT subject is different from request user ID", HttpStatus.CONFLICT);
        }
    }
}