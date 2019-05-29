package com.example.demo.service.impl;

import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.PicAppMes;
import com.example.demo.dto.SaPicAnswer;
import com.example.demo.dao.UserMes;
import com.example.demo.mapper.PicAnswerMapper;
import com.example.demo.mapper.PicAppMesMapper;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

@Service
public class SaServiceImpl {
    @Autowired
    PicAnswerMapper picAnswerMapper;
    @Autowired
    UserMesMapper userMesMapper;
    @Autowired
    PicAppMesMapper picAppMesMapper;
    /**
     * 根据用户id返回随机的图片答案
     * */
    public SaPicAnswer getRandomPic(String userId) throws FileNotFoundException {
        Jedis jedis = (Jedis) SpringUtil.getBean("jedis");
        Set<String > picAnswerList = jedis.smembers(userId);
        ArrayList<Integer> allAuthId = picAnswerMapper.getAllAuthId();
        if (allAuthId.size()==0){
            return null;
        }
        SaPicAnswer saPicAnswer = new SaPicAnswer();
        if(picAnswerList.size()==0){//若是第一次访问
            Integer authId = allAuthId.get(0);
            jedis.sadd(userId,authId.toString());
            jedis.expire(userId,300);
            PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(authId);
            UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
            PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
            saPicAnswer.setAnswerId(authId);
            saPicAnswer.setAnswerUserId(picAnswer.getUserId());
            saPicAnswer.setAwsome(picAnswer.getAwosome());
            saPicAnswer.setIntroduce(picAppMes.getIntroduce());
            saPicAnswer.setSubject(picAppMes.getAppSubject());
            saPicAnswer.setNickName(userMes.getNickname());
            ArrayList<String> imgPaths = new ArrayList<String>();
            File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
            File pathth = new File(path1.getAbsolutePath(), "static/images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/");
            File[] files = pathth.listFiles();
            for (File f : files
            ) {
                String fileName = f.getName();
                String imgPath = "images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/" + fileName;
                imgPaths.add(imgPath);
            }
            saPicAnswer.setImgs(imgPaths);
            return saPicAnswer;

        }else {//不是第一次访问
            int count = 0;
            for (Integer authId : allAuthId
            ) {
                if (!picAnswerList.contains(authId)) {//若不包含
                    jedis.sadd(userId,authId.toString());
                    jedis.expire(userId,300);
                    PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(authId);
                    UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
                    PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
                    saPicAnswer.setAnswerId(authId);
                    saPicAnswer.setAnswerUserId(picAnswer.getUserId());
                    saPicAnswer.setAwsome(picAnswer.getAwosome());
                    saPicAnswer.setIntroduce(picAppMes.getIntroduce());
                    saPicAnswer.setSubject(picAppMes.getAppSubject());
                    saPicAnswer.setNickName(userMes.getNickname());
                    ArrayList<String> imgPaths = new ArrayList<String>();
                    File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
                    File pathth = new File(path1.getAbsolutePath(), "static/images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/");
                    File[] files = pathth.listFiles();
                    for (File f : files
                    ) {
                        String fileName = f.getName();
                        String imgPath = "images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/" + fileName;
                        imgPaths.add(imgPath);
                    }
                    saPicAnswer.setImgs(imgPaths);
                    break;
                }
                count++;
            }
            if(count==allAuthId.size()-1){//若当前最佳都已经刷新完
                return null;
            }else {
                return saPicAnswer;
            }

        }

    }
    /**
     * 根据piAnswerId获取刷一刷的图片展示信息
     * */
    public SaPicAnswer getSaPicAnswerByAnswerId(int picAnswerId) throws FileNotFoundException {
        SaPicAnswer saPicAnswer = new SaPicAnswer();
        PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(picAnswerId);
        UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
        PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
        saPicAnswer.setAnswerId(picAnswerId);
        saPicAnswer.setAnswerUserId(picAnswer.getUserId());
        saPicAnswer.setAwsome(picAnswer.getAwosome());
        saPicAnswer.setIntroduce(picAppMes.getIntroduce());
        saPicAnswer.setSubject(picAppMes.getAppSubject());
        saPicAnswer.setNickName(userMes.getNickname());
        ArrayList<String> imgPaths = new ArrayList<String>();
        File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
        File pathth = new File(path1.getAbsolutePath(), "static/images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/");
        File[] files = pathth.listFiles();
        for (File f : files
        ) {
            String fileName = f.getName();
            String imgPath = "images/upload/pic/" + picAnswer.getPicappId() + "/" + picAnswer.getUserId() + "/" + fileName;
            imgPaths.add(imgPath);
        }
        saPicAnswer.setImgs(imgPaths);
        return saPicAnswer;
    }
}
