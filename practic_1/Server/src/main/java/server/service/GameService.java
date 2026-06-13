package server.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import server.model.Game;
import server.model.User;
import server.repository.GameRepository;

import java.util.*;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Map<String, Object> getStatistics(User user) {
        logger.info("Getting statistics: for user: " + user.getUsername());
        List<Game> games = gameRepository.findByUser(user);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalGames", games.size());
        stats.put("maxScore", games.stream().mapToInt(Game::getScore).max().orElse(0));
        stats.put("avgUndos", games.stream().mapToInt(Game::getUndoCount).average().orElse(0));
        return stats;
    }

    @Transactional
    public void clearStatistics(User user){
        logger.info("Clearing statistics: for user: " + user.getUsername());
        gameRepository.deleteByUser(user);
    }

    public Game startGame(User user){
        logger.info("Starting game: for user: " + user.getUsername());
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            int num = random.nextInt(-100, 101);
            sb.append(num);
            if (i < 24)
                sb.append(",");
        }
        Game game = new Game(user, sb.toString());
        return gameRepository.save(game);
    }

    public Game makeMove(Long game_id, int cellIndex){
        logger.info("Making move on game {}, cell {}: ", game_id, cellIndex);
        Game game = gameRepository.findById(game_id).orElseThrow();
        int[] numbers = parseGrid(game.getGrid());
        game.setScore(game.getScore() + numbers[cellIndex]);
        return gameRepository.save(game);
    }

    public Game undoMove(Long gameId, int cellIndex) {
        logger.info("Undoing move on game: {}, cell: {}", gameId, cellIndex);
        Game game = gameRepository.findById(gameId).orElseThrow();
        int[] numbers = parseGrid(game.getGrid());
        game.setScore(game.getScore() - numbers[cellIndex]);
        game.setUndoCount(game.getUndoCount() + 1);
        return gameRepository.save(game);
    }
    public Game findById(Long gameId) {
        logger.info("Finding game by id: {}", gameId);
        return gameRepository.findById(gameId).orElseThrow();
    }

    private int[] parseGrid(String grid) {
        String[] parts = grid.split(",");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Integer.parseInt(parts[i].trim());
        }
        return numbers;
    }


}
