package com.RestTime.RestTime.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private Double soldeConges;
}