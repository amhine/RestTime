package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;

import java.util.List;

public interface CongeService {
    DemandeConge soumettreDemande(Long userId, DemandeCongeCreateDTO dto);
    List<DemandeConge> getMesDemandes(Long userId);

    List<DemandeConge> getDemandesEnAttente();
    DemandeConge traiterDemande(Long demandeId, ValidationCongeDTO dto);
}
