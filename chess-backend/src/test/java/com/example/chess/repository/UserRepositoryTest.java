package com.example.chess.repository;

import com.example.chess.domain.Provider;
import com.example.chess.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldCheckIfUserExists() {
        User tempUser = User.builder()
                .providerType(Provider.LOCAL)
                .username("test")
                .password(null)
                .profilePicture(null)
                .userEmail("test@gmail.com")
                .gamesDrawn(0)
                .gamesLost(0)
                .gamesWon(0)
                .rating(List.of(600))
                .build();

        userRepository.save(tempUser);

        Optional<User> user = userRepository.findByUserEmail("test@gmail.com");
        assertTrue(user.isPresent());
    }

    @Test
    public void shouldCheckIfUserDoesNotExist() {

        Optional<User> nonExistingUser = userRepository.findByUserEmail("abc@gmail.com");
        assertFalse(nonExistingUser.isPresent());
    }


}