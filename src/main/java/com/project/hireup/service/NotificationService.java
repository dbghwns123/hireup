package com.project.hireup.service;

import com.project.hireup.entity.Notification;
import com.project.hireup.entity.User;
import com.project.hireup.repository.NotificationRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.NotificationType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  // 알림 내역 저장
  @Transactional
  public void createNotification(User user, NotificationType type) {
    Notification notification = Notification.builder()
        .user(user)
        .type(type)
        .createdAt(LocalDateTime.now())
        .build();

    notificationRepository.save(notification);
  }
}
