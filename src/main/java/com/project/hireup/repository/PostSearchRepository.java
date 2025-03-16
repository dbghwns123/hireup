package com.project.hireup.repository;

import com.project.hireup.entity.PostDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

  // 정확히 일치하는 제목 검색
  List<PostDocument> findByTitle(String title);

  // 정확히 일치하는 해시태그 검색
  List<PostDocument> findByHashtags(String hashtag);

}
