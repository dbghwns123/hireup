package com.project.hireup.controller;

import com.project.hireup.entity.PostDocument;
import com.project.hireup.service.PostSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/search")
@RequiredArgsConstructor
public class PostSearchController {

  private final PostSearchService postSearchService;

  @Operation(summary = "제목으로 게시글 검색", description = "제목을 통해 게시글을 검색합니다.")
  @GetMapping("/title")
  public ResponseEntity<Page<PostDocument>> searchByTitle(@RequestParam String title,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<PostDocument> posts = postSearchService.searchByTitle(title, pageable);
    return ResponseEntity.ok(posts);
  }

  @Operation(summary = "해시태그로 게시글 검색", description = "해시태그를 통해 게시글을 검색합니다.")
  @GetMapping("/hashtag")
  public ResponseEntity<Page<PostDocument>> searchByHashtag(@RequestParam String hashtag,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<PostDocument> posts = postSearchService.searchByHashtag(hashtag, pageable);
    return ResponseEntity.ok(posts);
  }
}
