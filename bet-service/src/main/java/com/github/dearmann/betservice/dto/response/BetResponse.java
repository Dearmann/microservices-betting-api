package com.github.dearmann.betservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetResponse {
    private Long id;
    private String userId;
    private Long matchId;
    private Long predictedTeamId;
    private Boolean correctPrediction;
    private Boolean matchFinished;
}
