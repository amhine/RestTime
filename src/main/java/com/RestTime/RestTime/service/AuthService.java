package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.LoginRequest;
import com.RestTime.RestTime.dto.RegisterRequest;
import com.RestTime.RestTime.model.entity.Utilisateur;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // 1. Création de l'objet Utilisateur à partir de la request
        var utilisateur = Utilisateur.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse())) // On crypte le mot de passe
                .role(request.getRole())
                .soldeConges(25.0) // Solde par défaut comme dans ton exemple
                .build();

        // 2. Sauvegarde en base de données
        userRepository.save(utilisateur);

        // 3. Création du UserDetails pour Spring Security (nécessaire pour générer le token)
        UserDetails userDetails = User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();

        // 4. Génération du token
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        // 1. Authentification via le Manager (vérifie email et mdp)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        // 2. Récupération de l'utilisateur en base
        var utilisateur = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // 3. Création du UserDetails
        UserDetails userDetails = User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();

        // 4. Génération du token
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}