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

    @GetMapping("/{id}")
    public UserRepresentation getUserById(@PathVariable("id") String id){
        return keyCloakService.getUserById(id);
    }

    @GetMapping("/by-username/{username}")
    public List<UserRepresentation> getUserByUsername(@PathVariable("username") String username){
        return keyCloakService.getUserByUsername(username);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRequest userRequest, @PathVariable("userId") String userId){
        keyCloakService.updateUser(userRequest, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId){
        keyCloakService.deleteUser(userId);
    }

    @GetMapping("/verification-link/{userId}")
    public void sendEmailVerificationLink(@PathVariable("userId") String userId){
        keyCloakService.sendEmailVerificationLink(userId);
    }

    @GetMapping("/reset-password/{userId}")
    public void sendResetPassword(@PathVariable("userId") String userId){
        keyCloakService.sendResetPassword(userId);
    }
}
