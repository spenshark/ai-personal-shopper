package spring.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser(User user);
}
