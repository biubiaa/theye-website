package com.example.demo.util;

import com.example.demo.dao.LevelScore;
import com.example.demo.dao.TimeSatistic;
import com.example.demo.dao.UserMes;
import com.example.demo.mapper.LevelScoreMapper;
import com.example.demo.mapper.TimeSatisticMapper;
import com.example.demo.mapper.UserMesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SatisticUserTime extends Thread {
    @Autowired
    TimeSatisticMapper timeSatisticMapper;
    @Autowired
    LevelScoreMapper levelScoreMapper;
    public void run(){
        while (true){
            List<TimeSatistic>tss = timeSatisticMapper.selectAll();
            if(tss.size()==0){//没有账户登录
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {//有账户在登录
                for (TimeSatistic ts: tss
                     ) {
                    if(((new Date().getTime()-ts.getLatestoptime().getTime()))>1000*60*10){
                        //超过时间,记录时间
                        timeSatisticMapper.deleteByPrimaryKey(ts.getId());
                        String userId = ts.getUserId();
                        LevelScore levelScore = levelScoreMapper.selectSumScoreByUserId(userId);
                        levelScore.setLoginTime((int) (levelScore.getLoginTime()+(ts.getLatestoptime().getTime()-ts.getLogintime().getTime())/1000/60));
                        levelScoreMapper.updateByPrimaryKey(levelScore);
                    }
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
