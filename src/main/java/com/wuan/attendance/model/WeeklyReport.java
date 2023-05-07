package com.wuan.attendance.model;

import java.io.Serializable;
import java.util.Date;

public class WeeklyReport implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer groupId;
    private Integer weekNumber;
    private String contentCompleted;
    private String contentProblems;
    private String contentPlan;
    private String contentLink;
    private String status;
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getContentCompleted() {
        return contentCompleted;
    }

    public void setContentCompleted(String contentCompleted) {
        this.contentCompleted = contentCompleted;
    }

    public String getContentProblems() {
        return contentProblems;
    }

    public void setContentProblems(String contentProblems) {
        this.contentProblems = contentProblems;
    }

    public String getContentPlan() {
        return contentPlan;
    }

    public void setContentPlan(String contentPlan) {
        this.contentPlan = contentPlan;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}