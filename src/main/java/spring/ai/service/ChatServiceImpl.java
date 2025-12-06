package spring.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import spring.ai.converter.ChatConverter;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    @Override
    public ChatResponseDto getChatResponse(ChatRequestDto request) {
        String response = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

        return ChatConverter.toChatResponse(response);
    }
}
