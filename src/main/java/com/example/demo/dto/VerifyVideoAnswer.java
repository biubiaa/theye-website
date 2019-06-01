package com.example.demo.dto;

import java.util.ArrayList;

public class VerifyVideoAnswer {
    public int picAnswerId;
    public String subject;
    public String introduce;
    public String video;

    public int getPicAnswerId() {
        return picAnswerId;
    }

    public void setPicAnswerId(int picAnswerId) {
        this.picAnswerId = picAnswerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
