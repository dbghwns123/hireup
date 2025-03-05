package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.DO_NOT_EQUAL_CURRENT_PASSWORD;
import static com.project.hireup.type.ErrorCode.NOT_EQUAL_CONFIRM_PASSWORD;
import static com.project.hireup.type.ErrorCode.NOT_EQUAL_CURRENT_PASSWORD;
import static com.project.hireup.type.ErrorCode.NOT_EXIST_NAME;
import static com.project.hireup.type.UserRole.ROLE_USER;
import static com.project.hireup.type.UserStatus.ACTIVE;

import com.project.hireup.dto.MyProfileResponseDto;
import com.project.hireup.dto.PasswordChangeRequestDto;
import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 이름으로 정보 조회
  public List<UserResponseDto> searchByName(String name) {
    List<User> users = userRepository.findAllByName(name);

    // 해당 이름으로 생성된 계정이 없을시 예외
    if (users.isEmpty()) {
      throw new HireUpException(NOT_EXIST_NAME);
    }

    return users.stream()
        .filter(user -> user.getUserRole() == ROLE_USER) // ROLE_USER만 필터링
        .filter(user -> user.getStatus() == ACTIVE) // ACTIVE 상태만 필터링
        .map(UserResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 나의 프로필 조회
  public MyProfileResponseDto getMyProfile(String email) {

    // 이미 로그인을 성공하고 조회하는 것이기 때문에 바로 get 으로 user 객체 가져오기
    User user = userRepository.findByEmail(email).get();

    return MyProfileResponseDto.fromEntity(user);
  }

  // 비밀번호 변경
  public void changePassword(@Valid PasswordChangeRequestDto requestDto, String email) {

    // 이미 로그인을 성공하고 조회하는 것이기 때문에 바로 get 으로 user 객체 가져오기
    User user = userRepository.findByEmail(email).get();

    // 기존 비밀번호 검증
    if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
      throw new HireUpException(NOT_EQUAL_CURRENT_PASSWORD);
    }

    // 기존 비밀번호와 새로운 비밀번호가 같은지 검증
    if (requestDto.getCurrentPassword().equals(requestDto.getNewPassword())) {
      throw new HireUpException(DO_NOT_EQUAL_CURRENT_PASSWORD);
    }

    // 새로운 비밀번호와 비밀번호 확인 검증
    if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
      throw new HireUpException(NOT_EQUAL_CONFIRM_PASSWORD);
    }

    // 새로운 비밀번호로 업데이트
    user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
    userRepository.save(user);
  }

  // 회원 탈퇴
  public void deleteAccount(String email) {

    // 이미 로그인을 성공하고 조회하는 것이기 때문에 바로 get 으로 user 객체 가져오기
    User user = userRepository.findByEmail(email).get();

    // 사용자 삭제
    userRepository.delete(user);
  }
}
