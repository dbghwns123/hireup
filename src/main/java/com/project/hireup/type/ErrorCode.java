package com.project.hireup.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
  INVALID_REQUEST("잘못된 요청입니다"),
  USER_ALREADY_EXISTS("이미 등록된 회원입니다."),
  NOT_EQUAL_CONFIRM_PASSWORD("비밀번호가 일치하지 않습니다."),
  NOT_EQUAL_TOKEN("토큰 값이 일치하지 않습니다."),
  EMAIL_NOT_SEND("이메일이 정상적으로 전송되지 않았습니다."),
  ;

  private final String description;

}
