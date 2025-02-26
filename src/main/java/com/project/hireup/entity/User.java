package com.project.hireup.entity;

import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(length = 100, nullable = false)
  private String emailAuthKey;

  @Column(nullable = false)
  private boolean emailAuthYn;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts;

  @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Follow> followers;

  @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Follow> followings;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Notification> notifications;

}
