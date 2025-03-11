package com.project.hireup.service;

import com.project.hireup.dto.CategoryRequestDto;
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

  // 카테고리 생성 (ROLE_ADMIN)
  public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {

    // 이미 해당 이름을 가진 카테고리가 있는지 확인
    if (categoryRepository.existsByName(requestDto.getName())) {
      throw new HireUpException(ErrorCode.ALREADY_CATEGORY);
    }

    return CategoryResponseDto.fromEntity(categoryRepository.save(Category.builder()
        .name(requestDto.getName())
        .isNotice(requestDto.getIsNotice())
        .build()));
  }

  // 카테고리 수정 (ROLE_ADMIN)
  public void updateCategory(Long id, CategoryRequestDto requestDto) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));

    category.updateCategory(requestDto);
    categoryRepository.save(category);
  }

  // 카테고리 삭제 (ROLE_ADMIN)
  public void deleteCategory(Long id) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));

    categoryRepository.delete(category);

  }
}
