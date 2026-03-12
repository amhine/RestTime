package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.Historique;
import com.RestTime.RestTime.model.enumeration.TypeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.repository.DemandeCongeRepository;
import com.RestTime.RestTime.repository.HistoriqueRepository;
import com.RestTime.RestTime.repository.TypeCongeRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.CongeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CongeServiceImpl implements CongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UserRepository userRepository;
    private final TypeCongeRepository typeCongeRepository;
    private final HistoriqueRepository historiqueRepository;

    @Override
    @Transactional
    public DemandeConge soumettreDemande(Long userId, DemandeCongeCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        TypeConge typeConge = typeCongeRepository.findById(dto.getTypeCongeId())
                .orElseThrow(() -> new RuntimeException("Type de congé introuvable"));

        int nombreJours = calculerNombreJours(dto.getDateDebut(), dto.getDateFin());

        if (nombreJours <= 0) {
            throw new RuntimeException("Dates invalides : la date de fin doit être égale ou postérieure à la date de début.");
        }

        if (user.getSoldeConges() < nombreJours) {
            throw new RuntimeException("Solde de congés insuffisant.");
        }

        DemandeConge demande = DemandeConge.builder()
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .nombreJours(nombreJours)
                .motif(dto.getMotif())
                .dateSoumission(LocalDate.now())
                .statut(StatutDemande.EN_ATTENTE)
                .user(user)
                .type(typeConge)
                .build();

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Soumission", "Demande soumise par l'employé.");

        return demande;
    }

    @Override
    @Transactional
    public DemandeConge traiterDemande(Long demandeId, ValidationCongeDTO dto) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getStatut().equals(StatutDemande.EN_ATTENTE)) {
            throw new RuntimeException("La demande n'est plus en attente.");
        }

        demande.setStatut(dto.getStatut());

        if (dto.getStatut().equals(StatutDemande.VALIDEE)) {
            User user = demande.getUser();
            user.setSoldeConges(user.getSoldeConges() - demande.getNombreJours());
            userRepository.save(user);
        }

        demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Traitement RH : " + dto.getStatut().name(), dto.getDetails());

        return demande;
    }

    @Override
    public List<DemandeConge> getMesDemandes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return demandeCongeRepository.findByUser(user);
    }

    @Override
    public List<DemandeConge> getDemandesEnAttente() {
        return demandeCongeRepository.findByStatut(StatutDemande.EN_ATTENTE);
    }

    private int calculerNombreJours(LocalDate debut, LocalDate fin) {
        if (fin.isBefore(debut)) {
            return 0;
        }
         long jours = ChronoUnit.DAYS.between(debut, fin) + 1;
        return (int) jours;
    }

    private void enregistrerHistorique(DemandeConge demande, String action, String details) {
        Historique historique = Historique.builder()
                .action(action)
                .dateAction(LocalDateTime.now())
                .details(details)
                .demandeConge(demande)
                .build();
        historiqueRepository.save(historique);
    }
}