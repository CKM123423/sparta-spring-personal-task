package com.sparta.spartaspringpersonaltask.global.exception.customexceptions;

@Deprecated
public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
