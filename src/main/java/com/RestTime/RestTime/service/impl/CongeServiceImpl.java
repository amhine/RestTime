package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.mapper.DemandeCongeMapper;
import com.RestTime.RestTime.mapper.HistoriqueMapper;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.Historique;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.Role;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.model.enumeration.TypeNotification;
import com.RestTime.RestTime.repository.*;
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
    private final NotificationRepository notificationRepository;


    @Override
    @Transactional
    public DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        LocalDate today = LocalDate.now();

        if (dto.getDateDebut().isBefore(today)) {
            throw new RuntimeException("Date de début ne peut pas être dans le passé");
        }

        int nombreJours = calculerNombreJours(dto.getDateDebut(), dto.getDateFin());

        if (nombreJours <= 0) {
            throw new RuntimeException("Dates invalides");
        }

        boolean hasPending = demandeCongeRepository
                .existsByUserAndStatut(user, StatutDemande.EN_ATTENTE);

        if (hasPending) {
            throw new RuntimeException("Vous avez déjà une demande en attente");
        }

        boolean overlap = demandeCongeRepository
                .existsByUserAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                        user,
                        dto.getDateFin(),
                        dto.getDateDebut()
                );

        if (overlap) {
            throw new RuntimeException("Dates chevauchent un congé existant");
        }


        switch (dto.getConge()) {

            case ANNUEL:

                if (nombreJours > 15) {
                    throw new RuntimeException("Congé annuel max 15 jours");
                }

                if (user.getSoldeConges() < nombreJours) {
                    throw new RuntimeException("Solde insuffisant");
                }
                break;

            case MALADIE:

                if (nombreJours > 30) {
                    throw new RuntimeException("Congé maladie max 30 jours");
                }
                break;

            case EXCEPTIONNEL:

                if (nombreJours > 5) {
                    throw new RuntimeException("Congé exceptionnel max 5 jours");
                }
                break;

            case FORMATION:

                if (nombreJours > 30) {
                    throw new RuntimeException("Congé formation max 30 jours");
                }
                break;
        }


        DemandeConge demande = demandeCongeMapper.toEntity(dto);

        demande.setNombreJours(nombreJours);
        demande.setDateSoumission(today);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setUser(user);
        demande.setType(dto.getConge());

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Soumission", "Demande soumise par l'employé");
        Notification notification = Notification.builder()
                .user(demande.getUser())
                .titre("Demande soumise")
                .message("Vous avez soumis une nouvelle demande de congé.")
                .type(TypeNotification.DEMANDE_SOUMISE)
                .dateEnvoi(LocalDateTime.now())
                .lue(false)
                .build();
        List<User> rhs = userRepository.findByRole(Role.RH);

        for (User rh : rhs) {
            Notification notificationRH = Notification.builder()
                    .user(rh)
                    .titre("Nouvelle demande de congé")
                    .message("🆕 " + user.getNom() + " " + user.getPrenom()
                            + " a soumis une demande de congé.")
                    .type(TypeNotification.DEMANDE_SOUMISE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();

            notificationRepository.save(notificationRH);
        }
        notificationRepository.save(notification);
        return demandeCongeMapper.toResponseDTO(demande);

    }


    @Override
    @Transactional
    public DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("La demande n'est plus en attente.");
        }

        if (dto.getStatut() != StatutDemande.VALIDEE && dto.getStatut() != StatutDemande.REFUSEE) {
            throw new RuntimeException("Statut invalide pour traitement RH");
        }

        demande.setStatut(dto.getStatut());

        if (dto.getStatut() == StatutDemande.VALIDEE) {
            User user = demande.getUser();
            double nouveauSolde = user.getSoldeConges() - demande.getNombreJours();
            if (nouveauSolde < 0) {
                throw new RuntimeException("Solde insuffisant");
            }
            user.setSoldeConges(nouveauSolde);
            userRepository.save(user);

            Notification notification = Notification.builder()
                    .user(user)
                    .titre("Demande validée")
                    .message("Votre demande de congé a été validée.")
                    .type(TypeNotification.DEMANDE_VALIDEE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();
            notificationRepository.save(notification);
        }

        if (dto.getStatut() == StatutDemande.REFUSEE) {
            User user = demande.getUser();

            Notification notification = Notification.builder()
                    .user(user)
                    .titre("Demande refusée")
                    .message("Votre demande de congé a été refusée.")
                    .type(TypeNotification.DEMANDE_REFUSEE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();
            notificationRepository.save(notification);
        }

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(
                demande,
                "Traitement RH : " + dto.getStatut().name(),
                dto.getDetails()
        );

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