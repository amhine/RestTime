package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import com.RestTime.RestTime.mapper.NotificationMapper;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.NotificationRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    private User getUserConnecte() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Override
    public List<NotificationResponseDTO> getMesNotifications() {

        User user = getUserConnecte();
        return notificationRepository
                .findByUserOrderByDateEnvoiDesc(user)
                .stream()
                .map(notificationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getMesNotificationsNonLues() {

        User user = getUserConnecte();
        return notificationRepository
                .findByUserAndLueFalseOrderByDateEnvoiDesc(user)
                .stream()
                .map(notificationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public NotificationResponseDTO marquerCommeLue(Long notificationId) {

        User user = getUserConnecte();
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Accès refusé ❌");
        }

        notification.setLue(true);
        notificationRepository.save(notification);

        return notificationMapper.toResponseDTO(notification);
    }
}