package com.project.hireup.repository;

import com.project.hireup.entity.Category;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findAllByCategory(Category category, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.category.id = :categoryId " +
      "AND (p.status = 'PUBLIC' OR " +
      "     (p.status = 'FOLLOWER' AND p.user IN " +
      "      (SELECT f.following FROM Follow f WHERE f.follower = :user)))")
  Page<Post> findByCategoryAndVisibility(@Param("categoryId") Long categoryId,
      @Param("user") User user, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE " +
      "(p.status = 'PUBLIC') OR " +
      "(p.status = 'FOLLOWER' AND p.user IN " +
      " (SELECT f.following FROM Follow f WHERE f.follower = :user))")
  Page<Post> findAllVisiblePosts(@Param("user") User user, Pageable pageable);

}
