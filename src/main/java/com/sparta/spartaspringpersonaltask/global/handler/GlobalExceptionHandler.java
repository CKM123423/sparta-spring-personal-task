package com.sparta.spartaspringpersonaltask.global.handler;

import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.InvalidPasswordException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.global.exceptions.error.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성검사 실패시 오류문 출력(400)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleValidationExceptions(BindException ex) {
        List<String> errors = new ArrayList<>();

        // 발생한 오류들을 전부 돌면서 오류문을 전부 추가
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    // 이미 삭제된 데이터를 삭제할때(400)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<Object> handleAlreadyDeletedException(AlreadyDeletedException e) {
        return ErrorCode.ALREADY_DELETED_EXCEPTION.buildResponse();
    }

    // 비밀번호가 틀렸을때(401)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException e) {
        return ErrorCode.INVALID_PASSWORD_EXCEPTION.buildResponse();
    }


    // 선택한 일정을 찾을 수 없을때(404)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return ErrorCode.NOT_FOUND_EXCEPTION.buildResponse();
    }

}
