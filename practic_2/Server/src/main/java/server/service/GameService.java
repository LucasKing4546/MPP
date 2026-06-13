package server.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import server.model.Game;
import server.model.User;
import server.repository.GameRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class.getName());
    private GameRepository gameRepository;

    public  GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Map<String, Object> getStatistics(User user) {
        logger.info("Getting statistics for user " + user.getUsername());

        List<Game> games = gameRepository.findByUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("total games", games.size());
        map.put("highest score", games.stream().mapToInt(Game::getScore).max().orElse(0));
        return map;
    }

    @Transactional
    public void clearStatistics(User user) {
        logger.info("Clearing statistics for user " + user.getUsername());
        gameRepository.deleteByUser(user);
    }

    public Game startGame(User user) {
        logger.info("Starting game for user " + user.getUsername());
        Game game = new Game(user);
        Random random = new Random();
        game.setLastNumber(random.nextInt(1001));
        return gameRepository.save(game);
    }

    public Game guessHigherOrLower(Long gameId, String guess) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        Random random = new Random();
        int new_number  = random.nextInt(1001);
        if (guess.equals("higher") && new_number >= game.getLastNumber()) {
            game.setLastNumber(new_number);
            int new_score = game.getScore() + 1;
            game.setScore(new_score);
        }
        else if (guess.equals("lower") && new_number <= game.getLastNumber()) {
            game.setLastNumber(new_number);
            int new_score = game.getScore() + 1;
            game.setScore(new_score);
        }
        else{
            game.setFinished(true);
        }
        return gameRepository.save(game);
    }

    public Game findById(Long gameId) {
        logger.info("Finding game by id " + gameId);
        return gameRepository.findById(gameId).orElseThrow() ;
    }
}
