package com.github.dearmann.userservice.controller;

import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/username/{id}")
    public String getUsernameById(@PathVariable String id) {
        return userService.getUsernameById(id);
    }

    @GetMapping("/{id}")
    public UserResponse getUserInteractionsById(@PathVariable String id) {
        return userService.getUserInteractionsById(id);
    }

    @GetMapping("/by-username/{username}")
    public UserResponse getUserInteractionsByUsername(@PathVariable String username) {
        return userService.getUserInteractionsByUsername(username);
    }

}
