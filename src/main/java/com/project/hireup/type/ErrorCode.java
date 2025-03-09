package com.project.hireup.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 500 INTERNAL SERVER ERROR (서버 내부 오류)
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
  EMAIL_NOT_SEND(HttpStatus.INTERNAL_SERVER_ERROR, "이메일이 정상적으로 전송되지 않았습니다."),

  // 400 BAD REQUEST (잘못된 요청)
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 회원입니다."),
  NOT_EQUAL_CONFIRM_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  NOT_EQUAL_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다."),
  DO_NOT_EQUAL_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호로는 변경할 수 없습니다."),
  DO_NOT_FOLLOWING_MYSELF(HttpStatus.BAD_REQUEST, "자신의 계정은 팔로잉할 수 없습니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
  NOT_EQUAL_TOKEN(HttpStatus.BAD_REQUEST, "토큰 값이 일치하지 않습니다."),
  NOT_EXIST_ACCOUNT(HttpStatus.BAD_REQUEST, "해당 계정은 존재하지 않습니다."),
  NOT_EXIST_FOLLOW(HttpStatus.BAD_REQUEST, "팔로우 관계가 존재하지 않습니다."),
  NOT_EXIST_CATEGORY(HttpStatus.BAD_REQUEST, "해당 카테고리는 존재하지 않습니다."),
  NOT_EXIST_EMAIL_AUTH_KEY(HttpStatus.BAD_REQUEST, "해당 이메일 인증 키는 존재하지 않습니다."),
  ALREADY_AUTH(HttpStatus.BAD_REQUEST, "이미 이메일 인증을 완료했습니다."),
  ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우한 사용자입니다."),
  NOT_EXIST_NAME(HttpStatus.BAD_REQUEST, "해당 이름을 가진 계정이 없습니다."),

  // 403 FORBIDDEN (접근 금지)
  EMAIL_UNVERIFIED(HttpStatus.FORBIDDEN, "이메일 인증이 완료되지 않은 계정입니다."),
  SUSPENDED_USER(HttpStatus.FORBIDDEN, "현재 이용이 정지된 계정입니다."),

  // 401 UNAUTHORIZED (인증 실패)
  EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
  INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT 서명이 유효하지 않습니다."),
  JWT_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED, "JWT 검증 중 오류가 발생했습니다."),

  // 400 BAD REQUEST (JWT 관련 요청 오류)
  INVALID_JWT_FORMAT(HttpStatus.BAD_REQUEST, "JWT 형식이 올바르지 않습니다."),
  UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");

  private final HttpStatus status;
  private final String description;
}
