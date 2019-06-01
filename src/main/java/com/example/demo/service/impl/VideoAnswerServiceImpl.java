package com.example.demo.service.impl;

import com.example.demo.dao.UserMes;
import com.example.demo.dao.VideoAnswer;
import com.example.demo.dao.VideoAppMes;
import com.example.demo.dto.SpecificVideoAnswer;
import com.example.demo.dto.VerifyVideoAnswer;
import com.example.demo.dto.ZSVideoMyAnswer;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.mapper.VideoAnswerMapper;
import com.example.demo.mapper.VideoAppMesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoAnswerServiceImpl {
    @Autowired
    VideoAppMesMapper videoAppMesMapper;
    @Autowired
    VideoAnswerMapper videoAnswerMapper;
    @Autowired
    UserMesMapper userMesMapper;

    /**
     * 添加视频答案*******************************************************没写完
     * */
    public int insertVideoAnswer(VideoAnswer videoAnswer){
        //先查询是否有该条记录
//        System.out.println(videoAnswer.getVedioappId());
        List<VideoAnswer> videoAnswers = videoAnswerMapper.chachong(videoAnswer.getUserId(),videoAnswer.getVedioappId());
        if(videoAnswers.isEmpty()){
            videoAnswerMapper.insert(videoAnswer);
            return 1;
        }else {
            return 0;
        }
    }
    /**
     * 根据userId查询他的所有video答案
     * */
    public List<ZSVideoMyAnswer> getVidelAnswerByUserId(String userId){
        List<ZSVideoMyAnswer> answers = new ArrayList<ZSVideoMyAnswer>();
        List<VideoAnswer> originAnswers = videoAnswerMapper.selectByUserId(userId);
        for (VideoAnswer v: originAnswers
             ) {
            ZSVideoMyAnswer zsVideoMyAnswer = new ZSVideoMyAnswer();
            zsVideoMyAnswer.setAnswerTime(v.getSubtime());
            zsVideoMyAnswer.setVideoAnswerId(v.getVedioappId());
            int  appId = v.getVedioappId();
            VideoAppMes videoAppMes = videoAppMesMapper.selectByPrimaryKey(appId);
            zsVideoMyAnswer.setIntroduce(videoAppMes.getIntroduction());
            if(videoAppMes.getSolve()==1) {
                zsVideoMyAnswer.setOk(videoAppMes.getRightUserId() == userId ? "已采纳" : "未采纳");
            }else {
                zsVideoMyAnswer.setOk("审核中");
            }
            zsVideoMyAnswer.setSubject(videoAppMes.getAppSubject());
            String appUserId = videoAppMes.getUserId();
            UserMes um = userMesMapper.selectByPrimaryKey(appUserId);
            String appUserName = um.getNickname();
            zsVideoMyAnswer.setAskUserName(appUserName);
            answers.add(zsVideoMyAnswer);

        }
        return answers;
    }
    /**
     * 根据请求id和用户id返回用户的视频答案信息
     * */
    public SpecificVideoAnswer getAnswerByAppId_UserId(int videoAppId,String userId){
        SpecificVideoAnswer specificVideoAnswer = new SpecificVideoAnswer();
        int sum = videoAnswerMapper.selectSumByAppId(videoAppId);
        //查询该用户的答案
        VideoAnswer videoAnswer = videoAnswerMapper.selectByAnswerUserId(videoAppId,userId);
        int count = 0;
        List<VideoAnswer> videoAnswers = videoAnswerMapper.selectByVideoAppId(videoAppId);
        //查看当前用户答案的序号
        for (VideoAnswer v: videoAnswers
             ) {
            count++;
            if(v.getUserId()==userId){
                break;
            }
        }
        VideoAppMes videoAppMes = videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
        UserMes answerUserMes = userMesMapper.selectByPrimaryKey(userId);
        UserMes askUserMes = userMesMapper.selectByPrimaryKey(videoAppMes.getUserId());

        specificVideoAnswer.setAnswerTIme(videoAnswer.getSubtime());
        specificVideoAnswer.setAnswerUserId(userId);
        specificVideoAnswer.setAnswerUserName(answerUserMes.getNickname());
        specificVideoAnswer.setAskTime(videoAppMes.getAsktime());
        specificVideoAnswer.setCount(count);
        specificVideoAnswer.setOriginUserId(askUserMes.getUserId());
        specificVideoAnswer.setOriginUserName(askUserMes.getNickname());
        specificVideoAnswer.setPicAppId(videoAppMes.getAppId());
        specificVideoAnswer.setSubject(videoAppMes.getAppSubject());
        specificVideoAnswer.setSum(sum);
        specificVideoAnswer.setVideoAnswerId(videoAnswer.getVedioId());

        specificVideoAnswer.setVideo("http://127.0.0.1:8080/"+videoAnswer.getVedioAdress());
        return specificVideoAnswer;
    }
    /**
     * 根据AppId和第几个返回视频答案信息
     * */
    public SpecificVideoAnswer selectByAppIdAndCount(int videoAppId,int count){
        SpecificVideoAnswer specificVideoAnswer = new SpecificVideoAnswer();
        int sum = videoAnswerMapper.selectSumByAppId(videoAppId);
        List<VideoAnswer> videoAnswers = videoAnswerMapper.selectByVideoAppId(videoAppId);
        if(videoAnswers.isEmpty()){
            return null;
        }else {
            VideoAnswer videoAnswer = videoAnswers.get(count - 1);

            VideoAppMes videoAppMes = videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
            UserMes answerUserMes = userMesMapper.selectByPrimaryKey(videoAnswer.getUserId());
            UserMes askUserMes = userMesMapper.selectByPrimaryKey(videoAppMes.getUserId());

            specificVideoAnswer.setAnswerTIme(videoAnswer.getSubtime());
            specificVideoAnswer.setAnswerUserId(videoAnswer.getUserId());
            specificVideoAnswer.setAnswerUserName(answerUserMes.getNickname());
            specificVideoAnswer.setAskTime(videoAppMes.getAsktime());
            specificVideoAnswer.setCount(count);
            specificVideoAnswer.setOriginUserId(askUserMes.getUserId());
            specificVideoAnswer.setOriginUserName(askUserMes.getNickname());
            specificVideoAnswer.setPicAppId(videoAppMes.getAppId());
            specificVideoAnswer.setSubject(videoAppMes.getAppSubject());
            specificVideoAnswer.setSum(sum);
            specificVideoAnswer.setVideoAnswerId(videoAnswer.getVedioId());
            specificVideoAnswer.setVideo("http://127.0.0.1:8080/" + videoAnswer.getVedioAdress());
            return specificVideoAnswer;
        }
    }
    /**
     * 审核展示的视频信息
     * */
    public VerifyVideoAnswer vv(){
        VerifyVideoAnswer verifyVideoAnswer = new VerifyVideoAnswer();
        ArrayList<VideoAnswer> videoAnswers = videoAnswerMapper.selectNoVerifyAnswer();
        if(videoAnswers.size()==0){
            return null;
        }else {
            VideoAnswer videoAnswer = videoAnswers.get(0);
            VideoAppMes videoAppMes = videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
            verifyVideoAnswer.setVideo("http://127.0.0.1:8080/" + videoAnswer.getVedioAdress());
            verifyVideoAnswer.setSubject(videoAppMes.getAppSubject());
            verifyVideoAnswer.setIntroduce(videoAppMes.getIntroduction());
            verifyVideoAnswer.setPicAnswerId(videoAnswer.getVedioId());
            return verifyVideoAnswer;
        }
    }
    /**
     * 视频答案审核通过
     * */
    public int changeState(int videoAnswerId,int state){
        return videoAnswerMapper.changeState(videoAnswerId,state);
    }
}
