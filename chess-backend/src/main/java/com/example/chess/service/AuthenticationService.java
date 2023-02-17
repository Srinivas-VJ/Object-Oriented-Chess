package com.example.chess.service;

import com.example.chess.domain.AuthenticationRequest;
import com.example.chess.domain.AuthenticationResponse;
import com.example.chess.domain.RegisterRequest;
import com.example.chess.domain.User;
import com.example.chess.exception.UserNameAlreadyExistsException;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> tempUser  = repository.findByUsername(request.getUsername());
        if (tempUser.isPresent())
            throw new UserNameAlreadyExistsException();
        User user = User.builder()
                .userEmail(request.getUserEmail())
                .username(request.getUsername())
                .profilePicture(request.getProfilePicture())
                .password(passwordEncoder.encode(request.getPassword()))
                .gamesDrawn(0)
                .gamesLost(0)
                .gamesWon(0)
                .rating(600)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = repository.findByUsername(request.getUsername());
        if (user.isEmpty())
            throw new UserNotFoundException();
        var jwtToken = jwtService.generateToken(user.get());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
