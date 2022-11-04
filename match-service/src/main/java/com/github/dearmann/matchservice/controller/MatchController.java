package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse createMatch(@RequestBody MatchRequest matchRequest) {
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

    @PutMapping("/{id}")
    public MatchResponse updateMatch(@PathVariable Long id, @RequestBody MatchRequest updatedMatchRequest) {
        return matchService.updateMatch(id, updatedMatchRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }

}
