package spring.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;
import spring.ai.global.base.ApiResponse;
import spring.ai.service.ChatService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "Chatbot API", description = "Spring AI를 활용한 간단한 챗봇 테스트")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "챗봇과 대화하기 (DTO)", description = "사용자의 메시지를 DTO로 전송하고 AI의 응답을 DTO로 반환합니다.")
    @PostMapping("/ask-dto")
    public ApiResponse<ChatResponseDto.ChatResponse> askChatbotWithDto(
            @RequestBody ChatRequestDto.ChatRequest request) {
        return ApiResponse.onSuccess(chatService.getChatResponse(request));
    }
}
