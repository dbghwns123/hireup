package com.project.hireup.repository;

import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);


}
