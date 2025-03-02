package com.project.hireup.security;

import com.project.hireup.entity.User;
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

  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(User user) {
    this.user = user;
    this.authorities = List.of(new SimpleGrantedAuthority(user.getUserRole().name()));
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getStatus() == UserStatus.ACTIVE; // 활성 상태 체크
  }
}
