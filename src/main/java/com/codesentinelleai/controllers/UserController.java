package com.codesentinelleai.controllers;

import com.codesentinelleai.entities.User;
import com.codesentinelleai.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@Validated @RequestBody User user) {
        if (user.getUsername().toLowerCase().contains("admin")) {
            log.warn("Unauthorized username pattern detected: {}", user.getUsername());
            throw new IllegalArgumentException("Invalid username: 'admin' is not allowed in the username");
        }

        log.info("Creating user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
}
