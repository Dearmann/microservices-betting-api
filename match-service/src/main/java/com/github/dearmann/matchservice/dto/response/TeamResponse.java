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
public class TeamResponse {
    private Long id;
    private String name;
    private Long gameId;
    private List<Long> matchesAsTeam1;
    private List<Long> matchesAsTeam2;
}
