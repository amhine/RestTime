package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.Role;
import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private Double soldeConges;
    private Role role;
    private Long serviceId;
}