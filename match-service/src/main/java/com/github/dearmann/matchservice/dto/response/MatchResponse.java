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
    private TeamResponse team1;
    private TeamResponse team2;
    private Boolean team1Won;
    private LocalDateTime start;
    private LocalDateTime estimatedEnd;
    private Boolean matchOver;
    private Long eventId;
    private Double averageRating;
    private Integer totalBets;
    private Integer team1Bets;
    private Integer team2Bets;
}
