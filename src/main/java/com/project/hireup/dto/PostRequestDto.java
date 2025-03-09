package com.project.hireup.dto;

import com.project.hireup.type.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
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
public class PostRequestDto {

  @NotBlank(message = "제목은 필수 입력 항목입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력 항목입니다.")
  private String content;

  @NotNull(message = "카테고리는 필수 입력 항목입니다.")
  private Long categoryId;

  private Set<String> hashtags; // 해시태그 추가

  @NotNull(message = "게시글 상태는 필수 입력 항목입니다.")
  private PostStatus status; // 게시글 상태 추가 (Enum)
}
