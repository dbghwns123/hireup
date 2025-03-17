package com.project.hireup.entity;

import jakarta.persistence.Id;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "posts")  // Elasticsearch 의 인덱스 이름
public class PostDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Keyword)
  private String title;

  @Field(type = FieldType.Keyword)
  private Set<String> hashtags;

  public static PostDocument fromEntity(Post post) {
    return PostDocument.builder()
        .id(post.getId())
        .title(post.getTitle())
        .hashtags(post.getHashtags())
        .build();
  }
}
