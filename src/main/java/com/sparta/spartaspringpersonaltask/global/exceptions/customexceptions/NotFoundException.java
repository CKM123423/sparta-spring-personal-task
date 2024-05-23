package com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
