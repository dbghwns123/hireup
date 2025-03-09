package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.CAN_NOT_READ_POST;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_CATEGORY;
import static com.project.hireup.type.ErrorCode.NOT_FOUND_POST;

import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.entity.Category;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.CategoryRepository;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.PostStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CategoryRepository categoryRepository;
  private final FollowRepository followRepository;

  // 게시글 생성
  public void createPost(@Valid PostRequestDto requestDto, Long id) {

    // 유저 조회
    User user = userRepository.findById(id)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 카테고리 조회
    Category category = categoryRepository.findById(requestDto.getCategoryId())
        .orElseThrow(() -> new HireUpException(NOT_EXIST_CATEGORY));

    postRepository.save(Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .category(category)
        .hashtags(requestDto.getHashtags())
        .status(requestDto.getStatus())
        .user(user)
        .build());
  }

  // 특정 게시글 조회
  public Post getPostById(Long postId, Long userId) {

    // 게시글 조회 요청을 한 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 게시글이 있는지 조회
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // 게시글의 상태가 Private 일 때
    if (post.getStatus() == PostStatus.PRIVATE) {
      throw new HireUpException(CAN_NOT_READ_POST);
    }

    // 게시글의 상태가 Follower 일 때, 현재 유저가 작성자를 팔로우하고 있는지 확인
    if (post.getStatus() == PostStatus.FOLLOWER) {

      boolean isFollowing = followRepository.existsByFollowerAndFollowing(user, post.getUser());

      if (!isFollowing) {
        throw new HireUpException(CAN_NOT_READ_POST);
      }
    }
    return post;
  }

  // 특정 카테고리의 게시글 목록 조회(페이징 처리)
  public Page<Post> getPostByCategory(Long categoryId, Long userId, Pageable pageable) {

    // 게시글 조회 요청을 한 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 카테고리가 존재하는지 확인
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_CATEGORY));

    return postRepository.findByCategoryAndVisibility(categoryId, user, pageable);
  }

  // 전체 게시글 목록 조회(페이징 처리)
  public Page<Post> getAllPosts(Long id, Pageable pageable) {

    // 게시글 조회 요청을 한 유저 조회
    User user = userRepository.findById(id)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 게시글 조회 (필터링 적용된 JPQL 사용)
    return postRepository.findAllVisiblePosts(user, pageable);
  }

}
