package com.github.dearmann.betservice.controller;

import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.dto.response.BetResponse;
import com.github.dearmann.betservice.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BetResponse createBet(@RequestBody BetRequest betRequest) {
         return betService.createBet(betRequest);
    }

    @GetMapping
    public List<BetResponse> getAllBets() {
        return betService.getAllBets();
    }

    @GetMapping("/{id}")
    public BetResponse getBetById(@PathVariable Long id) {
        return betService.getBetById(id);
    }

    @PutMapping("/{id}")
    public BetResponse updateBet(@PathVariable Long id, @RequestBody BetRequest updatedBetRequest) {
        return betService.updateBet(id, updatedBetRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBet(@PathVariable Long id) {
        betService.deleteBet(id);
    }

}
