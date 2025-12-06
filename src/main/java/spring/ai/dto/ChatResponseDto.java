package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "AI 챗봇의 답변 응답 DTO")
    public static class ChatResponse {
        @Schema(description = "AI의 답변")
        private String answer;
    }
}
