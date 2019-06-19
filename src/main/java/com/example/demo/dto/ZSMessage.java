package com.example.demo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZSMessage {
    private Integer id;
    private String userId;
    private String ctime;
    private String state;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(Date appTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        this.ctime = simpleDateFormat.format(appTime);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
