package com.project.hireup.controller;

import com.project.hireup.dto.CommentRequestDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<String> createComment(@PathVariable Long postId,
      @RequestBody @Valid CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    commentService.createComment(postId, userDetails.getId(), requestDto);
    return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
  }
}
