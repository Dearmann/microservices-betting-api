package com.github.dearmann.matchservice.dto.response;

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
public class EventResponse {
    private Long id;
    private String name;
    private String region;
    private Integer season;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long gameId;
    private List<Long> teamIds;
}
