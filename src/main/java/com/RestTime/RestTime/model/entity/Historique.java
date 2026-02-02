package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime dateAction;
    private String details;

    @ManyToOne
    @JoinColumn(name = "demande_conge_id")
    private DemandeConge demandeConge;
}
