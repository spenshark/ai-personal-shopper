package spring.ai.converter;

import spring.ai.dto.ChatResponseDto;
import spring.ai.entity.ChatMessage;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;
import spring.ai.entity.enums.Sender;

public class ChatConverter {

    public static ChatResponseDto toChatResponse(String response) {
        return new ChatResponseDto(response);
    }

    public static ChatRoom toChatRoom(User user) {
        return ChatRoom.builder()
                .user(user)
                .build();
    }

    public static ChatMessage toChatMessage(ChatRoom chatRoom, Sender sender, String message) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(message)
                .build();
    }
}
