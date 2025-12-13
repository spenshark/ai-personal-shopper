package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDto {
        public record Signup(
                @Schema(description = "회원가입 성공 메시지", example = "회원가입이 성공적으로 완료되었습니다.")
                String message,

                @Schema(description = "유저 id", example = "1")
                Long userId,

                @Schema(description = "챗룸 id", example = "1")
                Long chatRoomId
        ) {}
}
