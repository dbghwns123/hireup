package com.project.hireup.controller;

import com.project.hireup.dto.MyProfileResponseDto;
import com.project.hireup.dto.UserResponseDto;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.security.UserDetailsImpl;
import com.project.hireup.service.UserService;
import com.project.hireup.type.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated // 유효성 검사 활성화
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  // 이름으로 정보 조회
  @Operation(summary = "이름으로 사용자 조회", description = "사용자 이름을 통해 여러 사용자를 조회합니다.")
  @GetMapping("/search/name")
  public ResponseEntity<List<UserResponseDto>> searchByName(
      @RequestParam @NotBlank(message = "이름은 필수 입력 항목입니다.") String name) {

    List<UserResponseDto> users = userService.searchByName(name);
    if (users.isEmpty()) {
      throw new HireUpException(ErrorCode.NOT_EXIST_NAME);
    }
    return ResponseEntity.ok(users);
  }

  // 나의 프로필 조회
  @Operation(summary = "나의 프로필 조회", description = "현재 로그인된 사용자의 프로필 정보를 조회합니다.")
  @GetMapping("/my-profile")
  public ResponseEntity<MyProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    MyProfileResponseDto myProfile = userService.getMyProfile(userDetails.getEmail());
    return ResponseEntity.ok(myProfile);
  }

}
