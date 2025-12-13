package spring.ai.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.ai.entity.ChatMessage;
import spring.ai.entity.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10ByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
