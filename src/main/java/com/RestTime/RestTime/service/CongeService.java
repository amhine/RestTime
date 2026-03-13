package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.*;

import java.util.List;

public interface CongeService {
    DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto);
    DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto);

    List<DemandeCongeResponseDTO> getMesDemandes(Long userId);
    List<DemandeCongeResponseDTO> getDemandesEnAttente();

    DemandeCongeResponseDTO getDemandeById(Long id);
    StatistiquesRhDTO getStatistiques();
    List<HistoriqueResponseDTO> getHistoriqueGlobal();
}