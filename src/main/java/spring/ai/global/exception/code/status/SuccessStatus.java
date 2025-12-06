package spring.ai.global.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import spring.ai.global.exception.code.ApiCodeDto;
import spring.ai.global.exception.code.ApiSuccessCodeInterface;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements ApiSuccessCodeInterface {
    // For test
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "요청 성공 및 리소스 생성됨")
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = true;
    private final String code;
    private final String message;

    @Override
    public ApiCodeDto getSuccessCode() {
        return ApiCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}