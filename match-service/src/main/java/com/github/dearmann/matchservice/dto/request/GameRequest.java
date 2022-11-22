package com.github.dearmann.matchservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String logoUrl;
}
