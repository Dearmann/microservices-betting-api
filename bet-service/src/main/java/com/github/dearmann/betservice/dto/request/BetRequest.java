package com.github.dearmann.betservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {
    private Long userId;
    private Long matchId;
    private Long predictedTeamId;
}
