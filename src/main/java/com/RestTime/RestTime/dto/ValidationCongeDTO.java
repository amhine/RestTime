package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.StatutDemande;
import lombok.Data;

@Data
public class ValidationCongeDTO {
    private StatutDemande statut;
    private String details;
}