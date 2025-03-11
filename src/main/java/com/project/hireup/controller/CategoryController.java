package com.project.hireup.controller;

import com.project.hireup.dto.CategoryRequestDto;
import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

    List<CategoryResponseDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @Operation(summary = "특정 카테고리 조회", description = "카테고리 ID를 통해 특정 카테고리를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {

    CategoryResponseDto category = categoryService.getCategoryById(id);
    return ResponseEntity.ok(category);
  }

  @Operation(summary = "카테고리 생성 (관리자 전용)", description = "새로운 카테고리를 생성합니다. 관리자 권한이 필요합니다.")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {

    CategoryResponseDto category = categoryService.createCategory(requestDto);
    return ResponseEntity.ok("카테고리가 정상적으로 생성되었습니다.");
  }

  @Operation(summary = "카테고리 수정 (관리자 전용)", description = "카테고리 ID를 통해 특정 카테고리를 수정합니다. 관리자 권한이 필요합니다.")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<String> updateCategory(@PathVariable Long id,
      @RequestBody CategoryRequestDto requestDto) {

    categoryService.updateCategory(id, requestDto);
    return ResponseEntity.ok("카테고리 정상적으로 수정되었습니다.");
  }

  @Operation(summary = "카테고리 삭제 (관리자 전용)", description = "카테고리 ID를 통해 특정 카테고리를 삭제합니다. 관리자 권한이 필요합니다.")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) {

    categoryService.deleteCategory(id);
    return ResponseEntity.ok("카테고리가 정삭적으로 삭제되었습니다.");
  }

}
