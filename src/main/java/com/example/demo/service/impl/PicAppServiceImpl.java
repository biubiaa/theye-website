package com.example.demo.service.impl;

import com.example.demo.dao.BillSchedule;
import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.PicAppMes;
import com.example.demo.dao.UserMes;
import com.example.demo.dto.ZSPicAppMes;
import com.example.demo.mapper.PicAppMesMapper;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.service.BillService;
import com.example.demo.service.PicAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class PicAppServiceImpl implements PicAppService {
    @Autowired
    PicAppMesMapper picAppMesMapper;
    @Autowired
    UserMesMapper userMesMapper;
    @Autowired
    BillService billService;
    /**
     * 插入图片请求
     * */
    @Override
    public int addPicApp(PicAppMes picAppMes) {
        String userId = picAppMes.getUserId();
        UserMes userMes = userMesMapper.selectByPrimaryKey(userId);
        if(userMes.getMoney()<picAppMes.getMoney()){
            return 0;
        }
        userMes.setMoney(userMes.getMoney()-picAppMes.getMoney());
        //先插入再获取id
        picAppMesMapper.insert(picAppMes);
        int appId = picAppMesMapper.selectMaxappid();
        PicAppMes picAppMes1 = picAppMesMapper.selectByPrimaryKey(appId);
        BillSchedule bs = new BillSchedule();
        bs.setAppId(appId);
        bs.setCtime(picAppMes1.getAppTime());
        bs.setMoney(picAppMes1.getMoney());
        bs.setType(3);

        return billService.insertBill(bs);
    }
    /**
     * 获取发布时间
     * */
    public Date getfabuDate(String userId,String subject){
        return picAppMesMapper.getfabuDate(userId,subject);
    }
    /**
     * 根据用户id获取已发布的请求
     * */
    public List<ZSPicAppMes> getPicAppsByUserId(String userId){
        List<ZSPicAppMes> zsPicAppMes = new ArrayList<ZSPicAppMes>();
        for (PicAppMes picAppMes: picAppMesMapper.getAppsByUserId(userId)
             ) {
            ZSPicAppMes zsPicAppMes1 = new ZSPicAppMes();
            zsPicAppMes1.setIntroduce(picAppMes.getIntroduce());
            zsPicAppMes1.setAppId(picAppMes.getAppId());
            zsPicAppMes1.setAppSubject(picAppMes.getAppSubject());
            zsPicAppMes1.setAppTime(picAppMes.getAppTime());
            zsPicAppMes1.setMoney(picAppMes.getMoney());
            zsPicAppMes1.setRegion(picAppMes.getRegion());
            zsPicAppMes1.setSolve(picAppMes.getSolve());
            zsPicAppMes.add(zsPicAppMes1);
        }
        return zsPicAppMes;
    }
    /**
     * 根据城市查询
     * */
    public List<ZSPicAppMes> selectByCity(String region, String type){
        int tmp;
        if(type.equals("已解决"))
            tmp = 1;
        else tmp = 0;
        List<PicAppMes> picAppMes = picAppMesMapper.selectBycity(region,tmp);
        List<ZSPicAppMes> zsPicAppMes = new ArrayList<ZSPicAppMes>();
        for (PicAppMes picAppMes1: picAppMes
        ) {
            ZSPicAppMes zsPicAppMes1 = new ZSPicAppMes();
            zsPicAppMes1.setIntroduce(picAppMes1.getIntroduce());
            zsPicAppMes1.setAppId(picAppMes1.getAppId());
            zsPicAppMes1.setAppSubject(picAppMes1.getAppSubject());
            zsPicAppMes1.setAppTime(picAppMes1.getAppTime());
            zsPicAppMes1.setMoney(picAppMes1.getMoney());
            zsPicAppMes1.setRegion(picAppMes1.getRegion());
            zsPicAppMes1.setSolve(picAppMes1.getSolve());
            zsPicAppMes.add(zsPicAppMes1);
        }
        return zsPicAppMes;

    }
    @Override
    public int deleteApp(int appId) {

        return picAppMesMapper.deleteByPrimaryKey(appId);
    }

    @Override
    public int addPicAppAnswer(PicAnswer picAnswer) {
        return 0;
    }

    @Override
    public int deletePicAppAnswer(int PicAnswerId) {
        return 0;
    }
}
