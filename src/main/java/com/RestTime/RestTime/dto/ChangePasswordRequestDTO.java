package com.RestTime.RestTime.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String ancienMotDePasse;
    private String nouveauMotDePasse;
}