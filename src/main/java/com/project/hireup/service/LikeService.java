package com.project.hireup.service;

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
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final StringRedisTemplate redisTemplate;

  private static final String LIKE_COUNT_KEY = "post:like:count:";
  private static final String USER_LIKED_KEY = "user:liked:";

  /**
   * 좋아요 추가
   */
  @Transactional
  public void addLike(Long postId, Long userId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_FOUND_POST));

    // 사용자 존재 여부 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_ACCOUNT));

    // 게시글 상태가 PRIVATE인 경우 좋아요 불가
    if (post.getStatus() == PostStatus.PRIVATE) {
      throw new HireUpException(ErrorCode.PRIVATE_POST);
    }

    // 게시글 상태가 FOLLOWER인 경우 팔로워인지 확인
    if (post.getStatus() == PostStatus.FOLLOWER) {
      if (!followRepository.existsByFollowerAndFollowing(user, post.getUser())) {
        throw new HireUpException(ErrorCode.NOT_FOLLOWER);
      }
    }

    // 이미 좋아요를 눌렀는지 확인 (Redis 먼저 확인 후 DB 확인)
    String userLikedKey = USER_LIKED_KEY + userId;
    Boolean hasLiked = redisTemplate.opsForSet().isMember(userLikedKey, postId.toString());

    if (Boolean.TRUE.equals(hasLiked) || likeRepository.existsByUserAndPost(user, post)) {
      throw new HireUpException(ErrorCode.ALREADY_LIKED);
    }

    // DB에 좋아요 저장
    Like like = Like.builder()
        .user(user)
        .post(post)
        .build();
    likeRepository.save(like);

    // Redis에 좋아요 정보 추가
    redisTemplate.opsForSet().add(userLikedKey, postId.toString());
    redisTemplate.opsForValue().increment(LIKE_COUNT_KEY + postId);

    // 게시글의 좋아요 수 증가
    post.increaseLikeCount();
    postRepository.save(post);
  }

  /**
   * 좋아요 취소
   */
  @Transactional
  public void removeLike(Long postId, Long userId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_FOUND_POST));

    // 사용자 존재 여부 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_ACCOUNT));

    // 좋아요 정보 확인
    Like like = likeRepository.findByUserAndPost(user, post)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_EXIST_LIKE));

    // DB에서 좋아요 정보 삭제
    likeRepository.delete(like);

    // Redis에서 좋아요 정보 삭제
    String userLikedKey = USER_LIKED_KEY + userId;
    redisTemplate.opsForSet().remove(userLikedKey, postId.toString());
    redisTemplate.opsForValue().decrement(LIKE_COUNT_KEY + postId);

    // 게시글의 좋아요 수 감소
    post.decreaseLikeCount();
    postRepository.save(post);
  }

  /**
   * 게시글의 좋아요 수 조회
   */
  public Long getLikeCount(Long postId) {
    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(ErrorCode.NOT_FOUND_POST));

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
  @Scheduled(fixedRate = 300000) // 5분마다 실행
  @Transactional
  public void synchronizeLikeCounts() {
    Set<String> keys = redisTemplate.keys(LIKE_COUNT_KEY + "*");
    if (keys == null || keys.isEmpty()) {
      return;
    }

    for (String key : keys) {
      String postIdStr = key.substring(LIKE_COUNT_KEY.length());
      Long postId = Long.parseLong(postIdStr);
      String countStr = redisTemplate.opsForValue().get(key);

      if (countStr != null) {
        int count = Integer.parseInt(countStr);
        postRepository.findById(postId).ifPresent(post -> {
          post.setLikeCount(count);
          postRepository.save(post);
        });
      }
    }
  }

}
