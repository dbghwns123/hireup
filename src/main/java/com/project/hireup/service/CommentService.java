package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.CAN_NOT_DELETE_COMMENT;
import static com.project.hireup.type.ErrorCode.NOT_COMMENT_OWNER;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_COMMENT;
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
    if (post.getStatus() == PostStatus.FOLLOWER &&
        !followRepository.existsByFollowerAndFollowing(user, post.getUser())) {

      throw new HireUpException(NOT_FOLLOWER);
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

  // 댓글 수정
  @Transactional
  public void updateComment(Long commentId, Long userId, @Valid CommentRequestDto requestDto) {

    // 댓글 존재 여부 확인
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_COMMENT));

    // 작성자 정보 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 작성자 본인 확인
    if (!Objects.equals(comment.getUser().getId(), userId)) {
      throw new HireUpException(NOT_COMMENT_OWNER);
    }
    // 댓글 수정 (엔티티의 메서드 호출)
    comment.updateComment(requestDto);

    // 변경 사항 저장
    commentRepository.save(comment);
  }

  // 댓글 삭제
  @Transactional
  public void deleteComment(Long commentId, Long userId) {

    // 댓글 존재 여부 확인
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_COMMENT));

    // 작성자 정보 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    // 작성자 본인 또는 게시글 작성자 확인 (게시글 작성자도 댓글 삭제 가능)
    boolean isCommentOwner = comment.getUser().getId().equals(userId);
    boolean isPostOwner = comment.getPost().getUser().getId().equals(userId);

    if (!isCommentOwner && !isPostOwner) {
      throw new HireUpException(CAN_NOT_DELETE_COMMENT);
    }

    // 댓글 삭제
    commentRepository.delete(comment);

  }

}
