package com.ticketer.ticketer.controller;

import com.ticketer.ticketer.model.User;
import com.ticketer.ticketer.repository.UserRepository;
import com.ticketer.ticketer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User persistedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedUser);
    }
}
