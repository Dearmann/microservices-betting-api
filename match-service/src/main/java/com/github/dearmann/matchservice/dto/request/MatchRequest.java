package com.github.dearmann.matchservice.dto.request;

import com.github.dearmann.matchservice.model.Winner;
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
    private Winner winner;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long eventId;
    private Long team1Id;
    private Long team2Id;
}
