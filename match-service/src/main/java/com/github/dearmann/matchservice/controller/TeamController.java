package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.TeamRequest;
import com.github.dearmann.matchservice.dto.response.TeamResponse;
import com.github.dearmann.matchservice.service.FileService;
import com.github.dearmann.matchservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(@Valid @RequestBody TeamRequest teamRequest) {
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

    @GetMapping("/by-game/{gameId}")
    public List<TeamResponse> getAllTeamsByGameId(@PathVariable Long gameId) {
        return teamService.getAllTeamsByGameId(gameId);
    }

    @PutMapping("/{id}")
    public TeamResponse updateTeam(@Valid @RequestBody TeamRequest updatedTeamRequest, @PathVariable Long id) {
        return teamService.updateTeam(updatedTeamRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @GetMapping("/logo")
    public List<String> listObjectsUrls() {
        return fileService.listObjectsUrls("betting-team-logo");
    }

    @GetMapping("/logo/{fileName}")
    public ResponseEntity<InputStreamResource> downloadObject(@PathVariable String fileName) {
        return fileService.downloadObject(fileName, "betting-team-logo");
    }

    @PostMapping("/logo")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadObject(@RequestParam("file") MultipartFile file) {
        return fileService.uploadObject(file, "betting-team-logo");
    }

}
