package com.project.hireup.service;

import com.project.hireup.dto.MyProfileResponseDto;
import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserResponseDto> searchByName(String name) {
    List<User> users = userRepository.findAllByName(name);

    // 해당 이름으로 생성된 계정이 없을시 예외
    if (users.isEmpty()) {
      throw new HireUpException(ErrorCode.NOT_EXIST_NAME);
    }

    return users.stream()
        // ROLE_USER 만 조회 가능
        .filter(user -> user.getUserRole() == UserRole.ROLE_USER) // ROLE_USER만 필터링
        .filter(user -> user.getStatus() == UserStatus.ACTIVE) // ACTIVE 상태만 필터링
        .map(UserResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  public MyProfileResponseDto getMyProfile(String email) {

    // 이미 로그인을 성공하고 조회하는 것이기 때문에 바로 get으로 user 객체 가져오기
    User user = userRepository.findByEmail(email).get();

    return MyProfileResponseDto.fromEntity(user);

  }

}
