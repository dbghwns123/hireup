package com.project.hireup.repository;

import com.project.hireup.entity.Like;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  boolean existsByUserAndPost(User user, Post post);

}
