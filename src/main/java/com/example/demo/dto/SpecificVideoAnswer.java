package com.example.demo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * 视频答案界面展示信息
 * */
public class SpecificVideoAnswer {
    private int videoAnswerId;
    private String subject;
    private String originUserId;

    private String originUserName;
    private String answerUserId;
    private String answerUserName;
    private String askTime;
    private String answerTIme;
    private String video;
    private int count;
    private int sum;
    private int picAppId;

    public int getVideoAnswerId() {
        return videoAnswerId;
    }

    public void setVideoAnswerId(int videoAnswerId) {
        this.videoAnswerId = videoAnswerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOriginUserId() {
        return originUserId;
    }

    public void setOriginUserId(String originUserId) {
        this.originUserId = originUserId;
    }

    public String getOriginUserName() {
        return originUserName;
    }

    public void setOriginUserName(String originUserName) {
        this.originUserName = originUserName;
    }

    public String getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(String answerUserId) {
        this.answerUserId = answerUserId;
    }

    public String getAnswerUserName() {
        return answerUserName;
    }

    public void setAnswerUserName(String answerUserName) {
        this.answerUserName = answerUserName;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(Date askTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        this.askTime = simpleDateFormat.format(askTime);
    }

    public String getAnswerTIme() {
        return answerTIme;
    }

    public void setAnswerTIme(Date answerTIme) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        this.answerTIme = simpleDateFormat.format(answerTIme);
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPicAppId() {
        return picAppId;
    }

    public void setPicAppId(int picAppId) {
        this.picAppId = picAppId;
    }
}
