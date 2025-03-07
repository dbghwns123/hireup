package com.project.hireup.controller;

import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  // 팔로우 취소
  @Operation(summary = "팔로우 취소", description = "특정 사용자를 언팔로우합니다.")
  @DeleteMapping("/{followingId}")
  public ResponseEntity<String> unfollowUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long followingId) {

    followService.unfollowUser(userDetails.getId(), followingId);

    return ResponseEntity.ok("언팔로우 성공");
  }

  // 팔로잉 목록 조회 (내가 팔로우한 사람들)
  @Operation(summary = "팔로잉 목록 조회", description = "내가 팔로우하고 있는 사용자 목록을 조회합니다.")
  @GetMapping("/following")
  public ResponseEntity<List<UserResponseDto>> getFollowingList(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(followService.getFollowingList(userDetails.getId()));
  }

  // 팔로워 목록 조회 (나를 팔로우한 사람들)
  @Operation(summary = "팔로워 목록 조회", description = "나를 팔로우하는 사용자 목록을 조회합니다.")
  @GetMapping("/followers")
  public ResponseEntity<List<UserResponseDto>> getFollowerList(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(followService.getFollowerList(userDetails.getId()));
  }

}
