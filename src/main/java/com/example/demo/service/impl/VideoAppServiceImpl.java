package com.example.demo.service.impl;

import com.example.demo.dao.BillSchedule;
import com.example.demo.dao.UserMes;
import com.example.demo.dao.VideoAnswer;
import com.example.demo.dao.VideoAppMes;
import com.example.demo.dto.ZSVideoAppMes;
import com.example.demo.mapper.BillScheduleMapper;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.mapper.VideoAnswerMapper;
import com.example.demo.mapper.VideoAppMesMapper;
import com.example.demo.service.VideoAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoAppServiceImpl implements VideoAppService {
    @Autowired
    VideoAppMesMapper videoAppMesMapper;
    @Autowired
    BillScheduleMapper billScheduleMapper;
    @Autowired
    UserMesMapper userMesMapper;
    @Autowired
    VideoAnswerMapper videoAnswerMapper;

    /**
     * 添加video发布申请
     * //     *
     */
//    @Override
    public int addApp(VideoAppMes videoAppMes) {
        String userId = videoAppMes.getUserId();
        //扣掉个人金钱
        UserMes userMes = userMesMapper.selectByPrimaryKey(userId);
        if (userMes.getMoney() < videoAppMes.getMoney()) {
            return 0;
        }
        userMes.setMoney(userMes.getMoney() - videoAppMes.getMoney());
        //先插入再获取id
        videoAppMesMapper.insert(videoAppMes);
        int appId = videoAppMesMapper.selectMaxappid();
        VideoAppMes videoAppMes1 = videoAppMesMapper.selectByPrimaryKey(appId);
        //更新bill
        BillSchedule bs = new BillSchedule();
        bs.setAppId(appId);
        bs.setCtime(videoAppMes1.getAsktime());
        bs.setMoney(videoAppMes1.getMoney());
        bs.setType(3);
        billScheduleMapper.insert(bs);
        return 1;
    }

    /**
     * 删除video申请
     */
//    @Override
//    public int deleteApp(int appId) {
//        try {
//            //删除申请,返还金额
//            VideoAppMes videoAppMes = videoAppMesMapper.selectByPrimaryKey(appId);
//            double money = videoAppMes.getMoney();
//            String userId = videoAppMes.getUserId();
//            UserMes um = userMesMapper.selectByUserId(userId);
//            um.setMoney(um.getMoney() + money);
//            videoAppMesMapper.deleteByPrimaryKey(appId);
//            //增加bill
//            BillSchedule bs = new BillSchedule();
//            bs.setType(4);
//            bs.setMoney(money);
//            bs.setAppId(videoAppMes.getAppId());
//            billScheduleMapper.insert(bs);
//        }catch (Exception e){
//            return 0;
//        }
//        return 1;
//    }
    //添加视频答案
    @Override
    public int addVideoAnswer(VideoAnswer videoAnswer) {
        videoAnswerMapper.insert(videoAnswer);
        return 0;
    }

    //删除视频答案
    @Override
    public int deleteVideoAnswer(int videoAnswerId) {
        videoAnswerMapper.deleteByPrimaryKey(videoAnswerId);
        return 0;
    }

    @Override
    public int checkAnswerList(int videoAppId) {
        return 0;
    }

    /**
     * 删除视频请求
     */
    public int deleteVideoApp(int appId) {
        return videoAppMesMapper.deleteByPrimaryKey(appId);
    }

    /**
     * 查询by userId
     */
    public List<ZSVideoAppMes> selectByUserId(String userId) {
        List<VideoAppMes> videoAppMes = videoAppMesMapper.selectByUserId(userId);
        List<ZSVideoAppMes> zsVideoAppMes = new ArrayList<ZSVideoAppMes>();
        for (VideoAppMes v : videoAppMes
        ) {
            ZSVideoAppMes zsVideoAppMes1 = new ZSVideoAppMes();
            zsVideoAppMes1.setIntroduce(v.getIntroduction());
            zsVideoAppMes1.setAppId(v.getAppId());
            zsVideoAppMes1.setAppSubject(v.getAppSubject());
            zsVideoAppMes1.setAppTime(v.getAsktime());
            zsVideoAppMes1.setMoney(v.getMoney());
            zsVideoAppMes1.setRegion(v.getRegion());
            zsVideoAppMes1.setCount(videoAnswerMapper.selectCountByAppId(v.getAppId()));
            zsVideoAppMes1.setRightUserId(v.getRightUserId());
            zsVideoAppMes1.setSolve(v.getSolve());
            zsVideoAppMes.add(zsVideoAppMes1);

        }
        return zsVideoAppMes;
    }

    /**
     * 根据城市查询
     */
    public List<ZSVideoAppMes> selectByCity(String region, String type) {
        int solve;
        if (type.equals("已解决"))
            solve = 1;
        else solve = 0;
        List<VideoAppMes> videoAppMes = videoAppMesMapper.selectByCity(region, solve);
        List<ZSVideoAppMes> zsVideoAppMes = new ArrayList<ZSVideoAppMes>();
        for (VideoAppMes v : videoAppMes
        ) {
            ZSVideoAppMes zsVideoAppMes1 = new ZSVideoAppMes();
            zsVideoAppMes1.setIntroduce(v.getIntroduction());
            zsVideoAppMes1.setAppId(v.getAppId());
            zsVideoAppMes1.setAppSubject(v.getAppSubject());
            zsVideoAppMes1.setAppTime(v.getAsktime());
            zsVideoAppMes1.setMoney(v.getMoney());
            zsVideoAppMes1.setRegion(v.getRegion());
            zsVideoAppMes1.setRightUserId(v.getRightUserId());
            zsVideoAppMes1.setSolve(v.getSolve());
            int sum = videoAnswerMapper.selectSumByAppId(v.getAppId());
            zsVideoAppMes1.setAnswerCount(sum);
            zsVideoAppMes.add(zsVideoAppMes1);


        }
        return zsVideoAppMes;
    }
    public int changeAppState(int appId,int state){
        return videoAppMesMapper.changeAppState(appId,state);
    }
    /**
     * 获取一个申请的userId
     * */
    public String getUserIdByAppId(int appId){
        return videoAppMesMapper.selectUserIdByAppId(appId);
    }
}
