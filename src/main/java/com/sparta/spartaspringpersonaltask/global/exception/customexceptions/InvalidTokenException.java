package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}