package server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "games")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int score;
    private boolean finished;
    private int undoCount;
    private String grid;

    public Game() {}

    public Game(User user, String grid) {
        this.user = user;
        this.grid = grid;
        this.score = 0;
        this.finished = false;
        this.undoCount = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
    public int getUndoCount() { return undoCount; }
    public void setUndoCount(int undoCount) { this.undoCount = undoCount; }
    public String getGrid() { return grid; }
    public void setGrid(String grid) { this.grid = grid; }
}
