package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Match;
import com.github.dearmann.matchservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final EventService eventService;
    private final TeamService teamService;
    private final DtoUtility dtoUtility;

    public MatchResponse createMatch(MatchRequest matchRequest) {
        TeamResponse teamResponse1 = teamService.getTeamById(matchRequest.getTeam1Id());
        TeamResponse teamResponse2 = teamService.getTeamById(matchRequest.getTeam2Id());
        EventResponse eventResponse = eventService.getEventById(matchRequest.getEventId());

        if (!Objects.equals(eventResponse.getGameId(), teamResponse1.getGameId())) {
            throw new BadEntityIdException(teamResponse1.getName() + " cannot be added to " + eventResponse.getName() + ", because they are in different games",
                    HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(eventResponse.getGameId(), teamResponse2.getGameId())) {
            throw new BadEntityIdException(teamResponse2.getName() + " cannot be added to " + eventResponse.getName() + ", because they are in different games",
                    HttpStatus.BAD_REQUEST);
        }

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
            throw new BadEntityIdException("Match not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.matchToMatchResponse(match.get());
    }

    public List<MatchResponse> getAllMatchesFromEventId(Long eventId) {
        return matchRepository.findByEvent(eventService.getEventEntityById(eventId))
                .stream()
                .map(dtoUtility::matchToMatchResponse)
                .toList();
    }

    public MatchResponse updateMatch(Long id, MatchRequest updatedMatchRequest) {
        Optional<Match> matchById = matchRepository.findById(id);

        if (matchById.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Match updatedMatch = dtoUtility.matchRequestToMatch(updatedMatchRequest, id);

        // Can't edit teams or event of a match
        updatedMatch.setTeam1(matchById.get().getTeam1());
        updatedMatch.setTeam2(matchById.get().getTeam2());
        updatedMatch.setEvent(matchById.get().getEvent());
        matchRepository.save(updatedMatch);

        return dtoUtility.matchToMatchResponse(updatedMatch);
    }

    public void deleteMatch(Long id) {
        Optional<Match> matchToDelete = matchRepository.findById(id);

        if (matchToDelete.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        matchRepository.delete(matchToDelete.get());
    }
}
