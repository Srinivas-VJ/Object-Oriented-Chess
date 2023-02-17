package com.example.chess.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
@Builder
public class User implements UserDetails  {
    @Size(min = 3, max = 69, message = "User names must have a minimum size of 3 and a maximum size of 69")
    @NotNull(message = "User name is a required field")
    @Id
    private String username;
    @Email(message = "Invalid Email address")
    @NotNull(message = "User email is a required field")
    private String userEmail;
    private String password;
    private String profilePicture;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrawn;
    private int rating;
    @Enumerated(EnumType.STRING)
    private Provider providerType;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("srini".equals(username))
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

