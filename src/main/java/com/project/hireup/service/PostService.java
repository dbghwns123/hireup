package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_CATEGORY;

import com.project.hireup.dto.PostRequestDto;
import com.project.hireup.entity.Category;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.CategoryRepository;
import com.project.hireup.repository.FollowRepository;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
