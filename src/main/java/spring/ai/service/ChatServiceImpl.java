package spring.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ai.converter.ChatConverter;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    @Override
    @Transactional(readOnly = true)
    public ChatResponseDto.ChatResponse getChatResponse(ChatRequestDto.ChatRequest request) {
        String response = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();

        return ChatConverter.toChatResponse(response);
    }
}
