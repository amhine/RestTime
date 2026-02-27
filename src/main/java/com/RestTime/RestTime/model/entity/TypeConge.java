package com.RestTime.RestTime.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
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
