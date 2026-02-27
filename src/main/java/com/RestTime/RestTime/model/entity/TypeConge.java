package com.RestTime.RestTime.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "typecongee")
@Builder
public class TypeConge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;
    private String description;
    private Integer nombreJoursMax;

    @OneToMany(mappedBy = "typeConge", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DemandeConge> demandes;
}
