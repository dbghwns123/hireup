package com.project.hireup.dto;

import com.project.hireup.entity.Comment;
import java.time.LocalDateTime;
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
public class CommentResponseDto {

  private Long id;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 작성자 정보
  private UserResponseDto user;

  // 게시글 ID
  private Long postId;

  public static CommentResponseDto fromEntity(Comment comment) {

    return CommentResponseDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .postId(comment.getPost().getId())
        .user(UserResponseDto.fromEntity(comment.getUser()))
        .build();
  }

}
