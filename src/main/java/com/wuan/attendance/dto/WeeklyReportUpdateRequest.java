package com.wuan.attendance.dto;

public class WeeklyReportUpdateRequest {
    private String email;
    private Integer weekNumber;
    private WeeklyReportDTO weeklyReport;

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

    public WeeklyReportDTO getWeeklyReport() {
        return weeklyReport;
    }

    public void setWeeklyReport(WeeklyReportDTO weeklyReport) {
        this.weeklyReport = weeklyReport;
    }
}

