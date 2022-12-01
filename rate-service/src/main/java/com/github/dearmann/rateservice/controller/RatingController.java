package com.github.dearmann.rateservice.controller;

import com.github.dearmann.rateservice.dto.request.RatingRequest;
import com.github.dearmann.rateservice.dto.response.RatingResponse;
import com.github.dearmann.rateservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(@Valid @RequestBody RatingRequest ratingRequest,
                                       @RequestHeader("user-id") String jwtUserId) {
         return ratingService.createRating(ratingRequest, jwtUserId);
    }

    @GetMapping
    public List<RatingResponse> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public RatingResponse getRatingById(@PathVariable Long id) {
        return ratingService.getRatingById(id);
    }

    @GetMapping("/by-userid/{userId}")
    public List<RatingResponse> getAllRatingsByUserId(@PathVariable String userId) {
        return ratingService.getAllRatingsByUserId(userId);
    }

    @GetMapping("/by-matchid/{matchId}")
    public List<RatingResponse> getAllRatingsByMatchId(@PathVariable Long matchId) {
        return ratingService.getAllRatingsByMatchId(matchId);
    }

    @PutMapping("/{id}")
    public RatingResponse updateRating(@Valid @RequestBody RatingRequest updatedRatingRequest,
                                       @PathVariable Long id,
                                       @RequestHeader("user-id") String jwtUserId) {
        return ratingService.updateRating(updatedRatingRequest, id, jwtUserId);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id,
                             @RequestHeader("user-id") String jwtUserId) {
        ratingService.deleteRating(id, jwtUserId);
    }

    @DeleteMapping("/by-userid/{userId}")
    public void deleteRatingsByUserId(@PathVariable String userId) {
        ratingService.deleteRatingsByUserId(userId);
    }

    @DeleteMapping("/by-matchid/{matchId}")
    public void deleteRatingsByMatchId(@PathVariable Long matchId) {
        ratingService.deleteRatingsByMatchId(matchId);
    }

}
