package com.sparta.spartaspringpersonaltask.global.exceptions.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{

    // 400번대
    ALREADY_DELETED_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 삭제된 일정입니다."),
    INVALID_PASSWORD_EXCEPTION(HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "선택한 일정을 찾지 못하였습니다.");

    private final HttpStatus status;
    private final String message;
}
