package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String titre;
    private String message;
    private LocalDateTime dateEnvoi;
    private TypeNotification type;
    private boolean lue;
    private Long userId;
}