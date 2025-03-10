package com.project.hireup.service;

import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.entity.Category;
import com.project.hireup.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  // 모든 카테고리 조회
  public List<CategoryResponseDto> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .map(CategoryResponseDto::fromEntity)
        .collect(Collectors.toList());
  }
}
