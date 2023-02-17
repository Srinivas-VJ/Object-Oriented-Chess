package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements Serializable {
    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUser(){
        return (List<User>) userRepository.findAll();
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

    public void updateUser(User player) {
        userRepository.save(player);
    }
}
