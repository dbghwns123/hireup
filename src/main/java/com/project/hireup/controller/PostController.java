package com.project.hireup.controller;

import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.entity.Post;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  // 게시글 생성
  @PostMapping
  public ResponseEntity<String> createPost(@RequestBody @Valid PostRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    postService.createPost(requestDto, userDetails.getId());

    return ResponseEntity.ok("게시글이 성공적으로 생성되었습니다.");
  }

  // 특정 게시글 조회
  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPostById(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(postService.getPostById(postId, userDetails.getId()));
  }

  // 특정 카테고리의 게시글 목록 조회(페이징 처리)
  @GetMapping("/category/{categoryId}")
  public ResponseEntity<Page<Post>> getPostByCategory(@PathVariable Long categoryId,
      @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {

    return ResponseEntity.ok(
        postService.getPostByCategory(categoryId, userDetails.getId(), pageable));
  }
}
