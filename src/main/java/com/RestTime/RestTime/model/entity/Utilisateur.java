package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.RestTime.RestTime.model.enumeration.Role;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String motDePasse;
    private Double soldeConges;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToMany(mappedBy = "utilisateur")
    private List<DemandeConge> demandes;

    @OneToMany(mappedBy = "utilisateur")
    private List<Absence> absences;

    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notifications;
}
