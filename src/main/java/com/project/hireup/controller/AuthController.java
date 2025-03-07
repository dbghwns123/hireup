package com.project.hireup.controller;

import com.project.hireup.dto.SignInRequestDto;
import com.project.hireup.dto.SignUpRequestDto;
import com.project.hireup.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService userService;

  @Operation(summary = "회원가입", description = "사용자의 이메일, 비밀번호 등을 입력받아 회원가입을 처리합니다.")
  @PostMapping("/sign-up")
  public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

    userService.signUp(requestDto);

    return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
  }

  @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 JWT 토큰을 반환합니다.")
  @PostMapping("/sign-in")
  public ResponseEntity<String> signIn(@Valid @RequestBody SignInRequestDto requestDto) {
    String token = userService.signIn(requestDto.getEmail(), requestDto.getPassword());
    return ResponseEntity.ok(token);
  }

  @Operation(summary = "이메일 인증", description = "이메일 인증 링크를 통해 계정 활성화 상태를 변경합니다.")
  @GetMapping("/email-auth")
  public ResponseEntity<String> emailAuth(@RequestParam String uuid) {

    userService.emailAuth(uuid);

    return ResponseEntity.ok("이메일 인증이 성공적으로 완료되었습니다.");
  }
}
