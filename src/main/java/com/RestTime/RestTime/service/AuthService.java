package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.LoginRequest;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        var utilisateur = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        UserDetails userDetails = User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();

        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}