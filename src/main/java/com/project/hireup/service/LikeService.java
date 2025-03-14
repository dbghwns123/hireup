package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.*;
import static com.project.hireup.type.NotificationType.LIKE;
import static com.project.hireup.type.PostStatus.*;

import com.project.hireup.component.MailComponent;
import com.project.hireup.entity.Like;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.LikeRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.PostStatus;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final StringRedisTemplate redisTemplate;
  private final MailComponent mailComponent;
  private final NotificationService notificationService;

  private static final String LIKE_COUNT_KEY = "post:like:count:";
  private static final String USER_LIKED_KEY = "user:liked:";

  /**
   * 좋아요 추가
   */
  @Transactional
  public void addLike(Long postId, Long userId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // 사용자 존재 여부 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 게시글 상태가 PRIVATE인 경우 좋아요 불가
    if (post.getStatus() == PRIVATE) {
      throw new HireUpException(PRIVATE_POST);
    }

    // 게시글 상태가 FOLLOWER인 경우 팔로워인지 확인
    if (post.getStatus() == FOLLOWER) {
      if (!followRepository.existsByFollowerAndFollowing(user, post.getUser())) {
        throw new HireUpException(NOT_FOLLOWER);
      }
    }

    // 이미 좋아요를 눌렀는지 확인 (Redis 먼저 확인 후 DB 확인)
    String userLikedKey = USER_LIKED_KEY + userId;
    Boolean hasLiked = redisTemplate.opsForSet().isMember(userLikedKey, postId.toString());

    if (Boolean.TRUE.equals(hasLiked) || likeRepository.existsByUserAndPost(user, post)) {
      throw new HireUpException(ALREADY_LIKED);
    }

    // DB에 좋아요 저장
    likeRepository.save(Like.builder()
        .user(user)
        .post(post)
        .build());

    // Redis에 좋아요 수 증가 (Redis의 Atomic Increment 활용)
    Long updatedLikeCount = redisTemplate.opsForValue().increment(LIKE_COUNT_KEY + postId);

    // 게시글의 좋아요 수 증가
//    assert updatedLikeCount != null;
    post.setLikeCount(updatedLikeCount.intValue());
    postRepository.save(post);

    // 게시글 작성자에게 메일 전송
    String email = post.getUser().getEmail();
    String subject = "게시글에 새로운 좋아요가 추가되었습니다!";
    String text = "<p>안녕하세요, " + post.getUser().getName() + "님.</p>"
        + "<p>회원 <strong>" + user.getName() + "</strong>님이 귀하의 게시글 <strong>\"" + post.getTitle()
        + "\"</strong>에 좋아요를 눌렀습니다.</p>"
        + "<p>감사합니다.</p>";

    mailComponent.sendMail(email, subject, text);

    // 게시글 작성자에게 알림 저장
    notificationService.createNotification(post.getUser(), LIKE);
  }

  /**
   * 좋아요 취소
   */
  @Transactional
  public void removeLike(Long postId, Long userId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // 사용자 존재 여부 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 좋아요 정보 확인
    Like like = likeRepository.findByUserAndPost(user, post)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_LIKE));

    // DB에서 좋아요 정보 삭제
    likeRepository.delete(like);

    // Redis에서 좋아요 정보 삭제 및 좋아요 수 감소 (Redis의 Atomic Decrement 활용)
    String userLikedKey = USER_LIKED_KEY + userId;
    redisTemplate.opsForSet().remove(userLikedKey, postId.toString());

    Long updatedLikeCount = redisTemplate.opsForValue().decrement(LIKE_COUNT_KEY + postId);

    // 게시글의 좋아요 수 감소 (Redis 값을 사용하여 정합성 유지)
    if (updatedLikeCount != null && updatedLikeCount >= 0) {
      post.setLikeCount(updatedLikeCount.intValue());
      postRepository.save(post);
    } else {
      throw new RuntimeException("좋아요 수 감소 중 오류 발생");
    }
  }

  /**
   * 게시글의 좋아요 수 조회
   */
  public Long getLikeCount(Long postId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // Redis에서 좋아요 수 조회 (없으면 DB 조회 후 Redis 갱신)
    String countKey = LIKE_COUNT_KEY + postId;
    String count = redisTemplate.opsForValue().get(countKey);

    if (count != null) {
      return Long.parseLong(count);
    } else {
      long likeCount = likeRepository.countByPostId(postId);
      redisTemplate.opsForValue().set(countKey, String.valueOf(likeCount));
      return likeCount;
    }
  }

  /**
   * 사용자가 게시글에 좋아요를 눌렀는지 확인
   */
  public boolean hasUserLikedPost(Long postId, Long userId) {
    // Redis 확인
    String userLikedKey = USER_LIKED_KEY + userId;
    Boolean hasLiked = redisTemplate.opsForSet().isMember(userLikedKey, postId.toString());

    if (Boolean.TRUE.equals(hasLiked)) {
      return true;
    }

    // DB 확인
    return likeRepository.existsByUserIdAndPostId(userId, postId);
  }

  /**
   * Redis와 DB 동기화 (스케줄링된 메서드)
   */
  @Scheduled(fixedRate = 1800000) // 30분마다 실행
  @Transactional
  public void synchronizeLikeCounts() {
    String lockKey = "lock:sync:likeCounts";
    String lockValue = UUID.randomUUID().toString();

    try {
      // 분산 락 획득 (30초 TTL)
      Boolean locked = redisTemplate.opsForValue()
          .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(30));
      if (Boolean.FALSE.equals(locked)) {
        log.info("다른 인스턴스에서 이미 동기화 작업을 수행 중입니다.");
        return;
      }

      // DB에서 필요한 postId 목록 가져오기
      List<Long> postIds = postRepository.findAllPostIds(); // 필요한 postId만 조회

      for (Long postId : postIds) {
        String countKey = LIKE_COUNT_KEY + postId;
        String countStr = redisTemplate.opsForValue().get(countKey);

        if (countStr != null) {
          int count = Integer.parseInt(countStr);
          postRepository.findById(postId).ifPresent(post -> {
            post.setLikeCount(count);
            postRepository.save(post);
          });
        }
      }
    } finally {
      // 락 해제
      String currentLockValue = redisTemplate.opsForValue().get(lockKey);
      if (lockValue.equals(currentLockValue)) {
        redisTemplate.delete(lockKey);
      }
    }
  }


}
