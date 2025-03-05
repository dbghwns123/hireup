package com.project.hireup.dto;

import com.project.hireup.entity.User;
import com.project.hireup.type.UserStatus;
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
public class UserResponseDto {

  private Long id; // 사용자 ID
  private String email; // 사용자 이메일
  private String name; // 사용자 이름

  public static UserResponseDto fromEntity(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }

}
