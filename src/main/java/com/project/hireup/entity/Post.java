package com.project.hireup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.hireup.converter.StringSetConverter;
import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.type.PostStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 200, nullable = false)
  private String title;

  @Lob
  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column(columnDefinition = "JSON")
  @Convert(converter = StringSetConverter.class)
  private Set<String> hashtags = new HashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PostStatus status = PostStatus.PUBLIC;

  @Column(nullable = false)
  private int viewCount = 0;

  // 좋아요 수 캐싱 필드 추가
  @Column(nullable = false)
  private int likeCount = 0;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Like> likes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Report> reports;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostImage> images = new ArrayList<>();

  public void increaseViewCount() {
    this.viewCount++;
  }

  // 좋아요 수 증가
  public void increaseLikeCount() {
    this.likeCount++;
  }

  // 좋아요 수 감소
  public void decreaseLikeCount() {
    if (this.likeCount > 0) {
      this.likeCount--;
    }
  }

  // 좋아요 수 설정 (Redis와 동기화 시 사용)
  public void setLikeCount(int count) {
    this.likeCount = Math.max(0, count);
  }

  // 게시글 수정 메서드
  public void updatePost(PostRequestDto requestDto, Category category) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.category = category;
    this.hashtags = requestDto.getHashtags();
    this.status = requestDto.getStatus();
  }

}
