package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatResponseDto(
        @Schema(description = "AI 응답")
        String answer
) {}
