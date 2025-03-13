package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.*;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_FOLLOWER;
import static com.project.hireup.type.ErrorCode.NOT_FOUND_POST;
import static com.project.hireup.type.ErrorCode.PRIVATE_POST;

import com.project.hireup.dto.CommentRequestDto;
import com.project.hireup.dto.CommentResponseDto;
import com.project.hireup.entity.Comment;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.CommentRepository;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.PostStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  // 댓글 작성
  @Transactional
  public void createComment(Long postId, Long userId, @Valid CommentRequestDto requestDto) {

    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // 작성자 정보 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 게시글 상태 확인 (PRIVATE 이면 댓글 작성 불가)
    if (post.getStatus() == PostStatus.PRIVATE) {
      throw new HireUpException(PRIVATE_POST);
    }

    // 게시글의 상태가 Follower 일 때, 현재 유저가 작성자를 팔로우하고 있는지 확인
    if (post.getStatus() == PostStatus.FOLLOWER) {

      if (!followRepository.existsByFollowerAndFollowing(user, post.getUser())) {
        throw new HireUpException(NOT_FOLLOWER);
      }
    }

    commentRepository.save(Comment.builder()
        .content(requestDto.getContent())
        .user(user)
        .post(post)
        .build());
  }

  // 댓글 목록 조회
  public Page<CommentResponseDto> getCommentsByPost(Long postId, Pageable pageable) {

    // 게시글 존재 여부 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_FOUND_POST));

    // 최상위 댓글만 가져오기
    Page<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post, pageable);
    return comments.map(CommentResponseDto::fromEntity);
  }

}
