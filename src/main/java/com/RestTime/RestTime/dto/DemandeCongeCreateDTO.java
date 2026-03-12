package com.RestTime.RestTime.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandeCongeCreateDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private Long typeCongeId;
}