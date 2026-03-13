package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.TypeConge;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandeCongeCreateDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private TypeConge conge;}