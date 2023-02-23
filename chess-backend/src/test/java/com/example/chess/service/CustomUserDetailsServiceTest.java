package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void loadUserByUsername() {
        User dummyUser = User.builder()
                .userEmail("test@gmail.com")
                .username("test")
                .build();
        when(userRepository.findByUserEmail("test@gmail.com")).thenReturn(Optional.ofNullable(dummyUser));
        Optional<User> user = Optional.ofNullable(customUserDetailsService.loadUserByUsername("test@gmail.com"));
        assertTrue(user.isPresent());
    }
    @Test
    void loadNonExistentUserByUsername() {
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("test@gmail.com"));
    }
}