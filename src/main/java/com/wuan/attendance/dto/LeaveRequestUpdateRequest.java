package com.wuan.attendance.dto;

public class LeaveRequestUpdateRequest {
    private String email;
    private Integer weekNumber;
    private LeaveRequestDTO leaveRequestDTO;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public LeaveRequestDTO getLeaveRequestDTO() {
        return leaveRequestDTO;
    }

    public void setLeaveRequestDTO(LeaveRequestDTO leaveRequestDTO) {
        this.leaveRequestDTO = leaveRequestDTO;
    }
}
