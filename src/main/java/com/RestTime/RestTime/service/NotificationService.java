package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationResponseDTO> getMesNotifications();

    List<NotificationResponseDTO> getMesNotificationsNonLues();

    NotificationResponseDTO marquerCommeLue(Long notificationId);

}