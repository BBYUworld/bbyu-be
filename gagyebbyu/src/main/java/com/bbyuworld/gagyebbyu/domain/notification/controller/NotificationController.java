package com.bbyuworld.gagyebbyu.domain.notification.controller;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import com.bbyuworld.gagyebbyu.domain.notification.service.NotificationService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotificationController {
    private final NotificationService notificationService;


    @PostMapping
    @RequireJwtToken
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, String> map) {
        System.out.println("call sendNotificaiton");
        Long senderId = UserContext.getUserId();
        Long receiverId= Long.parseLong(map.get("receiverId"));
        notificationService.sendNotification(senderId, receiverId);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @GetMapping
    @RequireJwtToken
    public ResponseEntity<List<NotificationDto>> findAllUserNotification(){
        Long userId = UserContext.getUserId();
        return ResponseEntity.ok(notificationService.findAllUserNotification(userId));
    }

    @DeleteMapping
    @RequireJwtToken
    public ResponseEntity<String> deleteUserNotification(@RequestBody Map<String, String> map) {
        Long notificationId = Long.parseLong(map.get("notificationId"));
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }

    @PutMapping
    @RequireJwtToken
    public ResponseEntity<String> updateUserNotification(@RequestBody Map<String, String> map) {
        Long notificationId = Long.parseLong(map.get("notificationId"));
        notificationService.updateNotification(notificationId);
        return ResponseEntity.ok("Notification updated successfully");
    }

    @GetMapping("/count")
    @RequireJwtToken
    public ResponseEntity<Long> findAllUnreadNotificationCount(){
        Long userId = UserContext.getUserId();
        return ResponseEntity.ok(notificationService.findAllUnreadNotificationCount(userId));
    }



}
