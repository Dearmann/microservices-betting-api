package com.github.dearmann.rateservice.controller;

import com.github.dearmann.rateservice.dto.request.RatingRequest;
import com.github.dearmann.rateservice.dto.response.RatingResponse;
import com.github.dearmann.rateservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(@RequestBody RatingRequest ratingRequest) {
         return ratingService.createRating(ratingRequest);
    }

    @GetMapping
    public List<RatingResponse> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public RatingResponse getRatingById(@PathVariable Long id) {
        return ratingService.getRatingById(id);
    }

    @PutMapping("/{id}")
    public RatingResponse updateRating(@RequestBody RatingRequest updatedRatingRequest, @PathVariable Long id) {
        return ratingService.updateRating(updatedRatingRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

}
