package com.github.dearmann.matchservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    private Long teamOneId;
    private Long teamTwoId;
    private Boolean teamOneWon;
    private LocalDateTime start;
    private LocalDateTime estimatedEnd;
    private Boolean matchOver;
    private Long eventId;
}
