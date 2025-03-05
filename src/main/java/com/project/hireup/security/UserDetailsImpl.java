package com.project.hireup.security;

import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

  private final String email;
  private final String password;
  private final UserRole userRole;
  private final UserStatus userStatus;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(String email, String password, UserRole userRole, UserStatus userStatus) {
    this.email = email;
    this.password = password;
    this.userRole = userRole;
    this.userStatus = userStatus;
    this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return userStatus != UserStatus.SUSPENDED; // 정지된 계정이면 false 반환
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return userStatus == UserStatus.ACTIVE; // 이메일 인증 완료된 계정만 활성화
  }
}
