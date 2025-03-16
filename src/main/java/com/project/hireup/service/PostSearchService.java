package com.project.hireup.service;

import com.project.hireup.entity.Post;
import com.project.hireup.entity.PostDocument;
import com.project.hireup.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSearchService {

  private final PostSearchRepository postSearchRepository;

  // 게시글 저장 (Elasticsearch에 동기화)
  @Transactional
  public void savePost(Post post) {
    postSearchRepository.save(PostDocument.fromEntity(post));
  }

}
