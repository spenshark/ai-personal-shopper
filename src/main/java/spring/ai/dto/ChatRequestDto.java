package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRequestDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "AI 챗봇에게 질문하는 요청 DTO")
    public static class ChatRequest {
        @Schema(description = "AI에게 보낼 질문", example = "Spring Boot가 뭐야?")
        private String message;
    }
}
