package com.bbyuworld.gagyebbyu.domain.notification.service;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import com.bbyuworld.gagyebbyu.domain.notification.entity.Notification;
import com.bbyuworld.gagyebbyu.domain.notification.repository.NotificationRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    public boolean sendNotification(Long senderId, Long receiverId){
        String type = "connect_couple";
        User sender = userRepository.findUserById(senderId);
        User receiver = userRepository.findUserById(receiverId);
        String message = sender.getName()+"님께서 커플 연결 요청을 보내셨습니다.";
        Notification notification = Notification.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .type(type)
                .message(message)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);
        return true;
    }

    public List<NotificationDto> findAllUserNotification(Long userId){
        List<NotificationDto> dtoList = new ArrayList<>();
        List<Notification> resultList = notificationRepository.findAllUserNotifications(userId);
        for(Notification noti : resultList){
            NotificationDto dto = noti.toDto();
            dtoList.add(dto);
        }
        return dtoList;
    }
}
