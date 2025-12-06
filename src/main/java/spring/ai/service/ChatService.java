package spring.ai.service;

import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;

public interface ChatService {
    ChatResponseDto.ChatResponse getChatResponse(ChatRequestDto.ChatRequest request);
}
