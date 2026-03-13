package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JustificatifResponseDTO {
    private Long demandeId;
    private String nomFichier;
    private String cheminFichier;
    private String message;
}