package com.project.hireup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CommentRequestDto {

  @NotBlank(message = "댓글 내용은 필수입니다.")
  @Size(max = 500, message = "댓글은 최대 500자까지 작성 가능합니다.")
  private String content;

}
