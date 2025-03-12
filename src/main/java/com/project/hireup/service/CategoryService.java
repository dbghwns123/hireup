package com.project.hireup.service;

import com.project.hireup.dto.CategoryRequestDto;
import com.project.hireup.dto.CategoryResponseDto;
import com.project.hireup.entity.Category;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.CategoryRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.type.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final PostRepository postRepository;

  // 모든 카테고리 조회
  public List<CategoryResponseDto> getAllCategories() {

    return categoryRepository.findAll().stream()
        .map(CategoryResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 특정 카테고리 조회
  @Cacheable(value = "category", key = "#id") // 캐싱 적용
  public Category getCategoryById(Long id) {

    return categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));
  }

  // 카테고리 생성 (ROLE_ADMIN)
  @Transactional
  @CacheEvict(value = "category", allEntries = true) // 모든 카테고리 캐시 무효화
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
  @Transactional
  @CachePut(value = "category", key = "#id", unless = "#result == null") // 수정된 카테고리를 즉시 캐싱
  public void updateCategory(Long id, CategoryRequestDto requestDto) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));

    category.updateCategory(requestDto);
    categoryRepository.save(category);
  }

  // 카테고리 삭제 (ROLE_ADMIN)
  @Transactional
  @CacheEvict(value = "category", key = "#id") // 특정 카테고리 캐시 무효화
  public void deleteCategory(Long id) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_CATEGORY));

    // 해당 카테고리를 가진 게시글이 있다면 해당 카테고리 삭제 불가
    if (postRepository.countByCategory(category) > 0) {
      throw new HireUpException(ErrorCode.CAN_NOT_DELETE_CATEGORY);
    }

    categoryRepository.delete(category);
  }
}
