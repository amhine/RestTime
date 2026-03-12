package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.Role;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {
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


    @OneToMany(mappedBy = "user")
    private List<DemandeConge> demandes;

    @OneToMany(mappedBy = "user")
    private List<Absence> absences;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;
}
