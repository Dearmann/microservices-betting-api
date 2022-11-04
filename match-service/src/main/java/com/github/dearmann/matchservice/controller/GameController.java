package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.dto.response.GameResponse;
import com.github.dearmann.matchservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@RequestBody GameRequest gameRequest) {
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
    public GameResponse updateGame(@PathVariable Long id, @RequestBody GameRequest updatedGameRequest) {
        return gameService.updateGame(id, updatedGameRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

}
