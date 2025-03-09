package com.project.hireup.repository;

import com.project.hireup.entity.Category;
import com.project.hireup.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findAllByCategory(Category category, Pageable pageable);

}
