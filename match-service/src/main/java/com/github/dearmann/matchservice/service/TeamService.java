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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final DtoUtility dtoUtility;
    private final MatchService matchService;

    public TeamResponse createTeam(TeamRequest teamRequest) {
        Team team = dtoUtility.teamRequestToTeam(teamRequest, 0L);
        team = teamRepository.save(team);

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

    public List<TeamResponse> getAllTeamsByGameId(Long gameId) {
        return teamRepository.findByGameId(gameId)
                .stream()
                .map(dtoUtility::teamToTeamResponse)
                .toList();
    }

    public TeamResponse updateTeam(TeamRequest updatedTeamRequest, Long id) {
        Optional<Team> teamById = teamRepository.findById(id);

        if (teamById.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Team updatedTeam = dtoUtility.teamRequestToTeam(updatedTeamRequest, id);

        // Can't edit game of a team
        updatedTeam.setGame(teamById.get().getGame());
        updatedTeam = teamRepository.save(updatedTeam);

        return dtoUtility.teamToTeamResponse(updatedTeam);
    }

    public void deleteTeam(Long id) {
        Optional<Team> teamToDelete = teamRepository.findById(id);

        if (teamToDelete.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        teamToDelete.get().getMatchesAsTeam1().forEach(match -> {
            matchService.deleteMatch(match.getId());
        });
        teamToDelete.get().getMatchesAsTeam2().forEach(match -> {
            matchService.deleteMatch(match.getId());
        });
        teamRepository.delete(teamToDelete.get());
    }

    public void deleteTeamNotCascading(Long id) {
        Optional<Team> teamToDelete = teamRepository.findById(id);

        if (teamToDelete.isEmpty()) {
            throw new BadEntityIdException("Team not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        teamRepository.delete(teamToDelete.get());
    }
}
