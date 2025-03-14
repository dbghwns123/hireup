package com.project.hireup.repository;

import com.project.hireup.entity.Like;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  long countByPostId(Long postId);

  boolean existsByUserAndPost(User user, Post post);

  Optional<Like> findByUserAndPost(User user, Post post);

  boolean existsByUserIdAndPostId(Long userId, Long postId);
}
