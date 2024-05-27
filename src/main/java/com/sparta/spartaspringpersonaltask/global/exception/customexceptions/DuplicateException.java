package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
