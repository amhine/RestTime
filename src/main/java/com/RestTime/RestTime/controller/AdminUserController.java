package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UpdateRoleRequestDTO;
import com.RestTime.RestTime.dto.UpdateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {
        UserResponseDTO createdUser = utilisateurService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.updateUser(id, request));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.updateUserRole(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        utilisateurService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}