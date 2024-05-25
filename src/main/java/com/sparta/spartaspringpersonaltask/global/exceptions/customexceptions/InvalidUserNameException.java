package com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(String message) {
        super(message);
    }
}
