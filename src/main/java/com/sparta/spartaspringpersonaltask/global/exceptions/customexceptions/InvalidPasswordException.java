package com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
