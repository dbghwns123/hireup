package com.project.hireup.controller;

import com.project.hireup.dto.ReportRequestDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

  private final ReportService reportService;

  @Operation(summary = "게시글 신고", description = "특정 게시글을 신고합니다.")
  @PostMapping("/{postId}")
  public ResponseEntity<String> createReport(@PathVariable Long postId,
      @RequestBody @Valid ReportRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    reportService.createReport(postId, userDetails.getId(), requestDto);
    return ResponseEntity.ok("신고가 성공적으로 접수되었습니다.");
  }


}
