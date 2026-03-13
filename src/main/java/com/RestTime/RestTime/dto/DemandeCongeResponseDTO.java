package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.model.enumeration.TypeConge;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DemandeCongeResponseDTO {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreJours;
    private String motif;
    private LocalDate dateSoumission;
    private StatutDemande statut;
    private TypeConge type;

    private Long userId;
    private String nom;
    private String prenom;
}
