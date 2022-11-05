package com.github.dearmann.matchservice.dto;

import com.github.dearmann.matchservice.dto.request.EventRequest;
import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.dto.response.GameResponse;
import com.github.dearmann.matchservice.dto.response.MatchResponse;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.model.Event;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.model.Match;
import com.github.dearmann.matchservice.model.Team;
import com.github.dearmann.matchservice.service.EventService;
import com.github.dearmann.matchservice.service.GameService;
import com.github.dearmann.matchservice.service.TeamService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class DtoUtility {

    private final EventService eventService;
    private final GameService gameService;
    private final TeamService teamService;

    public DtoUtility(@Lazy EventService eventService,
                      @Lazy GameService gameService,
                      @Lazy TeamService teamService) {
        this.eventService = eventService;
        this.gameService = gameService;
        this.teamService = teamService;
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
                .build();
    }

    public Game gameRequestToGame(GameRequest gameRequest, Long id) {
        return Game.builder()
                .id(id)
                .name(gameRequest.getName())
                .build();
    }

    public GameResponse gameToGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .events(Optional.ofNullable(game.getEvents())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::eventToEventResponse)
                        .toList())
                .teams(Optional.ofNullable(game.getTeams())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::teamToTeamResponse)
                        .toList())
                .build();
    }

    public Match matchRequestToMatch(MatchRequest matchRequest, Long id) {
        return Match.builder()
                .id(id)
                .team1(teamService.getTeamEntityById(matchRequest.getTeam1Id()))
                .team2(teamService.getTeamEntityById(matchRequest.getTeam2Id()))
                .team1Won(matchRequest.getTeam1Won())
                .start(matchRequest.getStart())
                .estimatedEnd(matchRequest.getEstimatedEnd())
                .matchOver(matchRequest.getMatchOver())
                .event(eventService.getEventEntityById(matchRequest.getEventId()))
                .build();
    }

    public MatchResponse matchToMatchResponse(Match match) {
        return MatchResponse.builder()
                .id(match.getId())
                .team1(teamToTeamResponse(match.getTeam1()))
                .team2(teamToTeamResponse(match.getTeam2()))
                .team1Won(match.getTeam1Won())
                .start(match.getStart())
                .estimatedEnd(match.getEstimatedEnd())
                .matchOver(match.getMatchOver())
                .eventId(match.getEvent().getId())
                .build();
    }

    public Team teamRequestToTeam(TeamRequest teamRequest, Long id) {
        return Team.builder()
                .id(id)
                .name(teamRequest.getName())
                .game(gameService.getGameEntityById(teamRequest.getGameId()))
                .build();
    }

    public TeamResponse teamToTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
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
