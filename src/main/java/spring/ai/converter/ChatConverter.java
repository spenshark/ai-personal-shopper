package spring.ai.converter;

import spring.ai.dto.ChatResponseDto;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;

public class ChatConverter {

    public static ChatResponseDto toChatResponse(String response) {
        return new ChatResponseDto(response);
    }

    public static ChatRoom toChatRoom(User user) {
        return ChatRoom.builder()
                .user(user)
                .build();
    }
}
