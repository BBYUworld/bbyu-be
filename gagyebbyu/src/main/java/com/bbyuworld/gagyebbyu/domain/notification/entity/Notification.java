package com.bbyuworld.gagyebbyu.domain.notification.entity;

import com.bbyuworld.gagyebbyu.domain.notification.dto.NotificationDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    private String type;

    private String message;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public NotificationDto toDto() {
        return NotificationDto.builder()
                .notificationId(this.notificationId)
                .senderId(this.senderId)
                .receiverId(this.receiverId)
                .type(this.type)
                .message(this.message)
                .createdAt(this.createdAt)
                .build();
    }

    // Create Entity from DTO
    public static Notification fromDto(NotificationDto dto) {
        return Notification.builder()
                .notificationId(dto.getNotificationId())
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .type(dto.getType())
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    // Set properties of the entity from DTO
    public void setProperties(NotificationDto dto) {
        this.notificationId = dto.getNotificationId();
        this.senderId = dto.getSenderId();
        this.receiverId = dto.getReceiverId();
        this.type = dto.getType();
        this.message = dto.getMessage();
        this.createdAt = dto.getCreatedAt();
    }
}
