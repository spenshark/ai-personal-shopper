package spring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRequestDto(
        @Schema(description = "사용자 메시지", example = "안녕, 넌 누구니?")
        String message
) {}