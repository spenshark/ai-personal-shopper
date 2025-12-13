package spring.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.ai.converter.ChatConverter;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;
import spring.ai.global.base.ApiResponse;
import spring.ai.service.ChatService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "Chatbot API", description = "Spring AI를 활용한 챗봇 API")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "챗봇과 대화하기", description = "사용자의 메시지를 DTO로 전송하고 AI의 응답을 DTO로 반환합니다.")
    @PostMapping("")
    public ApiResponse<ChatResponseDto> chat(@RequestBody ChatRequestDto request) {
        return ApiResponse.onSuccess(chatService.chat(request));
    }

    @Operation(summary = "RAG 챗봇과 대화하기", description = "사용자의 메시지를 기반으로 VectorDB에서 관련 상품을 찾아 답변을 생성합니다.")
    @PostMapping("/rag")
    public ApiResponse<ChatResponseDto> ragChat(@RequestBody ChatRequestDto request) {
        return ApiResponse.onSuccess(chatService.ragChat(request));
    }
}
