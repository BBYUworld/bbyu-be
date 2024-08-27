package com.bbyuworld.gagyebbyu.domain.notification.repository;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import com.bbyuworld.gagyebbyu.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bbyuworld.gagyebbyu.domain.notification.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findAllUserNotifications(Long userId) {
        return queryFactory.selectFrom(notification)
                .where(notification.receiverId.eq(userId))
                .fetch();
    }
}
