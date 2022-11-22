package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse createMatch(@Valid @RequestBody MatchRequest matchRequest) {
        return matchService.createMatch(matchRequest);
    }

    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public MatchResponse getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    @GetMapping("/by-event/{eventId}")
    public List<MatchResponse> getAllMatchesFromEvent(@PathVariable Long eventId) {
        return matchService.getAllMatchesFromEventId(eventId);
    }

    @PutMapping("/{id}")
    public MatchResponse updateMatch(@Valid @RequestBody MatchRequest updatedMatchRequest, @PathVariable Long id) {
        return matchService.updateMatch(updatedMatchRequest, id);
    }

    @PutMapping("/result")
    public MatchResponse setMatchResult(@RequestParam("matchId") Long matchId,
                                        @RequestParam("winnerId") Long winnerId) {
        return matchService.setMatchResult(matchId, winnerId);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }

}
