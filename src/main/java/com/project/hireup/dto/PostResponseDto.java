package com.project.hireup.dto;

import com.project.hireup.converter.StringSetConverter;
import com.project.hireup.entity.Category;
import com.project.hireup.entity.Comment;
import com.project.hireup.entity.Like;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.PostImage;
import com.project.hireup.entity.Report;
import com.project.hireup.entity.User;
import com.project.hireup.type.PostStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
