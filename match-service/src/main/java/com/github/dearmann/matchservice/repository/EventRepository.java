package com.github.dearmann.matchservice.repository;

import com.github.dearmann.matchservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByGameId(Long gameId);
}
