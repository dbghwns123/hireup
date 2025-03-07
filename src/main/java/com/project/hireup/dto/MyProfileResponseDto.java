package com.project.hireup.dto;

import com.project.hireup.entity.User;
import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import java.time.LocalDateTime;
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
public class MyProfileResponseDto {

  private Long id;
  private String email;
  private String name;
  private boolean emailAuthYn;
  private UserStatus status;
  private UserRole userRole;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static MyProfileResponseDto fromEntity(User user) {
    return MyProfileResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .emailAuthYn(user.isEmailAuthYn())
        .status(user.getStatus())
        .userRole(user.getUserRole())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

}
