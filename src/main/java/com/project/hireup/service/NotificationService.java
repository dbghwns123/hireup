package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;

import com.project.hireup.dto.NotificationResponseDto;
import com.project.hireup.entity.Notification;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.NotificationRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.NotificationType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  // 알림 내역 조회
  @Transactional(readOnly = true)
  public Page<NotificationResponseDto> getNotificationsByUser(Long userId, Pageable pageable) {
    // 사용자 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 사용자 알림 조회 (최신순 정렬)

    // 엔티티를 DTO로 변환하여 반환
    return notificationRepository.findAllByUser(user, pageable)
        .map(NotificationResponseDto::fromEntity);
  }
}
