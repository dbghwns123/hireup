package com.project.hireup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.project.hireup.client.OpenWeatherClient;
import com.project.hireup.dto.DiaryRequestDto;
import com.project.hireup.entity.Diary;
import com.project.hireup.entity.User;
import com.project.hireup.entity.Weather;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.DiaryRepository;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

  @InjectMocks
  private DiaryService diaryService;

  @Mock
  private DiaryRepository diaryRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private OpenWeatherClient openWeatherClient;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .name("테스트유저")
        .password("encoded_pw")
        .userRole(UserRole.ROLE_USER)
        .emailAuthKey("abc123")
        .emailAuthYn(true)
        .status(UserStatus.ACTIVE)
        .build();
  }

  @Test
  void 성공_일기_작성() {
    // given
    LocalDate today = LocalDate.now();
    String content = "오늘 날씨 맑음!";
    String location = "Seoul";

    DiaryRequestDto requestDto = new DiaryRequestDto(content, location);

    Weather weather = Weather.builder()
        .weather("Clear")
        .description("맑음")
        .icon("http://example.com/icon.png")
        .temperature(25.5)
        .build();

    // mocking
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    Mockito.when(diaryRepository.existsByUserAndDate(testUser, today)).thenReturn(false);
    Mockito.when(openWeatherClient.getCurrentWeather(location)).thenReturn(weather);

    // when
    diaryService.createDiary(1L, requestDto);

    // then
    ArgumentCaptor<Diary> diaryCaptor = ArgumentCaptor.forClass(Diary.class);
    verify(diaryRepository).save(diaryCaptor.capture());

    Diary savedDiary = diaryCaptor.getValue();
    assertEquals(content, savedDiary.getContent());
    assertEquals(today, savedDiary.getDate());
    assertEquals(testUser, savedDiary.getUser());
    assertEquals(weather.getDescription(), savedDiary.getWeather().getDescription());
    assertEquals(weather.getTemperature(), savedDiary.getWeather().getTemperature());
  }

  @Test
  void 실패_존재하지_않는_사용자() {
    // given
    DiaryRequestDto requestDto = new DiaryRequestDto("내용", "Seoul");

    // mocking
    Mockito.when(userRepository.findById(999L)).thenReturn(Optional.empty());

    // when & then
    HireUpException exception = assertThrows(HireUpException.class,
        () -> diaryService.createDiary(999L, requestDto));

    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void 실패_일반_사용자가_아닌_경우() {
    // given
    User admin = User.builder()
        .id(2L)
        .email("admin@example.com")
        .name("관리자")
        .password("pw")
        .userRole(UserRole.ROLE_ADMIN)
        .emailAuthKey("key")
        .emailAuthYn(true)
        .status(UserStatus.ACTIVE)
        .build();

    DiaryRequestDto requestDto = new DiaryRequestDto("관리자 내용", "Seoul");

    // mocking
    Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(admin));

    // when & then
    HireUpException exception = assertThrows(HireUpException.class,
        () -> diaryService.createDiary(2L, requestDto));

    assertEquals(ErrorCode.DIARY_NOT_ALLOWED, exception.getErrorCode());
  }
}