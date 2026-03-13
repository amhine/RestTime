package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.JustificatifResponseDTO;
import com.RestTime.RestTime.dto.SoldeCongeDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeService {

    SoldeCongeDTO getSoldeConges(Long userId);

    void annulerDemande(Long demandeId, Long userId);

    JustificatifResponseDTO uploadJustificatif(Long demandeId, Long userId, MultipartFile fichier);
}