package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.service.CongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rh/conges")
@RequiredArgsConstructor
public class RhCongeController {

    private final CongeService congeService;

    @GetMapping("/enattente")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getDemandesEnAttente() {
        return ResponseEntity.ok(congeService.getDemandesEnAttente());
    }

    @PutMapping("/{demandeId}/traiter")
    public ResponseEntity<DemandeCongeResponseDTO> traiterDemande(
            @PathVariable Long demandeId,
            @RequestBody ValidationCongeDTO dto) {
        return ResponseEntity.ok(congeService.traiterDemande(demandeId, dto));
    }
}