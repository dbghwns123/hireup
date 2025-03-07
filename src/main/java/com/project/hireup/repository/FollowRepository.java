package com.project.hireup.repository;

import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);

  Optional<Follow> findByFollowerAndFollowing(User follower, User following);

  List<Follow> findAllByFollower(User follower);

  List<Follow> findAllByFollowing(User following);

}
