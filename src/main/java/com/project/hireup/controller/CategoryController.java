package com.project.hireup.controller;

import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  // 모든 카테고리 조회
  @GetMapping
  public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    List<CategoryResponseDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

}
