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
    public Notification findNotificationById(Long id) {
        return queryFactory.selectFrom(notification)
                .where(notification.notificationId.eq(id))
                .fetchOne();
    }

    @Override
    public List<Notification> findAllUserNotifications(Long userId) {
        return queryFactory.selectFrom(notification)
                .where(notification.receiverId.eq(userId))
                .fetch();
    }

    @Override
    public void deleteNotification(Long notificationId) {
        queryFactory.delete(notification).where(notification.notificationId.eq(notificationId));
    }

    @Override
    public Long findAllUnreadNotificationCount(Long userId) {
        return (long) queryFactory.selectFrom(notification)
                .where(notification.receiverId.eq(userId).and(
                        notification.isRead.eq(false)
                ))
                .fetch().size();
    }


}
