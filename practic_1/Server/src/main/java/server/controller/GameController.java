package server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.Game;
import server.model.User;
import server.repository.GameRepository;
import server.service.GameService;
import server.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final UserService userService;


    public GameController(GameService gameService,  UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam Long userId) {
        logger.info("GET /api/games/stats?userId={}", userId);
        User user = userService.findById(userId);
        Map<String, Object> games = gameService.getStatistics(user);
        return ResponseEntity.ok(games);
    }

    @DeleteMapping("/stats")
    public ResponseEntity<Void> clearStatistics(@RequestParam Long userId) {
        logger.info("DELETE /api/games/stats?userId={}", userId);
        User user = userService.findById(userId);
        gameService.clearStatistics(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam Long userId) {
        logger.info("POST /api/games/start?userId={}", userId);
        User user = userService.findById(userId);
        Game game = gameService.startGame(user);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<Game> makeMove(@PathVariable Long gameId, @RequestParam int cellIndex) {
        logger.info("POST /api/games/{}/move?cellIndex={}", gameId, cellIndex);
        Game game = gameService.makeMove(gameId, cellIndex);
        return ResponseEntity.ok(game);
    }
    @PostMapping("/{gameId}/undo")
    public ResponseEntity<Game> undoMove(@PathVariable Long gameId, @RequestParam int cellIndex) {
        logger.info("POST /api/games/{}/undo?cellIndex={}", gameId, cellIndex);
        Game game = gameService.undoMove(gameId, cellIndex);
        return ResponseEntity.ok(game);
    }
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable Long gameId) {
        logger.info("GET /api/games/{}", gameId);
        Game game = gameService.findById(gameId);
        return ResponseEntity.ok(game);
    }

}
