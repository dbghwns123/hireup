package com.project.hireup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  @Size(max = 50, message = "이름은 최대 50자까지 입력 가능합니다.")
  private String name;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상, 최대 20자 이하여야 합니다.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
  private String confirmPassword;

  @Email(message = "유효한 이메일 주소를 입력해주세요.")
  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  private String email;

  private boolean admin = false;
  private String adminToken = "";
}
