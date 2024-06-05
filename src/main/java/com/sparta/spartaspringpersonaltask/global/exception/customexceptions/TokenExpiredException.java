package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}