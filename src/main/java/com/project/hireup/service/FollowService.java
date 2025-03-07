package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.ALREADY_FOLLOWING;
import static com.project.hireup.type.ErrorCode.DO_NOT_FOLLOWING_MYSELF;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;

import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  // 팔로우 추가
  public void followUser(Long followerId, Long followingId) {

    // 팔로워 계정
    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 팔로잉할 계정
    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 나의 계정을 팔로잉 시도할시
    if (followerId.equals(followingId)) {
      throw new HireUpException(DO_NOT_FOLLOWING_MYSELF);
    }

    // 이미 팔로잉중인지 확인
    if (followRepository.existsByFollowerAndFollowing(follower, following)) {
      throw new HireUpException(ALREADY_FOLLOWING);
    }

    followRepository.save(Follow.builder()
        .follower(follower)
        .following(following)
        .build());
  }

}
