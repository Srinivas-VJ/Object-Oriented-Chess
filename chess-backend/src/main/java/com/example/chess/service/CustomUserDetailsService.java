package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService, Serializable {
    @Autowired
    private UserRepository userRepo;
    @Override
    @Transactional
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUserEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return user.get();
    }

}
