package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Team;
import com.github.dearmann.matchservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final DtoUtility dtoUtility;

    public TeamResponse createTeam(TeamRequest teamRequest) {
        Team team = dtoUtility.teamRequestToTeam(teamRequest, null);
        teamRepository.save(team);

        return dtoUtility.teamToTeamResponse(team);
    }

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(dtoUtility::teamToTeamResponse)
                .toList();
    }

    public TeamResponse getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.teamToTeamResponse(team.get());
    }

    public Team getTeamEntityById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return team.get();
    }

    public TeamResponse updateTeam(Long id, TeamRequest updatedTeamRequest) {
        Optional<Team> teamById = teamRepository.findById(id);

        if (teamById.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Team updatedTeam = dtoUtility.teamRequestToTeam(updatedTeamRequest, id);

        // Can't edit game of a team
        updatedTeam.setGame(teamById.get().getGame());
        teamRepository.save(updatedTeam);

        return dtoUtility.teamToTeamResponse(updatedTeam);
    }

    public void deleteTeam(Long id) {
        Optional<Team> teamToDelete = teamRepository.findById(id);

        if (teamToDelete.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        teamRepository.delete(teamToDelete.get());
    }
}
