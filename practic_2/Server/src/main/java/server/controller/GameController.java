package server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.Game;
import server.model.User;
import server.service.GameService;
import server.service.UserService;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam Long userId) {
        logger.info("GET /api/games/stats?userId={}", userId);
        User user = userService.findById(userId);
        Map<String, Object> map = gameService.getStatistics(user);
        return ResponseEntity.ok(map);
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
        logger.info("POST /api/games/start?user_id={}", userId);
        User user = userService.findById(userId);
        Game game = gameService.startGame(user);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable Long gameId) {
        logger.info("POST /api/games/{}", gameId);
        Game game = gameService.findById(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/guess")
    public ResponseEntity<Game> guessHigherOrLower(@PathVariable Long gameId, @RequestParam String user_guess) {
        logger.info("POST /api/games/{}/guess?user_guess={}", gameId,  user_guess);
        Game game = gameService.guessHigherOrLower(gameId, user_guess);
        return ResponseEntity.ok(game);
    }
}
