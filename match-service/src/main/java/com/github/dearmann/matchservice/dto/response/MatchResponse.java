package com.github.dearmann.matchservice.dto.response;

import com.github.dearmann.matchservice.model.Winner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long id;
    private Winner winner;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long eventId;
    private TeamResponse team1;
    private TeamResponse team2;
    private List<BetResponse> bets;
    private List<CommentResponse> comments;
    private List<RatingResponse> ratings;
}
