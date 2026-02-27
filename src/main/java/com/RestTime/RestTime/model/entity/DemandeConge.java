package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.StatutDemande;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "demandeconge")
@Builder
public class DemandeConge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreJours;
    private String motif;
    private LocalDate dateSoumission;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "type_conge_id")
    private TypeConge typeConge;

    @OneToMany(mappedBy = "demandeConge")
    private List<Historique> historiques;

    @ManyToOne
    @JoinColumn(name = "planning_id")
    private Planning planning;
}
