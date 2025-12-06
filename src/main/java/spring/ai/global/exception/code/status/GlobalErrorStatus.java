package spring.ai.global.exception.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import spring.ai.global.exception.code.ApiCodeDto;
import spring.ai.global.exception.code.ApiErrorCodeInterface;

@Getter
@AllArgsConstructor
public enum GlobalErrorStatus implements ApiErrorCodeInterface {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),
    _INTERNAL_PAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "페이지 에러, 0 이상의 페이지를 입력해주세요"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS5001", "데이터베이스 오류 발생"),

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4041", "사용자를 찾을 수 없습니다."),

    // Recipe 관련
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECIPE4041", "레시피를 찾을 수 없습니다."),

    // 재고 관련
    INVENTORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "INVENTORY4001", "재고에 해당 재료가 없습니다. 요리를 실행할 수 없습니다."),
    INVENTORY_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "INVENTORY4002", "요리에 필요한 재료의 양이 부족합니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public ApiCodeDto getErrorCode() {
        return ApiCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}