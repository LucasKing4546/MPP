# Exam Initialization — Complete Reference (Updated)

## SERVER — 4 files

### 1. `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.exam</groupId>
    <artifactId>Server</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.44.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-community-dialects</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 2. `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:sqlite:exam.sqlite
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

### 3. `src/main/java/server/ServerApplication.java`

```java
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
```

### 4. `src/main/java/server/model/User.java` (always needed)

```java
package server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
```

**Notes:**
- Use `GenerationType.AUTO` not `IDENTITY` (SQLite doesn't support IDENTITY)
- Always add `@JsonIgnoreProperties({"hibernateLazyInitializer"})` on every entity
- Always add an empty constructor
- Package is `server` — all sub-packages: `server.model`, `server.service`, `server.repository`, `server.controller`

**→ Reload Maven, hit Run on ServerApplication. Server alive on localhost:8080.**

---

## CLIENT — 4 files

### 1. `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.exam</groupId>
    <artifactId>Client</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <javafx.version>21.0.1</javafx.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>
</project>
```

### 2. `src/main/java/client/Launcher.java`

```java
package client;

public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
```

**Always run Launcher.main(), never MainApp.main() directly — avoids the "JavaFX runtime components missing" error.**

### 3. `src/main/java/client/MainApp.java`

```java
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class MainApp extends Application {

    private static final Logger logger = Logger.getLogger(MainApp.class.getName());
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Client");
        switchScene("/login.fxml");
        stage.show();
    }

    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            primaryStage.setScene(new Scene(loader.load(), 500, 400));
        } catch (Exception e) {
            logger.severe("Failed to load scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

**Call `MainApp.switchScene("/menu.fxml")` from any controller to navigate.**

### 4. `src/main/java/client/service/HttpService.java`

```java
package client.service;

import javafx.application.Platform;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class HttpService {

    private static final Logger logger = Logger.getLogger(HttpService.class.getName());
    private static final String BASE = "http://localhost:8080/api";
    private final HttpClient client = HttpClient.newHttpClient();

    public void get(String path, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("GET " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("GET " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }

    public void post(String path, String body, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("POST " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("POST " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }

    public void delete(String path, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .DELETE().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("DELETE " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("DELETE " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }
}
```

**→ Reload Maven, run Launcher.main(). Window opens.**

---

## GENERAL PROJECT STRUCTURE

```
Server/
├── pom.xml
└── src/main/
    ├── java/server/
    │   ├── ServerApplication.java
    │   ├── model/
    │   │   ├── User.java              ← @Entity, GenerationType.AUTO, @JsonIgnoreProperties
    │   │   └── Game.java              ← one per table
    │   ├── repository/
    │   │   ├── UserRepository.java    ← extends JpaRepository, findByUsername
    │   │   └── GameRepository.java    ← findByUser, deleteByUser
    │   ├── service/
    │   │   ├── UserService.java       ← @Service, getOrCreateUser, findById
    │   │   └── GameService.java       ← @Service, @Transactional on deletes
    │   └── controller/
    │       ├── UserController.java    ← @RestController, @RequestMapping("/api/users")
    │       └── GameController.java    ← @RestController, @RequestMapping("/api/games")
    └── resources/
        └── application.properties


Client/
├── pom.xml
└── src/main/
    ├── java/client/
    │   ├── Launcher.java              ← entry point, always run this
    │   ├── MainApp.java               ← switchScene() for navigation
    │   ├── Session.java               ← static userId, gameId, username
    │   ├── service/
    │   │   └── HttpService.java       ← get/post/delete with callbacks
    │   └── controller/
    │       ├── LoginController.java   ← one per FXML screen
    │       ├── MenuController.java
    │       └── GameController.java
    └── resources/
        ├── login.fxml                 ← design in Scene Builder
        ├── menu.fxml
        └── game.fxml
```

## GOTCHAS TO REMEMBER

1. **JDK 21** (ms-21) for both projects, not 25
2. **GenerationType.AUTO** not IDENTITY (SQLite)
3. **@JsonIgnoreProperties({"hibernateLazyInitializer"})** on every entity
4. **@Service** on every service class (forgetting = bean not found error)
5. **@Transactional** on any service method that does a delete
6. **Run Launcher.main()** not MainApp.main()
7. **Delete exam.sqlite** and restart server if you change entity fields
8. All packages under `server.*` on server, `client.*` on client
9. Parse JSON with `JsonParser.parseString(response).getAsJsonObject()`
10. Scene Builder: set fx:controller to full path like `client.controller.LoginController`

## COMMON PATTERNS

### Server: entity with relationship
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;
```

### Server: repository auto-generated queries
```java
List<Game> findByUser(User user);
void deleteByUser(User user);
Optional<User> findByUsername(String username);
```

### Server: statistics in service (not repository)
```java
public Map<String, Object> getStatistics(User user) {
    List<Game> games = gameRepository.findByUser(user);
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalGames", games.size());
    stats.put("maxScore", games.stream().mapToInt(Game::getScore).max().orElse(0));
    stats.put("avgUndos", games.stream().mapToInt(Game::getUndoCount).average().orElse(0));
    return stats;
}
```

### Client: call server from any controller
```java
private final HttpService http = new HttpService();

http.post("/users?username=" + username, "",
    response -> {
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        Session.userId = json.get("id").getAsLong();
        MainApp.switchScene("/menu.fxml");
    },
    error -> label.setText("Failed")
);
```

### Client: Session for sharing data between controllers
```java
public class Session {
    public static Long userId;
    public static Long gameId;
    public static String username;
}
```
