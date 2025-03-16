package com.project.hireup.controller;

import com.project.hireup.dto.ReportRequestDto;
import com.project.hireup.dto.ReportResponseDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Operation(summary = "나의 신고 내역 조회", description = "사용자가 자신이 신고한 게시글 목록을 조회합니다.")
  @GetMapping("/my")
  public ResponseEntity<List<ReportResponseDto>> getMyReports(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(reportService.getMyReports(userDetails.getId()));
  }

  @Operation(summary = "나의 특정 게시글 신고 내역 조회", description = "나의 특정 게시글의 신고 내역을 조회합니다.")
  @GetMapping("/post/{postId}")
  public ResponseEntity<List<ReportResponseDto>> getReportsByPost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return ResponseEntity.ok(reportService.getReportsByPost(postId, userDetails.getId()));
  }

  @Operation(summary = "신고 삭제", description = "관리자가 특정 신고를 삭제합니다.")
  @DeleteMapping("/{reportId}")
  public ResponseEntity<String> deleteReport(@PathVariable Long reportId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    reportService.deleteReport(reportId, userDetails.getId());
    return ResponseEntity.ok("신고가 삭제되었습니다.");
  }

}
