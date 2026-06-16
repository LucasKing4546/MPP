package server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="games")
@JsonIgnoreProperties
public class Game {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int score;
    private int draws;
    private LocalDateTime date;
    private boolean finished;
    private int lastServerGuess;

    public Game() {}

    public Game(User user) {
        this.user = user;
        this.score = 0;
        this.draws = 0;
        this.date = LocalDateTime.now();
        this.finished = false;
        this.lastServerGuess = -1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getDraws() {
        return draws;
    }
    public void setDraws(int draws) {
        this.draws = draws;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getLastServerGuess() {
        return lastServerGuess;
    }
    public void setLastServerGuess(int lastServerGuess) {
        this.lastServerGuess = lastServerGuess;
    }

}
