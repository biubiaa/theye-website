package com.example.demo.dto;

import java.util.ArrayList;

public class VerifyPicAnswer {
    public int picAnswerId;
    public String subject;
    public String introduce;
    public ArrayList<String> imgs;


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

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }


}
