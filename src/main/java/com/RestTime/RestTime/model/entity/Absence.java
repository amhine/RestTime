package com.RestTime.RestTime.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.StatutAbsence;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "absences")
@Builder
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String typeAbsence;
    private String motif;
    private String justificatif;

    @Enumerated(EnumType.STRING)
    private StatutAbsence statut;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}
