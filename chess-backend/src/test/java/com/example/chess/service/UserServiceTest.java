package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.exception.UserAlreadyExistsException;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static User dummyUser;

    @BeforeAll
    static void beforeAll() {
            dummyUser = User.builder()
                    .userEmail("test@gmail.com")
                    .username("test")
                    .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getAllUser() {
        userService.getAllUser();
        verify(userRepository).findAll();
    }

    @Test
    void getUserByUserName() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(dummyUser));
        Optional<User> user = Optional.ofNullable(userService.getUserByUserName("test"));
        assertTrue(user.isPresent());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUserName("Rahul"));
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findByUserEmail("test@gmail.com")).thenReturn(Optional.ofNullable(dummyUser));
        Optional<User> user = Optional.ofNullable(userService.getUserByEmail("test@gmail.com"));
        assertTrue(user.isPresent());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("abc@gmail.com"));
    }

    @Test
    void addUser() {
        userService.addUser(dummyUser);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertEquals(dummyUser, capturedUser);
    }
    @Test
    void addExistingUser() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(dummyUser));
        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(dummyUser));
        verify(userRepository, never()).save(any());
    }


    @Test
    void deleteUserByUserName() {
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(dummyUser));
        User deletedUser = userService.deleteUserByUserName("test");
        verify(userRepository).deleteById("test");
        assertEquals(deletedUser, dummyUser);
    }
    @Test
    void deleteNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserByUserName("Tenma"));
        verify(userRepository, never()).deleteById(any());
    }

}