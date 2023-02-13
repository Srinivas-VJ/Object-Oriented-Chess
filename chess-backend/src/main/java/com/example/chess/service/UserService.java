package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserByUserName(String userName){
        Optional<User> user = userRepository.findById(userName);
        if (user.isPresent())
            return user.get();
       throw new UserNotFoundException();
    }

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User deleteUserByUserName(String userName) {
        Optional<User> user = userRepository.findById(userName);
        if (user.isEmpty())
            throw new UserNotFoundException();
        User deletedUser = user.get();
        userRepository.deleteById(userName);
        return deletedUser;
    }
}
