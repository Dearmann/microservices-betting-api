package com.github.dearmann.betservice.dto;

import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.dto.response.BetResponse;
import com.github.dearmann.betservice.model.Bet;
import org.springframework.stereotype.Component;

@Component
public class DtoUtility {

    public Bet betRequestToBet(BetRequest betRequest, Long id) {
        return Bet.builder()
                .id(id)
                .userId(betRequest.getUserId())
                .matchId(betRequest.getMatchId())
                .predictedTeamId(betRequest.getPredictedTeamId())
                .correctPrediction(false)
                .matchFinished(false)
                .build();
    }

    public BetResponse betToBetResponse(Bet bet) {
        return BetResponse.builder()
                .id(bet.getId())
                .userId(bet.getUserId())
                .matchId(bet.getMatchId())
                .predictedTeamId(bet.getPredictedTeamId())
                .correctPrediction(bet.getCorrectPrediction())
                .matchFinished(bet.getMatchFinished())
                .build();
    }
}
