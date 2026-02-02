package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.RestTime.RestTime.model.enumeration.StatutDemande;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
