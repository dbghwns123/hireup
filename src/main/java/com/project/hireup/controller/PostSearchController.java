package com.project.hireup.controller;

import com.project.hireup.entity.PostDocument;
import com.project.hireup.service.PostSearchService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<List<PostDocument>> searchByTitle(@RequestParam String title) {
    List<PostDocument> posts = postSearchService.searchByTitle(title);
    return ResponseEntity.ok(posts);
  }

}
