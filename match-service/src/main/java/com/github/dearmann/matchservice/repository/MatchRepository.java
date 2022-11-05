package com.github.dearmann.matchservice.repository;

import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByEvent(Event event);

}
