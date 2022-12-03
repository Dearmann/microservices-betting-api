package com.github.dearmann.userservice.controller;

import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak")
public class KeycloakController {

    private final KeycloakService keyCloakService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createUser(@RequestBody UserRequest userRequest){
        return keyCloakService.createUser(userRequest);
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers() {
        return keyCloakService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserRepresentation getUserById(@PathVariable("id") String id){
        return keyCloakService.getUserById(id);
    }

    @GetMapping("/by-username/{username}")
    public List<UserRepresentation> getUserByUsername(@PathVariable("username") String username){
        return keyCloakService.getUserByUsername(username);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRequest userRequest,
                           @PathVariable("userId") String userId,
                           @RequestHeader("user-id") String jwtUserId){
        keyCloakService.updateUser(userRequest, userId, jwtUserId);
    }

    @PutMapping("/verification-link/{userId}")
    public void sendEmailVerificationLink(@PathVariable("userId") String userId){
        keyCloakService.sendEmailVerificationLink(userId);
    }

    @PutMapping("/reset-password/{userId}")
    public void sendResetPassword(@PathVariable("userId") String userId,
                                  @RequestHeader("user-id") String jwtUserId){
        keyCloakService.sendResetPassword(userId, jwtUserId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId,
                           @RequestHeader("user-id") String jwtUserId){
        keyCloakService.deleteUser(userId, jwtUserId);
    }

    @DeleteMapping("/admin/{userId}")
    public void privilegedDeleteUser(@PathVariable("userId") String userId){
        keyCloakService.privilegedDeleteUser(userId);
    }
}
