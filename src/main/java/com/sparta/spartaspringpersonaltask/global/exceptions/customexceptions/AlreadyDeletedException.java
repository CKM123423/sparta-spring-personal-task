package com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions;

public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
