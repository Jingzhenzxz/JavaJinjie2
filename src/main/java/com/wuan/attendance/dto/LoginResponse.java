package com.wuan.attendance.dto;

public class LoginResponse {
    private String token;
    private UserDTO userDTO;

    public LoginResponse(UserDTO userDTO, String token) {
        this.token = token;
        this.userDTO = userDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
