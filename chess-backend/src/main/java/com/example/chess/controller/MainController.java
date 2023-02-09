package com.example.chess.controller;

import com.example.chess.exception.UserNotFoundException;
import com.example.chess.service.UserService;
import com.example.chess.domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    GameController gameMoveController;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }
    @GetMapping("/users/{userName}")
    public User getAllUsers(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }
    @DeleteMapping("/users/{userName}")
    public ResponseEntity<Object> deleteUserByUserName (@PathVariable String userName) {
        User deletedUser = userService.deleteUserByUserName(userName);
        if (deletedUser == null)
            throw new UserNotFoundException();
        return new ResponseEntity<>(deletedUser, HttpStatus.NO_CONTENT);
    }
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userName}").buildAndExpand(user.getUserName()).toUri();
        return ResponseEntity.created(location).build();
    }
}
