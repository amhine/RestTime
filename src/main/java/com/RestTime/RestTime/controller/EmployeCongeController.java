package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.service.CongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employe/conges")
@RequiredArgsConstructor
public class EmployeCongeController {

    private final CongeService congeService;

    @PostMapping("/{userId}/soumettre")
    public ResponseEntity<DemandeCongeResponseDTO> soumettre(@PathVariable Long userId, @RequestBody DemandeCongeCreateDTO dto) {
        return ResponseEntity.ok(congeService.soumettreDemande(userId, dto));
    }

    @GetMapping("/{userId}/mesdemandes")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getMesDemandes(@PathVariable Long userId) {
        return ResponseEntity.ok(congeService.getMesDemandes(userId));
    }
}