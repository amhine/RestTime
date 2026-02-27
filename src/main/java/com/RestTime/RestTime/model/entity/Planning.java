package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "planing")
@Builder
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String periode;

    @OneToMany (mappedBy = "planning")
    private List<DemandeConge> demandes;

    @OneToMany(mappedBy = "planning")
    private List<JourFerie> joursFeries;
}
