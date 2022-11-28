package com.github.dearmann.rateservice.dto;

import com.github.dearmann.rateservice.dto.request.RatingRequest;
import com.github.dearmann.rateservice.dto.response.RatingResponse;
import com.github.dearmann.rateservice.model.Rating;
import org.springframework.stereotype.Component;

@Component
public class DtoUtility {

    public Rating ratingRequestToRating(RatingRequest ratingRequest, Long id) {
        return Rating.builder()
                .id(id)
                .userId(ratingRequest.getUserId())
                .matchId(ratingRequest.getMatchId())
                .rating(validateRatingRange(
                        ratingRequest.getRating()
                ))
                .build();
    }

    public RatingResponse ratingToRatingResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .userId(rating.getUserId())
                .matchId(rating.getMatchId())
                .rating(rating.getRating())
                .build();
    }

    private Integer validateRatingRange(Integer rating) {
        if (rating == null) {
            return 1;
        }
        else if (rating > 5) {
            return 5;
        }
        else if (rating < 1) {
            return 1;
        }
        else {
            return rating;
        }
    }
}
