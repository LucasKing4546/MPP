package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Game;
import server.model.User;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findByUser(User user);
    List<Game> findTop3ByOrderByScoreDescDrawsDescDateDesc();
}
