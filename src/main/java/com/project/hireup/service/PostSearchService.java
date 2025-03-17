package com.project.hireup.service;

import com.project.hireup.entity.Post;
import com.project.hireup.entity.PostDocument;
import com.project.hireup.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  // 게시글 삭제 (Elasticsearch에서도 삭제)
  @Transactional
  public void deletePost(Long postId) {
    postSearchRepository.deleteById(postId);
  }

  // 게시글 검색 (정확히 일치하는 제목, 페이징 처리)
  public Page<PostDocument> searchByTitle(String title, Pageable pageable) {
    return postSearchRepository.findByTitle(title, pageable);
  }

  // 게시글 검색 (정확히 일치하는 해시태그, 페이징 처리)
  public Page<PostDocument> searchByHashtag(String hashtag, Pageable pageable) {
    return postSearchRepository.findByHashtags(hashtag, pageable);
  }

}
