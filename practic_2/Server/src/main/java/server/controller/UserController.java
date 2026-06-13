package server.controller;

import server.model.User;
import server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> getOrCreate(@RequestParam String username) {
        logger.info("POST /api/users?username={}", username);
        User user = userService.getOrCreateUser(username);
        return ResponseEntity.ok(user);
    }

}
