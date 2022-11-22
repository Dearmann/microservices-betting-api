package com.github.dearmann.matchservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String region;
    private Integer season;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotNull(message = "Game ID is mandatory")
    private Long gameId;
}
