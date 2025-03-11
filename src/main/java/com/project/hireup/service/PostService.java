package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.CAN_NOT_READ_POST;
import static com.project.hireup.type.ErrorCode.CAN_NOT_UPDATE_POST;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_POST;
import static com.project.hireup.type.ErrorCode.NOT_FOUND_POST;

import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.dto.PostResponseDto;
import com.project.hireup.entity.Category;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.PostStatus;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CategoryService categoryService;
  private final FollowRepository followRepository;

  // 게시글 생성
  @Transactional
  public void createPost(@Valid PostRequestDto requestDto, Long id) {

    // 유저 조회
    User user = userRepository.findById(id)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 캐싱된 카테고리 데이터 활용 (CategoryService에서 가져오기)
    Category category = categoryService.getCategoryById(requestDto.getCategoryId());

    // 카테고리가 공지사항(Admin 권한)인지 확인
    if (category.isNotice()) {
      throw new HireUpException(ErrorCode.NO_PERMISSION_CATEGORY);
    }

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
  @Transactional
  public PostResponseDto getPostById(Long postId, Long userId) {

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

      if (!followRepository.existsByFollowerAndFollowing(user, post.getUser())) {
        throw new HireUpException(CAN_NOT_READ_POST);
      }
    }
    // 조회수 증가
    post.increaseViewCount();
    return PostResponseDto.fromEntity(post);
  }

  // 특정 카테고리의 게시글 목록 조회(페이징 처리)
  public Page<PostResponseDto> getPostByCategory(Long categoryId, Long userId, Pageable pageable) {

    // 게시글 조회 요청을 한 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 캐싱된 카테고리 데이터 활용
    Category category = categoryService.getCategoryById(categoryId);

    Page<Post> posts = postRepository.findByCategoryAndVisibility(categoryId,
        user, pageable);

    // Post 엔티티를 PostResponseDto로 변환
    return posts.map(PostResponseDto::fromEntity);
  }

  // 전체 게시글 목록 조회(페이징 처리)
  public Page<PostResponseDto> getAllPosts(Long id, Pageable pageable) {

    // 게시글 조회 요청을 한 유저 조회
    User user = userRepository.findById(id)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 게시글 조회 (필터링 적용된 JPQL 사용)
    Page<Post> posts = postRepository.findAllVisiblePosts(user, pageable);

    // Post 엔티티를 PostResponseDto로 변환
    return posts.map(PostResponseDto::fromEntity);
  }

  // 게시글 수정
  @Transactional
  public void updatePost(Long postId, Long userId, @Valid PostRequestDto requestDto) {

    // 게시글 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_POST));

    // 내가 작성한 게시글이 아닐시
    if (!Objects.equals(post.getUser().getId(), userId)) {
      throw new HireUpException(CAN_NOT_UPDATE_POST);
    }

    // 캐싱된 카테고리 데이터 활용
    Category category = categoryService.getCategoryById(requestDto.getCategoryId());

    // 수정하려는 카테고리가 공지사항 카테고리인지 확인
    if (category.isNotice()) {
      throw new HireUpException(ErrorCode.NO_PERMISSION_CATEGORY);
    }

    // 게시글 수정 (엔티티의 메서드 호출)
    post.updatePost(requestDto, category);

    // 변경 사항 저장
    postRepository.save(post);
  }

  // 게시글 삭제
  @Transactional
  public void deletePost(Long postId, Long userId) {

    // 게시글 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_POST));

    // 내가 작성한 게시글이 아닐시
    if (!Objects.equals(post.getUser().getId(), userId)) {
      throw new HireUpException(CAN_NOT_UPDATE_POST);
    }

    postRepository.delete(post);
  }
}
