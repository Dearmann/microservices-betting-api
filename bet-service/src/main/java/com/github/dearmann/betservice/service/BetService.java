package com.github.dearmann.betservice.service;

import com.github.dearmann.betservice.dto.DtoUtility;
import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.dto.response.BetResponse;
import com.github.dearmann.betservice.dto.response.MatchResponse;
import com.github.dearmann.betservice.dto.response.Winner;
import com.github.dearmann.betservice.exception.BadEntityIdException;
import com.github.dearmann.betservice.exception.BetException;
import com.github.dearmann.betservice.model.Bet;
import com.github.dearmann.betservice.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BetService {

    private final BetRepository betRepository;
    private final DtoUtility dtoUtility;
    private final WebClient.Builder webClientBuilder;

    public BetResponse createBet(BetRequest betRequest, String jwtUserId) {
        validateJWTSubject(betRequest.getUserId(), jwtUserId);
        validateMatch(betRequest);

        Bet bet = dtoUtility.betRequestToBet(betRequest, 0L);
        bet = betRepository.save(bet);

        return dtoUtility.betToBetResponse(bet);
    }

    public List<BetResponse> getAllBets() {
        return betRepository.findAll()
                .stream()
                .map(dtoUtility::betToBetResponse)
                .toList();
    }

    public BetResponse getBetById(Long id) {
        Optional<Bet> bet = betRepository.findById(id);

        if (bet.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.betToBetResponse(bet.get());
    }

    public Bet getBetEntityById(Long id) {
        Optional<Bet> bet = betRepository.findById(id);

        if (bet.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return bet.get();
    }

    public List<BetResponse> getAllBetsByUserId(String userId) {
        return betRepository.findByUserId(userId)
                .stream()
                .map(dtoUtility::betToBetResponse)
                .toList();
    }

    public List<BetResponse> getAllBetsByMatchId(Long matchId) {
        return betRepository.findByMatchId(matchId)
                .stream()
                .map(dtoUtility::betToBetResponse)
                .toList();
    }

    public BetResponse updateBet(BetRequest updatedBetRequest, Long id, String jwtUserId) {
        validateJWTSubject(updatedBetRequest.getUserId(), jwtUserId);
        validateMatch(updatedBetRequest);

        Optional<Bet> betById = betRepository.findById(id);

        if (betById.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        if (betById.get().getMatchFinished()) {
            throw new BetException("Match is already finished", HttpStatus.BAD_REQUEST);
        }

        Bet updatedBet = dtoUtility.betRequestToBet(updatedBetRequest, id);
        updatedBet.setCorrectPrediction(betById.get().getCorrectPrediction());
        updatedBet.setMatchFinished(betById.get().getMatchFinished());

        // Can't edit user or match of a bet
        updatedBet.setUserId(betById.get().getUserId());
        updatedBet.setMatchId(betById.get().getMatchId());
        updatedBet = betRepository.save(updatedBet);

        return dtoUtility.betToBetResponse(updatedBet);
    }

    public void setMatchResult(Long matchId, Long winnerId) {
        List<Bet> betsByMatchId = betRepository.findByMatchId(matchId);
        for (Bet bet : betsByMatchId) {
            bet.setMatchFinished(true);
            bet.setCorrectPrediction(Objects.equals(bet.getPredictedTeamId(), winnerId));
            betRepository.save(bet);
        }
    }

    public void deleteBet(Long id, String jwtUserId) {
        Optional<Bet> betToDelete = betRepository.findById(id);

        if (betToDelete.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        validateJWTSubject(betToDelete.get().getUserId(), jwtUserId);

        betRepository.delete(betToDelete.get());
    }

    public void deleteBetsByUserId(String userId) {
        List<Bet> betsByUserId = betRepository.findByUserId(userId);
        betRepository.deleteAll(betsByUserId);
    }

    public void deleteBetsByMatchId(Long matchId) {
        List<Bet> betsByMatchId = betRepository.findByMatchId(matchId);
        betRepository.deleteAll(betsByMatchId);
    }

    private void validateJWTSubject(String requestUserId, String jwtUserId) {
        if (!Objects.equals(requestUserId, jwtUserId)) {
            throw new BetException("JWT subject is different from request user ID", HttpStatus.CONFLICT);
        }
    }

    private void validateMatch(BetRequest betRequest) {
        MatchResponse matchResponse = webClientBuilder.build()
                .get()
                .uri("http://match-service/matches/" + betRequest.getMatchId())
                .retrieve()
                .bodyToMono(MatchResponse.class)
                .block();
        assert matchResponse != null;
        if (LocalDateTime.now().isAfter(matchResponse.getStart())) {
            throw new BetException("You cannot bet for a match that has already started", HttpStatus.NOT_ACCEPTABLE);
        }
        if (matchResponse.getWinner() != Winner.TBD) {
            throw new BetException("You cannot bet for a match that has already ended", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
