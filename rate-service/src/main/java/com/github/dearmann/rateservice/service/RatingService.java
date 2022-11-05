package com.github.dearmann.rateservice.service;

import com.github.dearmann.rateservice.dto.DtoUtility;
import com.github.dearmann.rateservice.dto.request.RatingRequest;
import com.github.dearmann.rateservice.dto.response.RatingResponse;
import com.github.dearmann.rateservice.exception.BadEntityIdException;
import com.github.dearmann.rateservice.model.Rating;
import com.github.dearmann.rateservice.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final DtoUtility dtoUtility;

    public RatingResponse createRating(RatingRequest ratingRequest) {
        Rating rating = dtoUtility.ratingRequestToRating(ratingRequest, 0L);
        ratingRepository.save(rating);

        return dtoUtility.ratingToRatingResponse(rating);
    }

    public List<RatingResponse> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(dtoUtility::ratingToRatingResponse)
                .toList();
    }

    public RatingResponse getRatingById(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);

        if (rating.isEmpty()) {
            throw new BadEntityIdException("Rating not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.ratingToRatingResponse(rating.get());
    }

    public Rating getRatingEntityById(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);

        if (rating.isEmpty()) {
            throw new BadEntityIdException("Rating not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return rating.get();
    }

    public RatingResponse updateRating(Long id, RatingRequest updatedRatingRequest) {
        Optional<Rating> ratingById = ratingRepository.findById(id);

        if (ratingById.isEmpty()) {
            throw new BadEntityIdException("Rating not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Rating updatedRating = dtoUtility.ratingRequestToRating(updatedRatingRequest, id);

        // Can't edit user or match of a rating
        updatedRating.setUserId(ratingById.get().getUserId());
        updatedRating.setMatchId(ratingById.get().getMatchId());
        ratingRepository.save(updatedRating);

        return dtoUtility.ratingToRatingResponse(updatedRating);
    }

    public void deleteRating(Long id) {
        Optional<Rating> ratingToDelete = ratingRepository.findById(id);

        if (ratingToDelete.isEmpty()) {
            throw new BadEntityIdException("Rating not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        ratingRepository.delete(ratingToDelete.get());
    }
}
