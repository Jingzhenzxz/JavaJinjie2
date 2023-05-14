package com.wuan.attendance.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String password) {
        super(password);
    }
}
