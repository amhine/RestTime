package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.TypeNotification;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String message;
    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
    private TypeNotification type;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}
