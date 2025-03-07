package com.project.hireup.controller;

import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

  private final FollowService followService;

  // 팔로우 추가
  @Operation(summary = "팔로우 추가", description = "특정 사용자를 팔로우합니다.")
  @PostMapping("/{followingId}")
  public ResponseEntity<String> followUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long followingId) {

    followService.followUser(userDetails.getId(), followingId);

    return ResponseEntity.ok("팔로우 성공");
  }

}
