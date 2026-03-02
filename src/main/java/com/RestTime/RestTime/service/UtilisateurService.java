package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UpdateRoleRequestDTO;
import com.RestTime.RestTime.dto.UpdateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;

import java.util.List;

public interface UtilisateurService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(CreateUserRequestDTO request);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request);
    UserResponseDTO updateUserRole(Long id, UpdateRoleRequestDTO request);
    void deleteUser(Long id);
}