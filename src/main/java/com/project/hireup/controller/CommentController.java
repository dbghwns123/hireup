package com.project.hireup.controller;

import com.project.hireup.dto.CommentRequestDto;
import com.project.hireup.dto.CommentResponseDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @Operation(summary = "댓글 목록 조회", description = "게시글의 댓글 목록을 조회합니다.")
  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<Page<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<CommentResponseDto> comments = commentService.getCommentsByPost(postId, pageable);
    return ResponseEntity.ok(comments);
  }

  @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<String> updateComment(@PathVariable Long commentId,
      @RequestBody @Valid CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    commentService.updateComment(commentId, userDetails.getId(), requestDto);
    return ResponseEntity.ok("댓글을 성공적으로 수정하였습니다.");
  }
}
