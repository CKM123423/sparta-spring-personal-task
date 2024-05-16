package com.sparta.spartaspringpersonaltask.exceptions.customexceptions;

public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
