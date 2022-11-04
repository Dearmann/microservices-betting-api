package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Match;
import com.github.dearmann.matchservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final DtoUtility dtoUtility;

    public MatchResponse createMatch(MatchRequest matchRequest) {
        Match match = dtoUtility.matchRequestToMatch(matchRequest, null);
        matchRepository.save(match);

        return dtoUtility.matchToMatchResponse(match);
    }

    public List<MatchResponse> getAllMatches() {
        return matchRepository.findAll()
                .stream()
                .map(dtoUtility::matchToMatchResponse)
                .toList();
    }

    public MatchResponse getMatchById(Long id) {
        Optional<Match> match = matchRepository.findById(id);

        if (match.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id);
        }
        return dtoUtility.matchToMatchResponse(match.get());
    }

    public MatchResponse updateMatch(Long id, MatchRequest updatedMatchRequest) {
        Optional<Match> matchById = matchRepository.findById(id);

        if (matchById.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id);
        }

        Match updatedMatch = dtoUtility.matchRequestToMatch(updatedMatchRequest, id);
        matchRepository.save(updatedMatch);

        return dtoUtility.matchToMatchResponse(updatedMatch);
    }

    public void deleteMatch(Long id) {
        Optional<Match> matchToDelete = matchRepository.findById(id);

        if (matchToDelete.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id);
        }
        matchRepository.delete(matchToDelete.get());
    }
}
