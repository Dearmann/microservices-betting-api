package com.github.dearmann.matchservice.dto.request;

import com.github.dearmann.matchservice.model.Winner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    private Winner winner;
    @NotNull(message = "Start time is mandatory")
    private LocalDateTime start;
    private LocalDateTime end;
    @NotNull(message = "Event ID is mandatory")
    private Long eventId;
    @NotNull(message = "Team ID is mandatory")
    private Long team1Id;
    @NotNull(message = "Team ID is mandatory")
    private Long team2Id;
}
