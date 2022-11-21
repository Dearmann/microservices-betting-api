package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.*;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Match;
import com.github.dearmann.matchservice.model.Winner;
import com.github.dearmann.matchservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final EventService eventService;
    private final TeamService teamService;
    private final DtoUtility dtoUtility;
    private final WebClient.Builder webClientBuilder;

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

        Match match = dtoUtility.matchRequestToMatch(matchRequest, 0L);
        match = matchRepository.save(match);

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

        MatchResponse matchResponse = dtoUtility.matchToMatchResponse(match.get());

        BetResponse[] betArray = webClientBuilder.build().get()
                .uri("http://bet-service/bets/by-matchid/" + id)
                .retrieve()
                .bodyToMono(BetResponse[].class)
                .block();
        matchResponse.setBets(Arrays.stream(betArray != null ? betArray : new BetResponse[0]).toList());

        CommentResponse[] commentArray = webClientBuilder.build().get()
                .uri("http://comment-service/comments/by-matchid/" + id)
                .retrieve()
                .bodyToMono(CommentResponse[].class)
                .block();
        matchResponse.setComments(Arrays.stream(commentArray != null ? commentArray : new CommentResponse[0]).toList());

        RatingResponse[] ratingArray = webClientBuilder.build().get()
                .uri("http://rate-service/ratings/by-matchid/" + id)
                .retrieve()
                .bodyToMono(RatingResponse[].class)
                .block();
        matchResponse.setRatings(Arrays.stream(ratingArray != null ? ratingArray : new RatingResponse[0]).toList());

        return matchResponse;
    }

    public List<MatchResponse> getAllMatchesFromEventId(Long eventId) {
        return matchRepository.findByEvent(eventService.getEventEntityById(eventId))
                .stream()
                .map(dtoUtility::matchToMatchResponse)
                .toList();
    }

    public MatchResponse updateMatch(MatchRequest updatedMatchRequest, Long id) {
        Optional<Match> matchById = matchRepository.findById(id);

        if (matchById.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Match updatedMatch = dtoUtility.matchRequestToMatch(updatedMatchRequest, id);

        // Can't edit teams or event of a match
        updatedMatch.setTeam1(matchById.get().getTeam1());
        updatedMatch.setTeam2(matchById.get().getTeam2());
        updatedMatch.setEvent(matchById.get().getEvent());
        updatedMatch = matchRepository.save(updatedMatch);

        return dtoUtility.matchToMatchResponse(updatedMatch);
    }

    public MatchResponse setMatchResult(Long matchId, Long winnerId) {
        Optional<Match> match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + matchId, HttpStatus.NOT_FOUND);
        }

        webClientBuilder.build()
                .put()
                .uri("http://bet-service/bets/result" + "?matchId=" + matchId + "&winnerId=" + winnerId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        if (Objects.equals(match.get().getTeam1().getId(), winnerId)) {
            match.get().setWinner(Winner.TEAM_1);
        }
        if (Objects.equals(match.get().getTeam2().getId(), winnerId)) {
            match.get().setWinner(Winner.TEAM_2);
        }
        Match matchWithResult = matchRepository.save(match.get());
        return dtoUtility.matchToMatchResponse(matchWithResult);
    }

    public void deleteMatch(Long id) {
        Optional<Match> matchToDelete = matchRepository.findById(id);

        if (matchToDelete.isEmpty()) {
            throw new BadEntityIdException("Match not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        webClientBuilder.build().delete()
                .uri("http://bet-service/bets/by-matchid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        webClientBuilder.build().delete()
                .uri("http://comment-service/comments/by-matchid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        webClientBuilder.build().delete()
                .uri("http://rate-service/ratings/by-matchid/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        matchRepository.delete(matchToDelete.get());
    }
}
