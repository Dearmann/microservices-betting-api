package com.github.dearmann.rateservice.dto.request;

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
public class RatingRequest {
    @NotBlank(message = "User ID is mandatory")
    private String userId;
    @NotNull(message = "Match ID is mandatory")
    private Long matchId;
    @NotNull(message = "Rating is mandatory")
    private Integer rating;
}
