package com.example.demo.service.impl;

import com.example.demo.dao.UserMes;
import com.example.demo.dao.VideoAnswer;
import com.example.demo.dao.VideoAppMes;
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
        String userId = videoAnswer.getUserId();
        int picAppId = videoAnswer.getVedioappId();
    return 1;
    }
    /**
     * 根据userId查询video答案
     *
     * */
    public List<ZSVideoMyAnswer> getVidelAnswerByUserId(String userId){
        List<ZSVideoMyAnswer> answers = new ArrayList<ZSVideoMyAnswer>();
        List<VideoAnswer> originAnswers = videoAnswerMapper.selectByUserId(userId);
        for (VideoAnswer v: originAnswers
             ) {
            ZSVideoMyAnswer zsVideoMyAnswer = new ZSVideoMyAnswer();
            zsVideoMyAnswer.setAnswerTime(v.getSubtime());
            zsVideoMyAnswer.setVideoAnswerId(v.getVedioId());
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
}
