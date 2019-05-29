package com.example.demo.dto;

import java.util.ArrayList;

public class SaPicAnswer {
    private Integer answerId;
    private String subject;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    private String introduce;
    private String answerUserId;
    private String nickName;
    private ArrayList<String> imgs;
    private Integer awsome;

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

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public Integer getAwsome() {
        return awsome;
    }

    public void setAwsome(Integer awsome) {
        this.awsome = awsome;
    }
}
