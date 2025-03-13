package com.project.hireup.controller;

import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
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
@RequestMapping("/api")
public class LikeController {

  private final LikeService likeService;

  @Operation(summary = "좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
  @PostMapping("/posts/{postId}/likes")
  public ResponseEntity<String> addLike(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    likeService.addLike(postId, userDetails.getId());
    return ResponseEntity.ok("좋아요를 눌렀습니다!");
  }

  @Operation(summary = "좋아요 취소", description = "게시글에 추가한 좋아요를 취소합니다.")
  @DeleteMapping("/posts/{postId}/likes")
  public ResponseEntity<String> removeLike(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    likeService.removeLike(postId, userDetails.getId());
    return ResponseEntity.ok("좋아요를 취소했습니다.");
  }

  @Operation(summary = "좋아요 수 조회", description = "게시글의 좋아요 수를 조회합니다.")
  @GetMapping("/posts/{postId}/likes/count")
  public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long postId) {
    Long count = likeService.getLikeCount(postId);
    return ResponseEntity.ok(Map.of("likeCount", count));
  }

}
