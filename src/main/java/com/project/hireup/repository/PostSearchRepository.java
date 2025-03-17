package com.project.hireup.repository;

import com.project.hireup.entity.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

  // 정확히 일치하는 제목 검색 (페이징 처리)
  Page<PostDocument> findByTitle(String title, Pageable pageable);

  // 정확히 일치하는 해시태그 검색 (페이징 처리)
  Page<PostDocument> findByHashtags(String hashtag, Pageable pageable);

}
