package spring.ai.converter;

import spring.ai.dto.ChatResponseDto;

public class ChatConverter {

    public static ChatResponseDto.ChatResponse toChatResponse(String response) {
        return ChatResponseDto.ChatResponse.builder()
                .answer(response)
                .build();
    }
}
