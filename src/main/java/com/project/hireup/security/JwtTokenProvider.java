package com.project.hireup.security;

import com.project.hireup.entity.User;
import com.project.hireup.type.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;

  // 암호화 알고리즘
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  private final long tokenTime = 1000L * 60 * 60; // 1시간

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // 토큰 생성
  public String createToken(String email, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenTime);

    return Jwts.builder()
        .setSubject(email) // 사용자 이메일 (아이디)
        .claim("role", role) // 권한 정보
        .setIssuedAt(now) // 발급 시간
        .setExpiration(expiryDate) // 만료 시간
        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
        .compact();
  }

  // JWT 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("JWT 토큰이 만료되었습니다.");
      throw new JwtException("JWT 토큰이 만료되었습니다.");
    } catch (MalformedJwtException e) {
      log.error("JWT 형식이 올바르지 않습니다.");
      throw new JwtException("JWT 형식이 올바르지 않습니다.");
    } catch (Exception e) {
      log.error("JWT 검증 중 오류 발생");
      throw new JwtException("JWT 검증 중 오류 발생");
    }
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token)
        .getBody();

    String email = claims.getSubject(); // JWT에서 email 추출
    String roleString = claims.get("role", String.class); // JWT에서 role을 String으로 가져옴

    // String -> UserRole 변환
    UserRole role = UserRole.valueOf(roleString);

    // User 객체 생성 (필요한 데이터만 포함)
    User user = User.builder()
        .email(email)
        .userRole(role)
        .build();

    // UserDetailsImpl에 User 객체 전달
    UserDetails userDetails = new UserDetailsImpl(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
