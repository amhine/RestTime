package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.JustificatifResponseDTO;
import com.RestTime.RestTime.dto.SoldeCongeDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.repository.DemandeCongeRepository;
import com.RestTime.RestTime.repository.HistoriqueRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.EmployeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements EmployeService {

    private final UserRepository userRepository;
    private final DemandeCongeRepository demandeCongeRepository;
    private final HistoriqueRepository historiqueRepository; // ← INDISPENSABLE

    @Value("${app.upload.dir:uploads/justificatifs}")
    private String uploadDir;

    @Override
    public SoldeCongeDTO getSoldeConges(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return new SoldeCongeDTO(user.getId(), user.getNom(), user.getPrenom(), user.getSoldeConges());
    }

    @Override
    @Transactional
    public void annulerDemande(Long demandeId, Long userId) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à annuler cette demande.");
        }

        if (!demande.getStatut().equals(StatutDemande.EN_ATTENTE)) {
            throw new RuntimeException(
                    "Impossible d'annuler : la demande n'est plus en attente (statut actuel : "
                            + demande.getStatut() + ")."
            );
        }

        historiqueRepository.deleteByDemandeConge(demande);

        demandeCongeRepository.delete(demande);
    }

    @Override
    @Transactional
    public JustificatifResponseDTO uploadJustificatif(Long demandeId, Long userId, MultipartFile fichier) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette demande.");
        }

        if (fichier.isEmpty()) {
            throw new RuntimeException("Le fichier est vide.");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = fichier.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String nomFichier = "demande_" + demandeId + "_" + UUID.randomUUID() + extension;

            Path cheminComplet = uploadPath.resolve(nomFichier);
            Files.copy(fichier.getInputStream(), cheminComplet);

            demande.setCheminJustificatif(cheminComplet.toString());
            demandeCongeRepository.save(demande);

            return new JustificatifResponseDTO(demandeId, nomFichier, cheminComplet.toString(),
                    "Justificatif téléversé avec succès.");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
        }
    }
}