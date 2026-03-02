package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.ForgotPasswordRequestDTO;
import com.RestTime.RestTime.dto.LoginRequest;
import com.RestTime.RestTime.service.AuthService;
import com.RestTime.RestTime.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UtilisateurService utilisateurService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        utilisateurService.requestPasswordReset(request);
        return ResponseEntity.ok("Si l'email existe, un lien de réinitialisation a été envoyé.");
    }
}
