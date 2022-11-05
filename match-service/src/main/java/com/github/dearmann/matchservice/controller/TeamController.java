package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(@RequestBody TeamRequest teamRequest) {
        return teamService.createTeam(teamRequest);
    }

    @GetMapping
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PutMapping("/{id}")
    public TeamResponse updateTeam(@RequestBody TeamRequest updatedTeamRequest, @PathVariable Long id) {
        return teamService.updateTeam(updatedTeamRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

}
