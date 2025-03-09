package com.project.hireup.repository;

import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);

  Optional<Follow> findByFollowerAndFollowing(User follower, User following);

  Page<Follow> findAllByFollower(User follower, Pageable pageable); // 페이징 처리

  Page<Follow> findAllByFollowing(User following, Pageable pageable); // 페이징 처리

}
