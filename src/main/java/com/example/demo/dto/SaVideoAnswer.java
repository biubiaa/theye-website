package com.example.demo.dto;

import java.util.ArrayList;

public class SaVideoAnswer {
    private Integer answerId;
    private String subject;
    private String introduce;
    private String answerUserId;
    private String nickName;
    private String video;
    private Integer awsome;
    private String askUserId;

    public String getAskUserId() {
        return askUserId;
    }

    public void setAskUserId(String askUserId) {
        this.askUserId = askUserId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
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

    public String getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(String answerUserId) {
        this.answerUserId = answerUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getAwsome() {
        return awsome;
    }

    public void setAwsome(Integer awsome) {
        this.awsome = awsome;
    }
}
