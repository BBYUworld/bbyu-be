package com.bbyuworld.gagyebbyu.domain.notification.repository;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import com.bbyuworld.gagyebbyu.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {
    Notification findNotificationById(Long id);
    List<Notification> findAllUserNotifications(Long userId);
    void deleteNotification(Long notificationId);
}
