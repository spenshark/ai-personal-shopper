package spring.ai.converter;

import spring.ai.dto.UserResponseDto;
import spring.ai.entity.User;

public class UserConverter {

    public static User toUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static UserResponseDto.Signup toSignupDto(Long userId, Long chatRoomId) {
        return new UserResponseDto.Signup(
                "회원가입이 성공적으로 완료되었습니다.",
                userId,
                chatRoomId
        );
    }
}
