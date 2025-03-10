package com.project.hireup.controller;

import com.project.hireup.dto.CategoryRequestDto;
import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  // 특정 카테고리 조회
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
    CategoryResponseDto category = categoryService.getCategoryById(id);
    return ResponseEntity.ok(category);
  }

  // ROLE_ADMIN만 카테고리 생성 가능
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
    CategoryResponseDto category = categoryService.createCategory(requestDto);
    return ResponseEntity.ok("카테고리가 정상적으로 생성되었습니다.");
  }


}
