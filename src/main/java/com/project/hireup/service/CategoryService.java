package com.project.hireup.service;

import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.entity.Category;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.CategoryRepository;
import com.project.hireup.type.ErrorCode;
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

    return categoryRepository.findAll().stream()
        .map(CategoryResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 특정 카테고리 조회
  public CategoryResponseDto getCategoryById(Long id) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));

    return CategoryResponseDto.fromEntity(category);
  }


}
