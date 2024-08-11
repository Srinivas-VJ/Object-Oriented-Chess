package com.example.chess.controller;

import com.example.chess.domain.DrawState;
import com.example.chess.domain.Game;
import com.example.chess.domain.User;
import com.example.chess.exception.GameHasNoPlayerException;
import com.example.chess.repository.GameRepository;
import com.example.chess.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.*;


@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private GameRepository gameRepository;
    @Autowired
    private WebApplicationContext context;

    private JacksonTester<Game> jacksonTester;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getAllGamesAdmin() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get("/game")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.OK.value() ,response.getStatus());
    }
    @Test
    @WithMockUser(roles="USER")
    @Disabled
    void getAllGamesUser() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get("/game")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value() ,response.getStatus());
    }
    @Test
    @Disabled
    void getAllGamesNoRole() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                        get("/game")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(HttpStatus.FOUND.value() ,response.getStatus());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Disabled
    void getGameByGameId() throws Exception {
        Game game = new Game("test", "test" , "test", "white", new ArrayList<>(), "ongoing", "test game", DrawState.NULL);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        MockHttpServletResponse response = mvc.perform(
                get("/game/test")
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Disabled
    void createGame() throws Exception {
        Game game = new Game("test", "test" , "test", "white", new ArrayList<>(), "ongoing", "test game", DrawState.NULL);
        User dummyUser = User.builder()
                .userEmail("test@gmail.com")
                .username("test")
                .build();
        when(userRepository.findById("test")).thenReturn(Optional.ofNullable(dummyUser));
        MockHttpServletResponse response = mvc.perform(
                post("/game")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(game).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
    @Test
    @WithMockUser(roles = "USER")
    @Disabled
    void createInvalidGame() throws Exception {
        Game game = new Game("test", null , null, "white", new ArrayList<>(), "ongoing", "test game", DrawState.NULL);
        assertThrows(GameHasNoPlayerException.class, () -> mvc.perform(
                        post("/game")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jacksonTester.write(game).getJson()))
                .andReturn().getResponse());
    }
}