package com.github.dearmann.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String userId;
    private String username;
    private Long matchId;
    private String message;
    private LocalDateTime createdDateTime;
}
