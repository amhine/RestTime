package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import com.RestTime.RestTime.mapper.NotificationMapper;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.NotificationRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;


    @Override
    public List<NotificationResponseDTO> getNotificationsNonLues(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return notificationRepository.findByUserAndLueFalse(user)
                .stream()
                .map(notificationMapper::toResponseDTO)
                .toList();
    }


    @Override
    public NotificationResponseDTO marquerCommeLue(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette notification.");
        }

        notification.setLue(true);
        notificationRepository.save(notification);

        return notificationMapper.toResponseDTO(notification);
    }
}