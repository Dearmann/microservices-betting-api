package com.github.dearmann.matchservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchUserInteractionsResponse {
    private Long userId;
    private MatchResponse match;
    private BetResponse bet;
    private RatingResponse rating;
    private List<CommentResponse> comments;
}
