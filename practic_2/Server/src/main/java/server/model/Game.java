package server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

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
    private boolean finished;
    private int lastNumber;

    public Game() {}

    public Game(User user) {
        this.user = user;
        this.score = 0;
        this.finished = false;
        this.lastNumber = -1;
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

    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getLastNumber() {
        return lastNumber;
    }
    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }
}
