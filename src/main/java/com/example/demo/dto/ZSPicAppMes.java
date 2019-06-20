package com.example.demo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZSPicAppMes {
    private int appId;
    private String appSubject;
    private String appTime;
    private Double money;
    private Integer solve;
    private String rightUserId;
    private String region;
    private int count;//回答的个数
    private int answerCount;

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    private String introduce;

    public String getAppSubject() {
        return appSubject;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setAppSubject(String appSubject) {
        this.appSubject = appSubject;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        this.appTime = simpleDateFormat.format(appTime);
//        this.appTime = /appTime;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getSolve() {
        return solve;
    }

    public void setSolve(Integer solve) {
        this.solve = solve;
    }

    public String getRightUserId() {
        return rightUserId;
    }

    public void setRightUserId(String rightUserId) {
        this.rightUserId = rightUserId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");


}
