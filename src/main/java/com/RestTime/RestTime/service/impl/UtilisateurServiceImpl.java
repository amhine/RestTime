package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.mapper.UtilisateurMapper;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.EmailService;
import com.RestTime.RestTime.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UserRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return utilisateurMapper.toUserResponseDTO(user);
    }
    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = utilisateurMapper.toEntity(request);
        user.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));



        User savedUser = utilisateurRepository.save(user);
        return utilisateurMapper.toUserResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        User user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setSoldeConges(request.getSoldeConges());



        User updatedUser = utilisateurRepository.save(user);
        return utilisateurMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserRole(Long id, UpdateRoleRequestDTO request) {
        User user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        user.setRole(request.getRole());

        User updatedUser = utilisateurRepository.save(user);
        return utilisateurMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur non trouvé");
        }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getCurrentUser(String email) {
        User user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        return utilisateurMapper.toUserResponseDTO(user);
    }
    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequestDTO request) {
        User user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), user.getMotDePasse())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect");
        }

        user.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(user);
    }

    @Override
    public void requestPasswordReset(ForgotPasswordRequestDTO request) {
        User user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Aucun compte associé à cet email"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenCreationDate(LocalDateTime.now());
        utilisateurRepository.save(user);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = utilisateurRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Lien de réinitialisation invalide ou expiré"));

        if (user.getResetTokenCreationDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Le lien de réinitialisation a expiré");
        }

        user.setMotDePasse(passwordEncoder.encode(newPassword));

        user.setResetToken(null);
        user.setResetTokenCreationDate(null);

        utilisateurRepository.save(user);
    }

}