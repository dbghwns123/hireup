package com.project.hireup.dto;

import com.project.hireup.entity.Post;
import java.time.LocalDateTime;
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
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private Set<String> hashtags;
  private int viewCount;
  private LocalDateTime createdAt;
  private String categoryName;
//  private List<Comment> comments;
//  private List<Like> likes;
//  private List<Report> reports;
//  private List<PostImage> images = new ArrayList<>();

  public static PostResponseDto fromEntity(Post post) {
    return PostResponseDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .hashtags(post.getHashtags())
        .viewCount(post.getViewCount())
        .createdAt(post.getCreatedAt())
        .categoryName(post.getCategory().getName())
        .build();
  }
}
