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
    private static final Logger logger = LoggerFactory.getLogger(GameService.class.getName());
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
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

    public Map<String, String> getLeaderboard() {
        logger.info("Getting leaderboard");
        Map<String, String> leaderboard = new HashMap<>();
        List<Game> games = gameRepository.findTop3ByOrderByScoreDescDrawsDescDateDesc();
        int i = 0;
        for (Game game : games) {
            i++;
            StringBuilder builder = new StringBuilder();
            builder.append("Username: ").append(game.getUser().getUsername());
            builder.append(", ");
            builder.append("Score: ").append(game.getScore());
            builder.append(", ");
            builder.append("Draws: ").append(game.getDraws());
            builder.append(", ");
            builder.append("Timestamp: ").append(game.getDate());
            leaderboard.put("top" + i, builder.toString());
        }
        return leaderboard;
    }

    public Game startGame(User user) {
        logger.info("Starting game for user " + user.getUsername());
        Game game = new Game(user);
        return gameRepository.save(game);
    }

    public Game findById(Long gameId) {
        logger.info("Finding game by id " + gameId);
        return gameRepository.findById(gameId).orElseThrow() ;
    }

    public Game RockPaperScissors(Game game, int userGuess) {
        logger.info("Rock Paper Scissors for game " + game.getId());
        Random random = new Random();
        int guess = random.nextInt(1000)%3;
        game.setLastServerGuess(guess);
        if (userGuess == guess) {
            game.setDraws(game.getDraws() + 1);
        }
        else if ((userGuess + 1) % 3 ==  guess % 3) {
            game.setScore(game.getScore() + 1);
        }
        else {
            game.setFinished(true);
        }
        return gameRepository.save(game);
    }
}
