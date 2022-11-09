package com.github.dearmann.matchservice.service;

import com.github.dearmann.matchservice.dto.DtoUtility;
import com.github.dearmann.matchservice.dto.request.GameRequest;
import com.github.dearmann.matchservice.dto.response.GameResponse;
import com.github.dearmann.matchservice.exception.BadEntityIdException;
import com.github.dearmann.matchservice.model.Game;
import com.github.dearmann.matchservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final DtoUtility dtoUtility;

    public GameResponse createGame(GameRequest gameRequest) {
        Game game = dtoUtility.gameRequestToGame(gameRequest, 0L);
        game = gameRepository.save(game);

        return dtoUtility.gameToGameResponse(game);
    }

    public List<GameResponse> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(dtoUtility::gameToGameResponse)
                .toList();
    }

    public GameResponse getGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);

        if (game.isEmpty()) {
            throw new BadEntityIdException("Game not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return dtoUtility.gameToGameResponse(game.get());
    }

    public Game getGameEntityById(Long id) {
        Optional<Game> game = gameRepository.findById(id);

        if (game.isEmpty()) {
            throw new BadEntityIdException("Game not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        return game.get();
    }

    public GameResponse updateGame(GameRequest updatedGameRequest, Long id) {
        Optional<Game> gameById = gameRepository.findById(id);

        if (gameById.isEmpty()) {
            throw new BadEntityIdException("Game not found ID - " + id, HttpStatus.NOT_FOUND);
        }

        Game updatedGame = dtoUtility.gameRequestToGame(updatedGameRequest, id);
        updatedGame = gameRepository.save(updatedGame);

        return dtoUtility.gameToGameResponse(updatedGame);
    }

    public void deleteGame(Long id) {
        Optional<Game> gameToDelete = gameRepository.findById(id);

        if (gameToDelete.isEmpty()) {
            throw new BadEntityIdException("Game not found ID - " + id, HttpStatus.NOT_FOUND);
        }
        gameRepository.delete(gameToDelete.get());
    }
}
