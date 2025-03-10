package com.project.hireup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoryRequestDto {

  @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
  private String name;

  @NotNull(message = "공지사항 여부는 필수 입력 항목입니다.")
  private Boolean isNotice;

}
