package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.mapper.DemandeCongeMapper;
import com.RestTime.RestTime.mapper.HistoriqueMapper;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.Historique;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.repository.AbsenceRepository;
import com.RestTime.RestTime.repository.DemandeCongeRepository;
import com.RestTime.RestTime.repository.HistoriqueRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.CongeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CongeServiceImpl implements CongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UserRepository userRepository;
    private final HistoriqueRepository historiqueRepository;
    private final DemandeCongeMapper demandeCongeMapper;
    private final AbsenceRepository absenceRepository;
    private final HistoriqueMapper historiqueMapper;

    @Override
    @Transactional
    public DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        int nombreJours = calculerNombreJours(dto.getDateDebut(), dto.getDateFin());

        if (nombreJours <= 0) {
            throw new RuntimeException("Dates invalides : la date de fin doit être égale ou postérieure à la date de début.");
        }

        if (user.getSoldeConges() < nombreJours) {
            throw new RuntimeException("Solde de congés insuffisant.");
        }

        DemandeConge demande = demandeCongeMapper.toEntity(dto);
        demande.setNombreJours(nombreJours);
        demande.setDateSoumission(LocalDate.now());
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setUser(user);
        demande.setType(dto.getConge());

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Soumission", "Demande soumise par l'employé.");

        return demandeCongeMapper.toResponseDTO(demande);
    }

    @Override
    @Transactional
    public DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto) {
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

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Traitement RH : " + dto.getStatut().name(), dto.getDetails());

        return demandeCongeMapper.toResponseDTO(demande);
    }
    @Override
    public List<DemandeCongeResponseDTO> getMesDemandes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return demandeCongeRepository.findByUser(user)
                .stream()
                .map(demandeCongeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DemandeCongeResponseDTO> getDemandesEnAttente() {
        return demandeCongeRepository.findByStatut(StatutDemande.EN_ATTENTE)
                .stream()
                .map(demandeCongeMapper::toResponseDTO)
                .toList();
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
    @Override
    public DemandeCongeResponseDTO getDemandeById(Long id) {
        DemandeConge demande = demandeCongeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'id : " + id));
        return demandeCongeMapper.toResponseDTO(demande);
    }

    @Override
    public StatistiquesRhDTO getStatistiques() {
        long total      = demandeCongeRepository.count();
        long approuvees = demandeCongeRepository.countByStatut(StatutDemande.VALIDEE);
        long refusees   = demandeCongeRepository.countByStatut(StatutDemande.REFUSEE);
        long enAttente  = demandeCongeRepository.countByStatut(StatutDemande.EN_ATTENTE);
        long absences   = absenceRepository.count();
        long employes   = userRepository.count();

        return StatistiquesRhDTO.builder()
                .totalDemandes(total)
                .demandesApprouvees(approuvees)
                .demandesRefusees(refusees)
                .demandesEnAttente(enAttente)
                .totalAbsences(absences)
                .tauxApprobation(total > 0 ? (approuvees * 100.0 / total) : 0)
                .tauxAbsenteisme(employes > 0 ? (absences * 100.0 / employes) : 0)
                .build();
    }

    @Override
    public List<HistoriqueResponseDTO> getHistoriqueGlobal() {
        return historiqueRepository.findAll().stream()
                .map(historiqueMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}