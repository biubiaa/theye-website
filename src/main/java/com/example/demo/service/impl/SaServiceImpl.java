package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.dto.SaPicAnswer;
import com.example.demo.dto.SaVideoAnswer;
import com.example.demo.mapper.*;
import com.example.demo.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SaServiceImpl {
    @Autowired
    PicAnswerMapper picAnswerMapper;
    @Autowired
    UserMesMapper userMesMapper;
    @Autowired
    PicAppMesMapper picAppMesMapper;
    @Autowired
    VideoAnswerMapper videoAnswerMapper;
    @Autowired
    VideoAppMesMapper videoAppMesMapper;
    @Autowired
    PicAwsomeMapper picAwsomeMapper;
    @Autowired
    VideoAwsomeMapper videoAwsomeMapper;
    /**
     * 根据用户id返回随机的图片答案
     * */
    public SaPicAnswer getRandomPic(String userId) throws FileNotFoundException {
        String originUserId = userId;
        userId ="pic" +userId;
        Jedis jedis = (Jedis) SpringUtil.getBean("jedis");
        Set<String > picAnswerList = jedis.smembers(userId);
        ArrayList<Integer> allAuthId = null ;
        try {
            allAuthId = picAnswerMapper.getAllAuthId();
        }catch (IllegalArgumentException e){

        }

        SaPicAnswer saPicAnswer = new SaPicAnswer();
        if(picAnswerList.size()==0){//若是第一次访问
            if (allAuthId.size()==0){
                return null;
            }
            Integer authId = allAuthId.get(0);
            jedis.sadd(userId,authId.toString());
            jedis.expire(userId,300);
            PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(authId);
            UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
            PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
            UserMes askUserMes = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
            saPicAnswer.setAnswerId(authId);
            saPicAnswer.setAnswerUserId(picAnswer.getUserId());
            saPicAnswer.setAwsome(picAnswer.getAwsome());
            saPicAnswer.setIntroduce(picAppMes.getIntroduce());
            saPicAnswer.setSubject(picAppMes.getAppSubject());
            saPicAnswer.setNickName(userMes.getNickname());
            saPicAnswer.setAskUserId(askUserMes.getUserId());
            ArrayList<String> imgPaths = new ArrayList<String>();

//            List<String> imgPaths = new ArrayList<String>();
            File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
            File pathth = new File(path1.getAbsolutePath(),"static/images/upload/pic/"+picAnswer.getPicappId()+"/"+originUserId+"/");
            File[] files = pathth.listFiles();
            for (File f: files
            ) {
                String fileName = f.getName();
                String imgPath = "http://127.0.0.1:8080/images/upload/pic/"+picAnswer.getPicappId()+"/"+picAnswer.getUserId()+"/"+fileName;
                imgPaths.add(imgPath);
            }
            saPicAnswer.setImgs(imgPaths);
            return saPicAnswer;

        }else {//不是第一次访问
            int count = 0;
            for (Integer authId : allAuthId
            ) {
                if (!picAnswerList.contains(authId.toString())) {//若不包含
                    jedis.sadd(userId,authId.toString());
                    jedis.expire(userId,300);

                    PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(authId);
                    UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
                    PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
                    UserMes um = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
                    saPicAnswer.setAskUserId(um.getUserId());
                    saPicAnswer.setAnswerId(authId);
                    saPicAnswer.setAnswerUserId(picAnswer.getUserId());
                    saPicAnswer.setAwsome(picAnswer.getAwsome());
                    saPicAnswer.setIntroduce(picAppMes.getIntroduce());
                    saPicAnswer.setSubject(picAppMes.getAppSubject());
                    saPicAnswer.setNickName(userMes.getNickname());

                    ArrayList<String> imgPaths = new ArrayList<String>();
                    String path = picAnswer.getPicAdress();//url路径
//                    List<String> imgPaths = new ArrayList<String>();
                    File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
                    File pathth = new File(path1.getAbsolutePath(),"static/images/upload/pic/"+picAnswer.getPicappId()+"/"+originUserId+"/");
                    File[] files = pathth.listFiles();
                    for (File f: files
                    ) {
                        String fileName = f.getName();
                        String imgPath = "http://127.0.0.1:8080/images/upload/pic/"+picAnswer.getPicappId()+"/"+picAnswer.getUserId()+"/"+fileName;
                        imgPaths.add(imgPath);
                    }
                    saPicAnswer.setImgs(imgPaths);
                    break;
                }
                count++;
            }
            if(count==allAuthId.size()){//若当前最佳都已经刷新完
                return null;
            }else {
                return saPicAnswer;
            }

        }
    }
    /**
     * 根据用户id返回随机的视频答案
     * */
    public SaVideoAnswer getRandomVideo(String userId) throws FileNotFoundException {
        userId ="video" +userId;
        Jedis jedis = (Jedis) SpringUtil.getBean("jedis");
        Set<String > videoAnswerList = jedis.smembers(userId);
        ArrayList<Integer> allAuthId = videoAnswerMapper.getAllAuthId();
        if (allAuthId.size()==0){
            return null;
        }
        SaVideoAnswer saVideoAnswer = new SaVideoAnswer();
        if(videoAnswerList.size()==0){//若是第一次访问
            Integer authId = allAuthId.get(0);
            jedis.sadd(userId,authId.toString());
            jedis.expire(userId,300);
            VideoAnswer videoAnswer = videoAnswerMapper.selectByPrimaryKey(authId);
            UserMes userMes = userMesMapper.selectByPrimaryKey(videoAnswer.getUserId());
            VideoAppMes videoAppMes =  videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
            UserMes askUserMes = userMesMapper.selectByPrimaryKey(videoAppMes.getUserId());
            saVideoAnswer.setAskUserId(askUserMes.getUserId());
            saVideoAnswer.setAnswerId(authId);
            saVideoAnswer.setAnswerUserId(videoAnswer.getUserId());
            saVideoAnswer.setAwsome(videoAnswer.getAwsome());
            saVideoAnswer.setIntroduce(videoAppMes.getIntroduction());
            saVideoAnswer.setSubject(videoAppMes.getAppSubject());
            saVideoAnswer.setNickName(userMes.getNickname());

            File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
            File pathth = new File(path1.getAbsolutePath(), "static/images/upload/video/" + videoAnswer.getVedioappId() + "/" + videoAnswer.getUserId() + "/");
            String path="";

            File[] files = pathth.listFiles();
            for (File f : files
            ) {
                String fileName = f.getName();
                String videoPath = "images/upload/video/" +videoAnswer.getVedioappId()+ "/" + videoAnswer.getUserId()+ "/" + fileName;
                path = videoPath;
            }
            saVideoAnswer.setVideo(path);
            return saVideoAnswer;

        }else {//不是第一次访问
            int count = 0;
            for (Integer authId : allAuthId
            ) {
                if (!videoAnswerList.contains(authId)) {//若不包含
                    jedis.sadd(userId,authId.toString());
                    jedis.expire(userId,300);
                    VideoAnswer videoAnswer = videoAnswerMapper.selectByPrimaryKey(authId);
                    UserMes userMes = userMesMapper.selectByPrimaryKey(videoAnswer.getUserId());
                    VideoAppMes videoAppMes =  videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
                    UserMes askUserMes = userMesMapper.selectByPrimaryKey(videoAppMes.getUserId());
                    saVideoAnswer.setAskUserId(askUserMes.getUserId());
                    saVideoAnswer.setAnswerId(authId);
                    saVideoAnswer.setAnswerUserId(videoAnswer.getUserId());
                    saVideoAnswer.setAwsome(videoAnswer.getAwsome());
                    saVideoAnswer.setIntroduce(videoAppMes.getIntroduction());
                    saVideoAnswer.setSubject(videoAppMes.getAppSubject());
                    saVideoAnswer.setNickName(userMes.getNickname());
                    ArrayList<String> imgPaths = new ArrayList<String>();
                    File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
                    File pathth = new File(path1.getAbsolutePath(), "static/images/upload/video/" + videoAnswer.getVedioappId() + "/" + videoAnswer.getUserId() + "/");
                    String path="";

                    File[] files = pathth.listFiles();
                    for (File f : files
                    ) {
                        String fileName = f.getName();
                        String videoPath = "images/upload/pic/" +videoAnswer.getVedioappId()+ "/" + videoAnswer.getUserId()+ "/" + fileName;
                        path = videoPath;
                    }
                    saVideoAnswer.setVideo(path);
                    break;
                }
                count++;
            }
            if(count==allAuthId.size()-1){//若当前最佳都已经刷新完
                return null;
            }else {
                return saVideoAnswer;
            }

        }
    }
    /**
     * 根据picAnswerId获取刷一刷的图片展示信息
     * */
    public SaPicAnswer getSaPicAnswerByAnswerId(int picAnswerId) throws FileNotFoundException {

        SaPicAnswer saPicAnswer = new SaPicAnswer();
        PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(picAnswerId);
        UserMes userMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
        PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
        UserMes askUserMes = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
        saPicAnswer.setAskUserId(askUserMes.getUserId());
        saPicAnswer.setAnswerId(picAnswerId);
        saPicAnswer.setAnswerUserId(picAnswer.getUserId());
        saPicAnswer.setAwsome(picAnswer.getAwsome());
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
    /**
     * 根据videoAnswerId获取刷一刷的视频展示信息
     * */
    public SaVideoAnswer getSaVideoAnswerByAnswerId(int videoAnswerId)throws  FileNotFoundException{
        SaVideoAnswer saVideoAnswer = new SaVideoAnswer();
        VideoAnswer videoAnswer = videoAnswerMapper.selectByPrimaryKey(videoAnswerId);
        UserMes userMes = userMesMapper.selectByPrimaryKey(videoAnswer.getUserId());
        VideoAppMes videoAppMes =  videoAppMesMapper.selectByPrimaryKey(videoAnswer.getVedioappId());
        UserMes askUserMes = userMesMapper.selectByPrimaryKey(videoAppMes.getUserId());
        saVideoAnswer.setAskUserId(askUserMes.getUserId());
        saVideoAnswer.setAnswerId(videoAnswerId);
        saVideoAnswer.setAnswerUserId(videoAnswer.getUserId());
        saVideoAnswer.setAwsome(videoAnswer.getAwsome());
        saVideoAnswer.setIntroduce(videoAppMes.getIntroduction());
        saVideoAnswer.setSubject(videoAppMes.getAppSubject());
        saVideoAnswer.setNickName(userMes.getNickname());
        File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
        File pathth = new File(path1.getAbsolutePath(), "static/images/upload/video/" + videoAnswer.getVedioappId() + "/" + videoAnswer.getUserId() + "/");
        String path="";

        File[] files = pathth.listFiles();
        for (File f : files
        ) {
            String fileName = f.getName();
            String videoPath = "images/upload/pic/" +videoAnswer.getVedioappId()+ "/" + videoAnswer.getUserId()+ "/" + fileName;
            path = videoPath;
        }
        saVideoAnswer.setVideo(path);
        return saVideoAnswer;
    }
    /**
     * 给图片答案点赞
     * */
    public int picAwsome(String userId,int picAnswerId){
//        查询点没点过赞
        PicAwsome picAwsome = picAwsomeMapper.check(userId,picAnswerId);

        if(picAwsome ==null){//没点过赞
            //插入点赞记录
            picAwsome = new PicAwsome();
            picAwsome.setAnswerId(picAnswerId);
            picAwsome.setUserId(userId);
            picAwsomeMapper.insert(picAwsome);
            //增加答案的点赞量
            PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(picAnswerId);
            picAnswer.setAwsome(picAnswer.getAwsome()+1);
            picAnswerMapper.picAwsome(picAnswer);
            return 1;

        }else {//点过赞，不能在点了
            return 0;
        }
    }
}
