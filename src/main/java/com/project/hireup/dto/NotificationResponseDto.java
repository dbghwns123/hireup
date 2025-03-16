package com.project.hireup.dto;

import com.project.hireup.entity.Notification;
import com.project.hireup.type.NotificationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

  private Long id;
  private NotificationType type;
  private LocalDateTime createdAt;

  public static NotificationResponseDto fromEntity(Notification notification) {
    return NotificationResponseDto.builder()
        .id(notification.getId())
        .type(notification.getType())
        .createdAt(notification.getCreatedAt())
        .build();
  }

}
