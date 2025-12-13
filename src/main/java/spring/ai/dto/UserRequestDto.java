package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequestDto {
        public record Signup(
                @Schema(description = "이메일", example = "email123@naver.com")
                String email,

                @Schema(description = "비밀번호", example = "password123!")
                String password
        ) {}
}