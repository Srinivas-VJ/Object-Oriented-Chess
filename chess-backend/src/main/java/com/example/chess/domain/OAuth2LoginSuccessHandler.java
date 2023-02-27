package com.example.chess.domain;

import com.example.chess.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User user  = (CustomOAuth2User) authentication.getPrincipal();
        Optional<User> existingUser = userRepository.findByUserEmail(user.getAttribute("email"));
        if (existingUser.isEmpty()) {
            // google
            User newUser;
            if (user.getAttributes().containsKey("sub")) {
                newUser = User.builder()
                        .userEmail(user.getEmail())
                        .rating(List.of(600))
                        .gamesWon(0)
                        .gamesLost(0)
                        .gamesDrawn(0)
                        .profilePicture(user.getAttribute("picture"))
                        .password(null)
                        .providerType(Provider.GOOGLE)
                        .username(Objects.requireNonNull(user.getAttribute("given_name")).toString().replaceAll(" ", "_"))
                        .build();
            }
            // github
            else {
                newUser = User.builder()
                        .userEmail(user.getEmail())
                        .rating(List.of(600))
                        .gamesWon(0)
                        .gamesLost(0)
                        .gamesDrawn(0)
                        .profilePicture(user.getAttribute("avatar_url"))
                        .password(null)
                        .providerType(Provider.GITHUB)
                        .username(Objects.requireNonNull(user.getAttribute("login")).toString().replaceAll(" ", "_"))
                        .build();
            }
            userRepository.save(newUser);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
