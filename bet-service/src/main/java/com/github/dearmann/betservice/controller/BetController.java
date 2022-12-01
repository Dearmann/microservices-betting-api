package com.github.dearmann.betservice.controller;

import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.dto.response.BetResponse;
import com.github.dearmann.betservice.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BetResponse createBet(@Valid @RequestBody BetRequest betRequest,
                                 @RequestHeader("user-id") String jwtUserId) {
         return betService.createBet(betRequest, jwtUserId);
    }

    @GetMapping
    public List<BetResponse> getAllBets() {
        return betService.getAllBets();
    }

    @GetMapping("/{id}")
    public BetResponse getBetById(@PathVariable Long id) {
        return betService.getBetById(id);
    }

    @GetMapping("/by-userid/{userId}")
    public List<BetResponse> getAllBetsByUserId(@PathVariable String userId) {
        return betService.getAllBetsByUserId(userId);
    }

    @GetMapping("/by-matchid/{matchId}")
    public List<BetResponse> getAllBetsByMatchId(@PathVariable Long matchId) {
        return betService.getAllBetsByMatchId(matchId);
    }

    @PutMapping("/{id}")
    public BetResponse updateBet(@Valid @RequestBody BetRequest updatedBetRequest,
                                 @PathVariable Long id,
                                 @RequestHeader("user-id") String jwtUserId) {
        return betService.updateBet(updatedBetRequest, id, jwtUserId);
    }

    @PutMapping("/result")
    public void setMatchResult(@RequestParam("matchId") Long matchId,
                               @RequestParam("winnerId") Long winnerId) {
        betService.setMatchResult(matchId, winnerId);
    }

    @DeleteMapping("/{id}")
    public void deleteBet(@PathVariable Long id,
                          @RequestHeader("user-id") String jwtUserId) {
        betService.deleteBet(id, jwtUserId);
    }

    @DeleteMapping("/by-userid/{userId}")
    public void deleteBetsByUserId(@PathVariable String userId) {
        betService.deleteBetsByUserId(userId);
    }

    @DeleteMapping("/by-matchid/{matchId}")
    public void deleteBetsByMatchId(@PathVariable Long matchId) {
        betService.deleteBetsByMatchId(matchId);
    }

}
