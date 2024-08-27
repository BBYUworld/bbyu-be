package com.bbyuworld.gagyebbyu.domain.notification.repository;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import com.bbyuworld.gagyebbyu.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findAllUserNotifications(Long userId);
}
