package com.github.dearmann.matchservice.dto.request;

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
public class TeamRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String logoUrl;
    @NotNull(message = "Game ID is mandatory")
    private Long gameId;
}
