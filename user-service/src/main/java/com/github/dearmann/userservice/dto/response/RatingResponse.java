package com.github.dearmann.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private Long id;
    private String userId;
    private Long matchId;
    private Integer rating;
}
