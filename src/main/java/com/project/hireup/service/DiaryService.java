package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.DIARY_ALREADY_EXISTS;
import static com.project.hireup.type.ErrorCode.DIARY_NOT_ALLOWED;
import static com.project.hireup.type.ErrorCode.USER_NOT_FOUND;

import com.project.hireup.client.OpenWeatherClient;
import com.project.hireup.dto.DiaryRequestDto;
import com.project.hireup.entity.Diary;
import com.project.hireup.entity.User;
import com.project.hireup.entity.Weather;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.DiaryRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.UserRole;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final OpenWeatherClient openWeatherClient;
  private final UserRepository userRepository;

  @Transactional
  public void createDiary(Long userId, DiaryRequestDto request) {
    LocalDate today = LocalDate.now();

    // 1. 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new HireUpException(USER_NOT_FOUND));

    // 2. 일반 사용자만 작성 가능
    if (user.getUserRole() != UserRole.ROLE_USER) {
      throw new HireUpException(DIARY_NOT_ALLOWED);
    }

    // 3. 중복 작성 방지
    if (diaryRepository.existsByUserAndDate(user, today)) {
      throw new HireUpException(DIARY_ALREADY_EXISTS);
    }

    // 4. 날씨 조회
    Weather weather = openWeatherClient.getCurrentWeather("Seoul");

    // 5. 저장
    diaryRepository.save(Diary.builder()
        .user(user)
        .date(today)
        .content(request.getContent())
        .weather(weather)
        .build());
  }
}