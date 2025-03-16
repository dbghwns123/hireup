package com.project.hireup.dto;

import com.project.hireup.entity.Report;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDto {

  private Long id;
  private String reason;
  private Long postId;
  private LocalDateTime reportedAt;

  public static ReportResponseDto fromEntity(Report report) {
    return ReportResponseDto.builder()
        .id(report.getId())
        .reason(report.getReason())
        .postId(report.getPost().getId())
        .reportedAt(report.getReportedAt())
        .build();
  }
}
