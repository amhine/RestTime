package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.mapper.UtilisateurMapper;
import com.RestTime.RestTime.model.entity.Utilisateur;
import com.RestTime.RestTime.repository.ServiceRepository;
import com.RestTime.RestTime.repository.UtilisateurRepository;
import com.RestTime.RestTime.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ServiceRepository serviceRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder; // Bean à configurer dans SecurityConfig

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(request);
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        if (request.getServiceId() != null) {
            com.RestTime.RestTime.model.entity.Service service =
                    serviceRepository.findById(request.getServiceId())                    .orElseThrow(() -> new EntityNotFoundException("Service non trouvé"));
            utilisateur.setService(service);
        }

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toUserResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setSoldeConges(request.getSoldeConges());

        if (request.getServiceId() != null) {
            com.RestTime.RestTime.model.entity.Service service =
                    serviceRepository.findById(request.getServiceId())                    .orElseThrow(() -> new EntityNotFoundException("Service non trouvé"));
            utilisateur.setService(service);
        }

        Utilisateur updatedUser = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserRole(Long id, UpdateRoleRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        utilisateur.setRole(request.getRole());

        Utilisateur updatedUser = utilisateurRepository.save(utilisateur);
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
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        return utilisateurMapper.toUserResponseDTO(utilisateur);
    }
    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }
    @Override
    public void requestPasswordReset(ForgotPasswordRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Aucun compte associé à cet email"));


        System.out.println("--------------------------------------------------");
        System.out.println("DEMANDE DE RESET MOT DE PASSE POUR : " + utilisateur.getEmail());
        System.out.println("Lien simulé : http://localhost:4200/reset-password?token=AZERTY123");
        System.out.println("--------------------------------------------------");
    }
}