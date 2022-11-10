package com.github.dearmann.betservice.service;

import com.github.dearmann.betservice.dto.DtoUtility;
import com.github.dearmann.betservice.dto.request.BetRequest;
import com.github.dearmann.betservice.dto.response.BetResponse;
import com.github.dearmann.betservice.exception.BadEntityIdException;
import com.github.dearmann.betservice.model.Bet;
import com.github.dearmann.betservice.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BetService {

    private final BetRepository betRepository;
    private final DtoUtility dtoUtility;

    public BetResponse createBet(BetRequest betRequest) {
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

    public List<BetResponse> getAllBetsByUserId(Long userId) {
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

    public BetResponse updateBet(BetRequest updatedBetRequest, Long id) {
        Optional<Bet> betById = betRepository.findById(id);

        if (betById.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Bet updatedBet = dtoUtility.betRequestToBet(updatedBetRequest, id);
        // Can't edit user or match of a bet
        updatedBet.setUserId(betById.get().getUserId());
        updatedBet.setMatchId(betById.get().getMatchId());
        updatedBet = betRepository.save(updatedBet);

        return dtoUtility.betToBetResponse(updatedBet);
    }

    public void deleteBet(Long id) {
        Optional<Bet> betToDelete = betRepository.findById(id);

        if (betToDelete.isEmpty()) {
            throw new BadEntityIdException("Bet not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        betRepository.delete(betToDelete.get());
    }

    public void deleteBetsByUserId(Long userId) {
        List<Bet> betsByUserId = betRepository.findByUserId(userId);
        betRepository.deleteAll(betsByUserId);
    }

    public void deleteBetsByMatchId(Long matchId) {
        List<Bet> betsByMatchId = betRepository.findByMatchId(matchId);
        betRepository.deleteAll(betsByMatchId);
    }
}
