package com.github.dearmann.matchservice.dto;

import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.dto.response.GameResponse;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.model.*;
import com.github.dearmann.matchservice.service.EventService;
import com.github.dearmann.matchservice.service.GameService;
import com.github.dearmann.matchservice.service.MatchService;
import com.github.dearmann.matchservice.service.TeamService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DtoUtility {

    private final EventService eventService;
    private final GameService gameService;
    private final TeamService teamService;
    private final MatchService matchService;

    public DtoUtility(@Lazy EventService eventService,
                      @Lazy GameService gameService,
                      @Lazy TeamService teamService,
                      @Lazy MatchService matchService) {
        this.eventService = eventService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.matchService = matchService;
    }

    public Event eventRequestToEvent(EventRequest eventRequest, Long id) {
        return Event.builder()
                .id(id)
                .name(eventRequest.getName())
                .region(eventRequest.getRegion())
                .season(eventRequest.getSeason())
                .start(eventRequest.getStart())
                .end(eventRequest.getEnd())
                .game(gameService.getGameEntityById(eventRequest.getGameId()))
                .build();
    }

    public EventResponse eventToEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .region(event.getRegion())
                .season(event.getSeason())
                .start(event.getStart())
                .end(event.getEnd())
                .gameId(event.getGame().getId())
                .teamIds(getAllTeamIdsByEventId(event.getId()))
                .build();
    }

    public List<Long> getAllTeamIdsByEventId(Long eventId) {
        Set<Long> teamIdSet = new HashSet<>();
        for (MatchResponse matchResponse : matchService.getAllMatchesFromEventId(eventId)) {
            teamIdSet.add(matchResponse.getTeam1().getId());
            teamIdSet.add(matchResponse.getTeam2().getId());
        }
        return teamIdSet.stream().toList();
    }

    public Game gameRequestToGame(GameRequest gameRequest, Long id) {
        return Game.builder()
                .id(id)
                .name(gameRequest.getName())
                .logoUrl(gameRequest.getLogoUrl())
                .build();
    }

    public GameResponse gameToGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .logoUrl(game.getLogoUrl())
                .eventIds(Optional.ofNullable(game.getEvents())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Event::getId)
                        .toList())
                .teamIds(Optional.ofNullable(game.getTeams())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Team::getId)
                        .toList())
                .build();
    }

    public Match matchRequestToMatch(MatchRequest matchRequest, Long id) {
        Match match = Match.builder()
                .id(id)
                .winner(matchRequest.getWinner())
                .start(matchRequest.getStart())
                .end(matchRequest.getEnd())
                .event(eventService.getEventEntityById(matchRequest.getEventId()))
                .team1(teamService.getTeamEntityById(matchRequest.getTeam1Id()))
                .team2(teamService.getTeamEntityById(matchRequest.getTeam2Id()))
                .build();
        if (match.getWinner() == null) {
            match.setWinner(Winner.TBD);
        }
        return match;
    }

    public MatchResponse matchToMatchResponse(Match match) {
        return MatchResponse.builder()
                .id(match.getId())
                .winner(match.getWinner())
                .start(match.getStart())
                .end(match.getEnd())
                .eventId(match.getEvent().getId())
                .team1(teamToTeamResponse(match.getTeam1()))
                .team2(teamToTeamResponse(match.getTeam2()))
                .build();
    }

    public Team teamRequestToTeam(TeamRequest teamRequest, Long id) {
        return Team.builder()
                .id(id)
                .name(teamRequest.getName())
                .logoUrl(teamRequest.getLogoUrl())
                .game(gameService.getGameEntityById(teamRequest.getGameId()))
                .build();
    }

    public TeamResponse teamToTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .logoUrl(team.getLogoUrl())
                .gameId(team.getGame().getId())
                .matchesAsTeam1(Optional.ofNullable(team.getMatchesAsTeam1())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Match::getId)
                        .toList())
                .matchesAsTeam2(Optional.ofNullable(team.getMatchesAsTeam2())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Match::getId)
                        .toList())
                .build();
    }
}
