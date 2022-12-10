package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.dto.response.GameResponse;
import com.github.dearmann.matchservice.service.FileService;
import com.github.dearmann.matchservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@Valid @RequestBody GameRequest gameRequest) {
        return gameService.createGame(gameRequest);
    }

    @GetMapping
    public List<GameResponse> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public GameResponse getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @PutMapping("/{id}")
    public GameResponse updateGame(@Valid @RequestBody GameRequest updatedGameRequest, @PathVariable Long id) {
        return gameService.updateGame(updatedGameRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    @GetMapping("/logo")
    public List<String> listObjectsUrls() {
        return fileService.listObjectsUrls("betting-game-logo");
    }

    @GetMapping("/logo/{fileName}")
    public ResponseEntity<InputStreamResource> downloadObject(@PathVariable String fileName) {
        return fileService.downloadObject(fileName, "betting-game-logo");
    }

    @PostMapping("/logo")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadObject(@RequestParam("file") MultipartFile file) {
        return fileService.uploadObject(file, "betting-game-logo");
    }

}
