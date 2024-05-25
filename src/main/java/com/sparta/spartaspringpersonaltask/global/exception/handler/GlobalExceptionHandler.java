package com.sparta.spartaspringpersonaltask.global.exception.handler;

import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // 유효성검사 실패시 오류문 출력
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        // 발생한 오류들을 전부 돌면서 오류문을 전부 추가
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    // 이미 삭제된 데이터를 삭제할때
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<Object> handleAlreadyDeletedException(AlreadyDeletedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 입력값이 틀렸을때
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<Object> handleInvalidException(InvalidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    // 해당 정보를 찾을 수 없을때
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
