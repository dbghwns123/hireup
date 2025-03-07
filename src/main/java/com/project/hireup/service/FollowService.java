package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.ALREADY_FOLLOWING;
import static com.project.hireup.type.ErrorCode.DO_NOT_FOLLOWING_MYSELF;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_FOLLOW;

import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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

  // 팔로우 취소
  public void unfollowUser(Long followerId, Long followingId) {

    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_FOLLOW));

    followRepository.delete(follow);
  }

  // 팔로잉 목록 조회 (내가 팔로우한 사람들)
  public List<UserResponseDto> getFollowingList(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    return followRepository.findAllByFollower(user)
        .stream()
        .map(follow -> UserResponseDto.fromEntity(follow.getFollowing()))
        .collect(Collectors.toList());
  }

}
