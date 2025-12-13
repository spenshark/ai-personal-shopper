package spring.ai.service;

import spring.ai.dto.UserRequestDto;
import spring.ai.dto.UserResponseDto;

public interface UserService {
    UserResponseDto.Signup signup(UserRequestDto.Signup request);

}
