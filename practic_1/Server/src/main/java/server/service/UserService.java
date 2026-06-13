package server.service;

import server.model.User;
import server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String username){
        logger.info("Getting user by username: " + username);
        return userRepository.findByUsername(username).orElseGet(() -> {
            logger.info("Creating new user: {}", username);
            return userRepository.save(new User(username));
        });
    }

    public User findById(Long userId){
        logger.info("Finding user by id: " + userId);
        return userRepository.findById(userId).orElse(null);
    }
}
