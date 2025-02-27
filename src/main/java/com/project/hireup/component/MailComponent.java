package com.project.hireup.component;

import static com.project.hireup.type.ErrorCode.EMAIL_NOT_SEND;

import com.project.hireup.exception.HireUpException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponent {

  private final JavaMailSender javaMailSender;

  public void sendMail(String mail, String subject, String text) {

    MimeMessagePreparator msg = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        // 메일을 보낼 대상
        mimeMessageHelper.setTo(mail);
        // 메일 제목
        mimeMessageHelper.setSubject(subject);
        // 메일 내용 (내용과, html 설정)
        mimeMessageHelper.setText(text, true);
      }
    };

    try {
      javaMailSender.send(msg);
    } catch (Exception e) {
      throw new HireUpException(EMAIL_NOT_SEND, "메일 전송에 실패했습니다: " + e.getMessage());
    }
  }
}
