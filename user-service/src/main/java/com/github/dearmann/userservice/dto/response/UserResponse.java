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
    private Long jpaId;
    private String keycloakId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Long createdAtTimestamp;
    private List<String> roles;
    private List<BetResponse> bets;
    private List<CommentResponse> comments;
    private List<RatingResponse> ratings;
}
