package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.dto.JustificatifResponseDTO;
import com.RestTime.RestTime.dto.SoldeCongeDTO;
import com.RestTime.RestTime.service.CongeService;
import com.RestTime.RestTime.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;


    @GetMapping("/{userId}/solde")
    public ResponseEntity<SoldeCongeDTO> getSoldeConges(@PathVariable Long userId) {
        return ResponseEntity.ok(employeService.getSoldeConges(userId));
    }


    @DeleteMapping("/{userId}/demandes/{demandeId}")
    public ResponseEntity<Void> annulerDemande(
            @PathVariable Long userId,
            @PathVariable Long demandeId) {
        employeService.annulerDemande(demandeId, userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{userId}/demandes/{demandeId}/justificatif")
    public ResponseEntity<JustificatifResponseDTO> uploadJustificatif(
            @PathVariable Long userId,
            @PathVariable Long demandeId,
            @RequestParam("fichier") MultipartFile fichier) {
        return ResponseEntity.ok(employeService.uploadJustificatif(demandeId, userId, fichier));
    }

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