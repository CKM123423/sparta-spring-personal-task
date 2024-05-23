package com.sparta.spartaspringpersonaltask.global.handler;

import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.InvalidPasswordException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.global.exceptions.error.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이미 삭제된 데이터를 삭제할때(400)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<Object> handleAlreadyDeletedException(AlreadyDeletedException e) {
        ErrorCode errorCode = ErrorCode.ALREADY_DELETED_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getMessage());
    }

    // 비밀번호가 틀렸을때(401)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException e) {
        ErrorCode errorCode = ErrorCode.INVALID_PASSWORD_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getMessage());
    }

    // 선택한 일정을 찾을 수 없을때(404)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getMessage());
    }
}
