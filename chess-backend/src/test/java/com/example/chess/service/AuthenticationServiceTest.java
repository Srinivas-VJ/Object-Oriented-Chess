package com.example.chess.service;

import com.example.chess.domain.AuthenticationRequest;
import com.example.chess.domain.RegisterRequest;
import com.example.chess.domain.User;
import com.example.chess.exception.UserAlreadyExistsException;
import com.example.chess.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @Disabled
    void registerFails() {
        RegisterRequest request = new RegisterRequest("test", "test@gmail.com", "password", "");
        User dummyUser = User.builder()
                .userEmail("test@gmail.com")
                .password("password")
                .username("test")
                .build();
        when(userRepository.findByUserEmail("test@gmail.com")).thenReturn(Optional.ofNullable(dummyUser));
        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(request));
    }

    @Test
    @Disabled
    void registerSucceeds() {
        RegisterRequest request = new RegisterRequest("test", "abc@gmail.com", "password", "");
        authenticationService.register(request);
        verify(userRepository).findByUserEmail("abc@gmail.com");
    }

    @Test
    @Disabled
    void authenticateFailed() {
        AuthenticationRequest request = new AuthenticationRequest("test@gmail.com", "password");
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(userRepository).findByUserEmail("test@gmail.com");
    }
    @Test
    @Disabled
    void authenticateSuccess() {
        User dummyUser = User.builder()
                .userEmail("test@gmail.com")
                .password("password")
                .username("test")
                .build();
        when(userRepository.findByUserEmail("test@gmail.com")).thenReturn(Optional.ofNullable(dummyUser));
        AuthenticationRequest request = new AuthenticationRequest("test@gmail.com", "password");
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(userRepository).findByUserEmail("test@gmail.com");
    }
}
