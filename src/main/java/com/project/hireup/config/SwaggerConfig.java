package com.project.hireup.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("HireUp API Documentation")
            .version("1.0.0")
            .description("HireUp 프로젝트의 RESTful API 명세서"))
        .addSecurityItem(new SecurityRequirement().addList("AccessTokenKey"))
        .components(
            new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(
                    "AccessTokenKey",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식 인증
                        .scheme("bearer") // Bearer 토큰 사용
                        .bearerFormat("JWT") // JWT 형식 명시
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .description("JWT Authorization header. Example: \"Bearer {token}\"")));
  }

  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder()
        .group("HireUp APIs") // 그룹 이름 설정
        .pathsToMatch("/api/**") // /api/ 경로 하위만 Swagger 적용
        .build();
  }
}
