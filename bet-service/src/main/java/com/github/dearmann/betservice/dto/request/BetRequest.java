package com.github.dearmann.betservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {
    @NotBlank(message = "User ID is mandatory")
    private String userId;
    @NotNull(message = "Match ID is mandatory")
    private Long matchId;
    @NotNull(message = "Predicted team ID is mandatory")
    private Long predictedTeamId;
}
