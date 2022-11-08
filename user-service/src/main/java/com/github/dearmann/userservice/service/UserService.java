package com.github.dearmann.userservice.service;

import com.github.dearmann.userservice.dto.DtoUtility;
import com.github.dearmann.userservice.dto.request.UserRequest;
import com.github.dearmann.userservice.dto.response.UserResponse;
import com.github.dearmann.userservice.exception.BadEntityIdException;
import com.github.dearmann.userservice.model.User;
import com.github.dearmann.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoUtility dtoUtility;

    public UserResponse createUser(UserRequest userRequest) {
        User user = dtoUtility.userRequestToUser(userRequest, 0L);
        user.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        user = userRepository.save(user);

        return dtoUtility.userToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(dtoUtility::userToUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.userToUserResponse(user.get());
    }

    public User getUserEntityById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return user.get();
    }

    public UserResponse updateUser(UserRequest updatedUserRequest, Long id) {
        Optional<User> userById = userRepository.findById(id);

        if (userById.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        User updatedUser = dtoUtility.userRequestToUser(updatedUserRequest, id);

        // Can't edit createdAt of a user
        updatedUser.setCreatedAt(userById.get().getCreatedAt());
        updatedUser = userRepository.save(updatedUser);

        return dtoUtility.userToUserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isEmpty()) {
            throw new BadEntityIdException("User not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.delete(userToDelete.get());
    }
}
