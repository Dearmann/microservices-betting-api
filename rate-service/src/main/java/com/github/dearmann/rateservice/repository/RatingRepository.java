package com.github.dearmann.rateservice.repository;

import com.github.dearmann.rateservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserId(String userId);
    List<Rating> findByMatchId(Long matchId);
}
