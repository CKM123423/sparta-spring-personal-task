package com.sparta.spartaspringpersonaltask.exceptions.customexceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
