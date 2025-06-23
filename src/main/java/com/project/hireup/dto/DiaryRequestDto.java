package com.project.hireup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDto {

  private String content;
  private String location; // 사용자가 입력한 지역명 (예: "Seoul", "Busan")
}