package com.github.dearmann.matchservice.utility;

import com.github.dearmann.matchservice.dto.request.MatchRequest;
import com.github.dearmann.matchservice.dto.response.EventResponse;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.exception.MatchException;
import com.github.dearmann.matchservice.service.EventService;
import com.github.dearmann.matchservice.service.TeamService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MatchValidator {
    private final EventService eventService;
    private final TeamService teamService;

    public MatchValidator(@Lazy EventService eventService,
                          @Lazy TeamService teamService) {
        this.eventService = eventService;
        this.teamService = teamService;
    }

    public void validateMatchCreation(MatchRequest matchRequest) {
        if (Objects.equals(matchRequest.getTeam1Id(), matchRequest.getTeam2Id())) {
            throw new MatchException("The team cannot fight itself", HttpStatus.BAD_REQUEST);
        }

        TeamResponse teamResponse1 = teamService.getTeamById(matchRequest.getTeam1Id());
        TeamResponse teamResponse2 = teamService.getTeamById(matchRequest.getTeam2Id());
        EventResponse eventResponse = eventService.getEventById(matchRequest.getEventId());

        if (!Objects.equals(eventResponse.getGameId(), teamResponse1.getGameId())) {
            throw new BadEntityIdException(
                    teamResponse1.getName() + " cannot be added to " + eventResponse.getName() + ", because they are in different games",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!Objects.equals(eventResponse.getGameId(), teamResponse2.getGameId())) {
            throw new BadEntityIdException(
                    teamResponse2.getName() + " cannot be added to " + eventResponse.getName() + ", because they are in different games",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
