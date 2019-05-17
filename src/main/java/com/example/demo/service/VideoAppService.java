package com.example.demo.service;

import com.example.demo.dao.VideoAnswer;

public interface VideoAppService {
//    //发布申请,更新个人金钱，生成账单
//    public int addApp(VideoAppMes videoAppMes);
//    //撤销申请
//    public int deleteApp(int appId);
    //添加答案
    public int addVideoAnswer(VideoAnswer videoAnswer);
    //删除答案
    public int deleteVideoAnswer(int videoAnswerId);
    //查看答案列表
    public int checkAnswerList(int videoAppId);

}
