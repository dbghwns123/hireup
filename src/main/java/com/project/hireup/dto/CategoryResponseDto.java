package com.project.hireup.dto;

import com.project.hireup.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

  private Long id;
  private String name;
  private boolean isNotice;

  public static CategoryResponseDto fromEntity(Category category) {
    return new CategoryResponseDto(
        category.getId(),
        category.getName(),
        category.isNotice()
    );
  }

}
