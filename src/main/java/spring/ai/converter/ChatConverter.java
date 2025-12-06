package spring.ai.converter;

import spring.ai.dto.ChatResponseDto;

public class ChatConverter {

    public static ChatResponseDto toChatResponse(String response) {
        return new ChatResponseDto(response);
    }
}
