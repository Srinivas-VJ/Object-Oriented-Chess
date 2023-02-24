package com.example.chess.controller;

import com.example.chess.domain.User;
import com.example.chess.domain.UserResponse;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUser();
        List<UserResponse> response = new ArrayList<>();
        users.stream().map(UserResponse::new).forEach(response::add);
        return response;
    }
    @GetMapping("/users/{userName}")
    public UserResponse getUserByUsername(@PathVariable String userName) {
        return new UserResponse(userService.getUserByUserName(userName));
    }
    @DeleteMapping("/users/{userName}")
    public ResponseEntity<Object> deleteUserByUserName (@PathVariable String userName) {
        User deletedUser = userService.deleteUserByUserName(userName);
        if (deletedUser == null)
            throw new UserNotFoundException();
        return new ResponseEntity<>(new UserResponse(deletedUser), HttpStatus.NO_CONTENT);
    }
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userName}").buildAndExpand(user.getUsername()).toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/test")
    public String test(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
