package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SoldeCongeDTO {
    private Long userId;
    private String nom;
    private String prenom;
    private Double soldeConges;
}