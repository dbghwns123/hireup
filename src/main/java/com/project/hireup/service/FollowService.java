package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.ALREADY_FOLLOWING;
import static com.project.hireup.type.ErrorCode.DO_NOT_FOLLOWING_MYSELF;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_FOLLOW;
import static com.project.hireup.type.NotificationType.FOLLOW;

import com.project.hireup.component.MailComponent;
import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.entity.Follow;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final MailComponent mailComponent;
  private final NotificationService notificationService;

  // 팔로우 추가
  @Transactional
  public void followUser(Long followerId, Long followingId) {

    // 나의 계정을 팔로잉 시도할시
    if (followerId.equals(followingId)) {
      throw new HireUpException(DO_NOT_FOLLOWING_MYSELF);
    }

    // 팔로워 계정
    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 팔로잉할 계정
    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 이미 팔로잉중인지 확인
    if (followRepository.existsByFollowerAndFollowing(follower, following)) {
      throw new HireUpException(ALREADY_FOLLOWING);
    }

    followRepository.save(Follow.builder()
        .follower(follower)
        .following(following)
        .build());

    // 팔로잉 대상 사용자에게 메일 전송
    String email = following.getEmail();
    String subject = "새로운 팔로워가 생겼습니다!";
    String text = "<p>안녕하세요, " + following.getName() + "님.</p>"
        + "<p>회원 <strong>" + follower.getName() + "</strong>님이 귀하를 팔로우하기 시작했습니다.</p>"
        + "<p>감사합니다.</p>";

    mailComponent.sendMail(email, subject, text);

    // 팔로잉 대상 사용자에게 알림 저장
    notificationService.createNotification(following, FOLLOW);
  }

  // 팔로우 취소
  @Transactional
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
  public Page<UserResponseDto> getFollowingList(Long userId, Pageable pageable) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    return followRepository.findAllByFollower(user, pageable)
        .map(follow -> UserResponseDto.fromEntity(follow.getFollowing()));

  }

  // 팔로워 목록 조회 (나를 팔로우한 사람들)
  public Page<UserResponseDto> getFollowerList(Long userId, Pageable pageable) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    return followRepository.findAllByFollowing(user, pageable)
        .map(follow -> UserResponseDto.fromEntity(follow.getFollower()));

  }

}
