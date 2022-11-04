package com.github.dearmann.matchservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String name;
    private String region;
    private int season;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long gameId;
}
