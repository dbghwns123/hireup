package com.project.hireup.controller;

import com.project.hireup.dto.NotificationResponseDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "사용자 알림 조회", description = "사용자의 알림 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<Page<NotificationResponseDto>> getNotifications(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    // 사용자 ID를 기반으로 알림 조회
    Page<NotificationResponseDto> notifications = notificationService.getNotificationsByUser(userDetails.getId(), pageable);
    return ResponseEntity.ok(notifications);
  }
}
