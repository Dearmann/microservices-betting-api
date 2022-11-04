package com.github.dearmann.matchservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long id;
    private TeamResponse teamOne;
    private TeamResponse teamTwo;
    private Boolean teamOneWon;
    private LocalDateTime start;
    private LocalDateTime estimatedEnd;
    private Boolean matchOver;
    private Long eventId;
}
