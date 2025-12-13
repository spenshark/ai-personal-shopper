package spring.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ai.converter.ChatConverter;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;
import spring.ai.entity.ChatMessage;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;
import spring.ai.entity.enums.Sender;
import spring.ai.global.exception.RestApiException;
import spring.ai.global.exception.code.status.GlobalErrorStatus;
import spring.ai.repository.ChatMessageRepository;
import spring.ai.repository.ChatRoomRepository;
import spring.ai.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatResponseDto chat(ChatRequestDto request) {
        Long userId = 1L; // Temporary hardcoded user ID
        ChatRoom chatRoom = getChatRoom(userId);

        saveMessage(chatRoom, Sender.USER, request.message());

        List<Message> history = getConversationHistory(chatRoom);

        String aiResponse = chatClient.prompt()
                .messages(history)
                .call()
                .content();

        saveMessage(chatRoom, Sender.AI, aiResponse);
        return ChatConverter.toChatResponse(aiResponse);
    }

    private ChatRoom getChatRoom(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus.USER_NOT_FOUND));

        return chatRoomRepository.findByUser(user)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus.CHAT_ROOM_NOT_FOUND));
    }

    private List<Message> getConversationHistory(ChatRoom chatRoom) {
        List<ChatMessage> history = chatMessageRepository.findTop10ByChatRoomOrderByCreatedAtDesc(chatRoom);
        Collections.reverse(history); // Sort chronologically

        return history.stream()
                .map(m -> {
                    if (m.getSender() == Sender.USER) {
                        return (Message) new UserMessage(m.getMessage());
                    } else {
                        return (Message) new AssistantMessage(m.getMessage());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void saveMessage(ChatRoom chatRoom, Sender sender, String message) {
        ChatMessage chatMessage = ChatConverter.toChatMessage(chatRoom, sender, message);
        chatMessageRepository.save(chatMessage);
    }
}