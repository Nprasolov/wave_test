package com.example.wave_first.entity;

import java.util.Date;
import java.util.Set;

public class ScheduleRest {
    private String presTitle;
    private String presTheme;
    private Long roomName;
    private Set<String> users;
    private Date startTime;
    private Date endTime;

    public String getPresTitle() {
        return presTitle;
    }

    public void setPresTitle(String presTitle) {
        this.presTitle = presTitle;
    }

    public String getPresTheme() {
        return presTheme;
    }

    public void setPresTheme(String presTheme) {
        this.presTheme = presTheme;
    }

    public Long getRoomName() {
        return roomName;
    }

    public void setRoomName(Long roomName) {
        this.roomName = roomName;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
