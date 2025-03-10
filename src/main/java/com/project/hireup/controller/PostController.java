package com.project.hireup.controller;

import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.dto.PostResponseDto;
import com.project.hireup.entity.Post;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
  @PostMapping
  public ResponseEntity<String> createPost(@RequestBody @Valid PostRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    postService.createPost(requestDto, userDetails.getId());

    return ResponseEntity.ok("게시글이 성공적으로 생성되었습니다.");
  }

  @Operation(summary = "특정 게시글 조회", description = "게시글 ID를 통해 특정 게시글을 조회합니다.")
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(postService.getPostById(postId, userDetails.getId()));
  }

  @Operation(summary = "특정 카테고리의 게시글 목록 조회", description = "특정 카테고리에 속한 게시글 목록을 페이징 처리하여 조회합니다.")
  @GetMapping("/category/{categoryId}")
  public ResponseEntity<Page<PostResponseDto>> getPostByCategory(@PathVariable Long categoryId,
      @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {

    return ResponseEntity.ok(
        postService.getPostByCategory(categoryId, userDetails.getId(), pageable));
  }

  @Operation(summary = "전체 게시글 목록 조회", description = "모든 게시글을 페이징 처리하여 조회합니다.")
  @GetMapping
  public ResponseEntity<Page<PostResponseDto>> getAllPosts(
      @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {

    return ResponseEntity.ok(postService.getAllPosts(userDetails.getId(), pageable));

  }

  @Operation(summary = "게시글 수정", description = "게시글 ID를 통해 특정 게시글을 수정합니다.")
  @PutMapping("/{postId}")
  public ResponseEntity<String> updatePost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid PostRequestDto requestDto) {

    postService.updatePost(postId, userDetails.getId(), requestDto);

    return ResponseEntity.ok("게시글이 수정이 성공적으로 완료되었습니다.");
  }

  @Operation(summary = "게시글 삭제", description = "게시글 ID를 통해 특정 게시글을 삭제합니다.")
  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    postService.deletePost(postId, userDetails.getId());

    return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
  }
}
