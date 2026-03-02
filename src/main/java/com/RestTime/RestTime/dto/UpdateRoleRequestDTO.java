package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.Role;
import lombok.Data;

@Data
public class UpdateRoleRequestDTO {
    private Role role;
}