package com.github.dearmann.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dearmann.userservice.BaseTest;
import com.github.dearmann.userservice.dto.DtoUtility;
import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.model.User;
import com.github.dearmann.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserIntegrationTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DtoUtility dtoUtility;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserRequest userRequest = getUserRequest();
        String userRequestJSON = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(userRequest.getUsername())))
                .andExpect(jsonPath("$.password", is(userRequest.getPassword())))
                .andExpect(jsonPath("$.email", is(userRequest.getEmail())))
                .andDo(print());
        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        UserRequest userRequest1 = getUserRequest();
        UserRequest userRequest2 = getUserRequest();
        userRequest2.setUsername("test username 2");
        userList.add(dtoUtility.userRequestToUser(userRequest1, 0L));
        userList.add(dtoUtility.userRequestToUser(userRequest2, 0L));
        userRepository.saveAll(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userRepository.findAll().size())))
                .andDo(print());
    }

    @Test
    void shouldGetUserById() throws Exception {
        UserRequest userRequest = getUserRequest();
        User savedUser = userRepository.save(dtoUtility.userRequestToUser(userRequest, 0L));

        mockMvc.perform(get("/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(userRequest.getUsername())))
                .andExpect(jsonPath("$.password", is(userRequest.getPassword())))
                .andExpect(jsonPath("$.email", is(userRequest.getEmail())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenGettingAllUsers() throws Exception {
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserRequest userRequest = getUserRequest();
        User savedUser = userRepository.save(dtoUtility.userRequestToUser(userRequest, 0L));
        UserRequest updatedUserRequest = getUserRequest();
        updatedUserRequest.setUsername("edited test username");
        updatedUserRequest.setPassword("edited test password");
        updatedUserRequest.setEmail("edited.test@email.com");
        String updatedUserRequestJSON = objectMapper.writeValueAsString(updatedUserRequest);

        mockMvc.perform(put("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserRequestJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(userRequest.getUsername())))
                .andExpect(jsonPath("$.password", is(updatedUserRequest.getPassword())))
                .andExpect(jsonPath("$.email", is(updatedUserRequest.getEmail())))
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUpdatingUser() throws Exception {
        UserRequest updatedUserRequest = getUserRequest();
        updatedUserRequest.setUsername("edited test username");
        updatedUserRequest.setPassword("edited test password");
        updatedUserRequest.setEmail("edited.test@email.com");
        String updatedUserRequestJSON = objectMapper.writeValueAsString(updatedUserRequest);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserRequestJSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UserRequest userRequest = getUserRequest();
        User savedUser = userRepository.save(dtoUtility.userRequestToUser(userRequest, 0L));

        mockMvc.perform(delete("/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeletingUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private UserRequest getUserRequest() {
        return UserRequest.builder()
                .username("test username")
                .password("test password")
                .email("test@email.com")
                .firstname("test firstname")
                .lastname("test lastname")
                .build();
    }
}