package com.project.hireup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequestDto {

  @NotBlank(message = "기존 비밀번호는 필수 입력 항목입니다.")
  private String currentPassword;

  @NotBlank(message = "새로운 비밀번호는 필수 입력 항목입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상, 최대 20자 이하여야 합니다.")
  private String newPassword;

  @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
  private String confirmPassword;

}
