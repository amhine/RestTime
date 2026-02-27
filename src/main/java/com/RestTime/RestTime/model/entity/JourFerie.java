package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jourferie")
@Builder
public class JourFerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "planning_id")
    private Planning planning;
}
