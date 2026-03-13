package com.RestTime.RestTime.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String token;
    private String nouveauMotDePasse;
}
