package com.sparta.spartaspringpersonaltask.exceptions.customexceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
