package com.example.chess.domain;

import com.example.chess.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        Optional<User> existingUser = userRepository.findByUserEmail(email);
        if (existingUser.isEmpty()) {
            userRepository.save(User.builder()
                    .userEmail(email)
                    .rating(List.of(600))
                    .gamesWon(0)
                    .gamesLost(0)
                    .gamesDrawn(0)
                    .profilePicture(oauthUser.getAttribute("picture"))
                    .password(null)
                    .providerType(Provider.GOOGLE)
                    .username(Objects.requireNonNull(oauthUser.getAttribute("given_name")).toString().replace(" ", "_"))
                    .build());
        }
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("http://localhost:3000");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}