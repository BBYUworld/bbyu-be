package com.bbyuworld.gagyebbyu.domain.notification.repository;

import com.bbyuworld.gagyebbyu.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{

}
