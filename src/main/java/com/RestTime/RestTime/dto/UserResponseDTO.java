package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Double soldeConges;
    private Role role;
    private String nomService;
}