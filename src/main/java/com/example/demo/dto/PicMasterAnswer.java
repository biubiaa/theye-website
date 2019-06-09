package com.example.demo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PicMasterAnswer {
    private String subject;
    private String answerTime;
    private String nickName;
    private String link;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        this.answerTime = simpleDateFormat.format(answerTime);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
