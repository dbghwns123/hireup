package com.project.hireup.service;

import static com.project.hireup.type.ErrorCode.EMAIL_NOT_SEND;
import static com.project.hireup.type.ErrorCode.NOT_EQUAL_CONFIRM_PASSWORD;
import static com.project.hireup.type.ErrorCode.NOT_EQUAL_TOKEN;
import static com.project.hireup.type.ErrorCode.USER_ALREADY_EXISTS;

import com.project.hireup.component.MailComponent;
import com.project.hireup.dto.SignUpRequestDto;
import com.project.hireup.entity.User;
import com.project.hireup.exception.HireUpException;
import com.project.hireup.repository.UserRepository;
import com.project.hireup.type.UserRole;
import com.project.hireup.type.UserStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MailComponent mailComponent;

  @Value("${admin.token}")
  private String adminToken;

  public void signUp(SignUpRequestDto requestDto) {

    // 회원가입 진행 -> 이미 등록된 email이 있다면 예외 처리
    if (userRepository.existsByEmail(requestDto.getEmail())) {
      throw new HireUpException(USER_ALREADY_EXISTS);
    }

    // 비밀번호와 비밀번호 확인 입력이 다를시 예외 처리
    if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
      throw new HireUpException(NOT_EQUAL_CONFIRM_PASSWORD);
    }

    System.out.println(requestDto.isAdmin());
    // 기본으로는 일반 user
    UserRole role = UserRole.ROLE_USER;
    // isAdmin 값이 true 이고 함께 넣어준 토큰 값이 일치한다면 Partner role 부여
    if (requestDto.isAdmin()) {
      if (!adminToken.equals(requestDto.getAdminToken())) {
        throw new HireUpException(NOT_EQUAL_TOKEN);
      }
      role = UserRole.ROLE_ADMIN;
    }

    // email 인증 키를 UUID를 사용하여 랜덤 값으로 생성
    String uuid = UUID.randomUUID().toString();

    User user = User.builder()
        .email(requestDto.getEmail())
        .name(requestDto.getName())
        .password(requestDto.getPassword()) // 추후 BCrypt.hashpw 를 사용하여 비밀번호 암호화 예정
        .emailAuthKey(uuid)
        .userRole(role)
        .emailAuthYn(false)
        .status(UserStatus.UNVERIFIED)
        .build();
    userRepository.save(user);

    String email = requestDto.getEmail();
    String subject = "HireUp 사이트 가입을 환영합니다!";
    String text = "<p>" + requestDto.getName() + "님, HireUp 사이트 가입을 환영합니다!</p>"
        + "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>"
        + "<div><a target='_blank' href='http://localhost:8080/api/user/email-auth?id=" + uuid
        + "'>가입 완료</a></div>"
        + "<p>가입을 완료하시면 HireUp의 다양한 서비스를 이용하실 수 있습니다.</p>"
        + "<p>감사합니다.</p>";

    boolean sendMail = mailComponent.sendMail(email, subject, text);

    if (!sendMail) {
      throw new HireUpException(EMAIL_NOT_SEND);
    }

  }
}
