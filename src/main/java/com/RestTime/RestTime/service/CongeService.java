package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;

import java.util.List;

public interface CongeService {
    DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto);

    List<DemandeCongeResponseDTO> getMesDemandes(Long userId);
    List<DemandeCongeResponseDTO> getDemandesEnAttente();

    DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto);
}