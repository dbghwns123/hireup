package com.project.hireup.controller;

import com.project.hireup.dto.DiaryRequestDto;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

  private final DiaryService diaryService;

  @PostMapping
  public ResponseEntity<Void> createDiary(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody DiaryRequestDto diaryRequestDto) {
    diaryService.createDiary(userDetails.getId(), diaryRequestDto);
    return ResponseEntity.ok().build();
  }
}
