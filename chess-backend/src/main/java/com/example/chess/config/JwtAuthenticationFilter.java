package com.example.chess.config;

import com.example.chess.domain.User;
import com.example.chess.service.CustomUserDetailsService;
import com.example.chess.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String authHeader =  request.getHeader("Authorization");
       final String jwtToken;
       final String email;
       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
       }
       jwtToken = authHeader.substring(7);
       try {
           email = jwtService.extractUserName(jwtToken);
       }
       catch (Exception ex) {
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
           response.flushBuffer();
           return; // End the request
       }
       if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)   {
           User userDetails = this.userDetailsService.loadUserByUsername(email);
           try {
               if (jwtService.isTokenValid(jwtToken, userDetails)) {
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
               }
           }
           catch (Exception exception) {
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
               response.flushBuffer();
               return; // End the request
           }
       }
       filterChain.doFilter(request, response);

    }
}
