package com.wuan.attendance.model;

import java.io.Serializable;

public class Group implements Serializable {
    private Integer id;
    private String name;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
