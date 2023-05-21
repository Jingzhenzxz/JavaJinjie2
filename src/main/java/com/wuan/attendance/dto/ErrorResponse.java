package com.wuan.attendance.dto;

// 如果需要返回具有一致结构的错误响应，那就需要这个类。
public class ErrorResponse {
    private String message;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
