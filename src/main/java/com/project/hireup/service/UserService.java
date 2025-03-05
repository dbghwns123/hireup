package com.project.hireup.service;

import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.ErrorCode;
import com.project.hireup.type.UserRole;
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
        .filter(user -> user.getUserRole() == UserRole.ROLE_USER)
        .map(UserResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

}
