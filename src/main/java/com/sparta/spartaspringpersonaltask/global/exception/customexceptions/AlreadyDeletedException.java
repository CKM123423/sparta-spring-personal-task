package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
