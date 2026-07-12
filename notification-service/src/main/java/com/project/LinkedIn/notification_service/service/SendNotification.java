package com.project.LinkedIn.notification_service.service;

import com.project.LinkedIn.notification_service.entity.Notification;
import com.project.LinkedIn.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotification {

    private final NotificationRepository notificationRepository;
    public void notify(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);

        notificationRepository.save(notification);
    }
}
