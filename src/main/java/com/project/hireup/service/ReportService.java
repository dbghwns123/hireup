package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.*;
import static com.project.hireup.type.PostStatus.*;

import com.project.hireup.dto.ReportRequestDto;
import com.project.hireup.entity.Post;
import com.project.hireup.entity.Report;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.PostRepository;
import com.project.hireup.repository.ReportRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.PostStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Transactional
  public void createReport(Long postId, Long userId, @Valid ReportRequestDto requestDto) {

    // 게시글 검증
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_POST));

    // 유저 검증
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(NOT_EXIST_ACCOUNT));

    reportRepository.save(Report.builder()
        .reporter(user)
        .post(post)
        .reason(requestDto.getReason())
        .build());

    // 자동 상태 변경 (10회 이상일 경우 비공개 처리)
    if (post.getReports().size() >= 10) {
      post.setStatus(PRIVATE);
      postRepository.save(post);
    }
  }
}
