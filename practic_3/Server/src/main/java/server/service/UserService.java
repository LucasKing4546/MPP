package server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import server.model.User;
import server.repository.UserRepository;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String username) {
        logger.info("Getting user by username: " + username);
        return userRepository.findByUsername(username).orElseGet(() ->
                userRepository.save(new User(username)));
    }

    public User findById(Long id) {
        logger.info("Getting user by id: " + id);
        return userRepository.findById(id).orElse(null);
    }
}
