package spring.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import spring.ai.dto.UserRequestDto;
import spring.ai.dto.UserResponseDto;
import spring.ai.global.base.ApiResponse;
import spring.ai.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 api", description = "사용자 회원가입을 처리합니다.")
    @PostMapping("/signup")
    public ApiResponse<UserResponseDto.Signup> signup(
        @RequestPart("request") UserRequestDto.Signup request) {
        return ApiResponse.onSuccess(userService.signup(request));
    }
}
