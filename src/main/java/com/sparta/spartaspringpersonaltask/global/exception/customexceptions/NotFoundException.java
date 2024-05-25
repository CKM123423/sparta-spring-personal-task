package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
