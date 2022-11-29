package com.github.dearmann.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String email;
    private String firstName;
    private String lastName;
    private Long createdTimestamp;
    private List<BetResponse> bets;
    private List<CommentResponse> comments;
    private List<RatingResponse> ratings;
}
