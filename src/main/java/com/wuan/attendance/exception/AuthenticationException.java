package com.wuan.attendance.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException (String message) {
        super(message);
    }
}