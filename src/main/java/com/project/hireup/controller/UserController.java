package com.project.hireup.controller;

import com.project.hireup.dto.SignInRequestDto;
import com.project.hireup.dto.SignUpRequestDto;
import com.project.hireup.service.UserService;
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
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

    userService.signUp(requestDto);

    return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
  }

  @PostMapping("/sign-in")
  public ResponseEntity<String> signIn(@Valid @RequestBody SignInRequestDto requestDto) {
    String token = userService.signIn(requestDto.getEmail(), requestDto.getPassword());
    return ResponseEntity.ok(token);
  }


  @GetMapping("/email-auth")
  public ResponseEntity<String> emailAuth(@RequestParam String uuid) {

    userService.emailAuth(uuid);

    return ResponseEntity.ok("이메일 인증이 성공적으로 완료되었습니다.");
  }
}
