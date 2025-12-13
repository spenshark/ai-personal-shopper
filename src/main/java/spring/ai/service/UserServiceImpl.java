package spring.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ai.converter.ChatConverter;
import spring.ai.converter.UserConverter;
import spring.ai.dto.UserRequestDto;
import spring.ai.dto.UserResponseDto;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;
import spring.ai.repository.ChatRoomRepository;
import spring.ai.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public UserResponseDto.Signup signup(UserRequestDto.Signup request) {
        User user = UserConverter.toUser(request.email(), request.password());
        userRepository.save(user);

        ChatRoom chatRoom = ChatConverter.toChatRoom(user);
        chatRoomRepository.save(chatRoom);

        user.setChatRoom(chatRoom);

        return UserConverter.toSignupDto(user.getId(), chatRoom.getId());
    }
}