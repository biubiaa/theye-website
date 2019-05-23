package com.example.demo.service.impl;

import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.PicAppMes;
import com.example.demo.dao.UserMes;
import com.example.demo.dto.SpecificPicAnswer;
import com.example.demo.dto.ZSPicMyAnswer;
import com.example.demo.mapper.PicAnswerMapper;
import com.example.demo.mapper.PicAppMesMapper;
import com.example.demo.mapper.UserMesMapper;
import com.example.demo.util.DeleteFileFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PicAnswerImpl {
    @Autowired
    PicAnswerMapper picAnswerMapper;
    @Autowired
    PicAppMesMapper picAppMesMapper;
    @Autowired
    UserMesMapper userMesMapper;
    public int insertNewAnswer(PicAnswer picAnswer){

        //先查询是否有该条记录，若没有则添加，有则不进行操作
        List<PicAnswer>  flag= picAnswerMapper.chachong(picAnswer.getUserId(),picAnswer.getPicappId());

        System.out.println(flag.isEmpty());
        if(flag.isEmpty()){
            //没有记录，则插入
            picAnswerMapper.insert(picAnswer);
        }

        return 1;
    }
    public List<ZSPicMyAnswer> getPicAnswerByuserId(String userId){
        List<PicAnswer> pics = picAnswerMapper.selectAnswerByUserId(userId);
        List<ZSPicMyAnswer> picAnswers = new ArrayList<ZSPicMyAnswer>();
        for (PicAnswer p: pics
             ) {

            ZSPicMyAnswer z1 = new ZSPicMyAnswer();
            z1.setAnswerTime(p.getAppTime());
            z1.setPicAnswerId(p.getPicappId());
            int appId = p.getPicappId();
            PicAppMes picAppMes =  picAppMesMapper.selectByPrimaryKey(appId);
            z1.setIntroduce(picAppMes.getIntroduce());
            if(picAppMes.getSolve()==1){
                z1.setOk(picAppMes.getRightUserId()==userId?"已采纳":"未采纳");
            }else {
                z1.setOk("审核中");
            }
            z1.setSubject(picAppMes.getAppSubject());
            String originUserId = picAppMes.getUserId();
            UserMes userMes = userMesMapper.selectByPrimaryKey(originUserId);
            z1.setAskUserName(userMes.getNickname());
            picAnswers.add(z1);
        }
        return  picAnswers;
    }

    public int deletePicAnswer(int picAnswerId,String path){
        picAnswerMapper.deleteByPrimaryKey(picAnswerId);
        //删除文件夹
        DeleteFileFolder.traverseFolder(path);
        return 1;
    }
    /**
     * 根据请求id和用户id返回用户的答案信息
     * */
    public SpecificPicAnswer getAnswersBy(int picAppId,String userId) throws FileNotFoundException {
        SpecificPicAnswer specificPicAnswer = new SpecificPicAnswer();
        //获取该请求的回答总数量
        int sum = picAnswerMapper.selectSumByAppId(picAppId);

        //查看我的答案
        PicAnswer picAnswer = picAnswerMapper.selectByAnswerUserId(picAppId,userId);

        int count = 0;
        List<PicAnswer> picAnswers = picAnswerMapper.selectByPicAppId(picAnswer.getPicappId());
        System.out.println("answers:"+picAnswers.size());

        System.out.println(picAnswers.get(0).getPicappId());
        for (PicAnswer p: picAnswers
             ) {
            count++;
            if(p.getUserId().equals(userId)){
                break;
            }
        }
        PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(picAnswer.getPicappId());
        UserMes appUserMes = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
        UserMes answerUserMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
        specificPicAnswer.setSum(sum);
        specificPicAnswer.setAnswerTIme(picAnswer.getAppTime());
        specificPicAnswer.setAnswerUserId(picAnswer.getUserId());
        //是第几个
        specificPicAnswer.setCount(count);
        specificPicAnswer.setAskTime(picAppMes.getAppTime());
        specificPicAnswer.setAnswerUserName(answerUserMes.getNickname());
        specificPicAnswer.setAnswerUserId(answerUserMes.getUserId());
        specificPicAnswer.setOriginUserId(appUserMes.getUserId());
        specificPicAnswer.setOriginUserName(appUserMes.getNickname());
        specificPicAnswer.setSubject(picAppMes.getAppSubject());
        specificPicAnswer.setPicAnswerId(picAnswer.getPicId());
        specificPicAnswer.setPicAppId(picAppId);
        //查看路径下有多少文件
        String path = picAnswer.getPicAdress();//url路径
        List<String> imgPaths = new ArrayList<String>();
        File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
        File pathth = new File(path1.getAbsolutePath(),"static/images/upload/pic/"+picAppId+"/"+userId+"/");
        File[] files = pathth.listFiles();
        for (File f: files
             ) {
            String fileName = f.getName();
            String imgPath = "http://127.0.0.1:8080/images/upload/pic/"+picAppId+"/"+picAnswer.getUserId()+"/"+fileName;
            imgPaths.add(imgPath);
        }
        specificPicAnswer.setImgs(imgPaths);
        return specificPicAnswer;
    }
    /**
     * 根据请求id和第几个来返回答案信息
     * */
    public SpecificPicAnswer selectByAppIdAndCount(int picAppId,int count) throws FileNotFoundException {
        SpecificPicAnswer specificPicAnswer = new SpecificPicAnswer();
        //获取该请求的回答总数量
        int sum = picAnswerMapper.selectSumByAppId(picAppId);

        //查看我的答案
//        PicAnswer picAnswer = picAnswerMapper.selectByAnswerUserId(picAppId,userId);

//        int count = 0;
        List<PicAnswer> picAnswers = picAnswerMapper.selectByPicAppId(picAppId);
//        System.out.println("answers:"+picAnswers.size());

        PicAnswer picAnswer = picAnswers.get(count-1);
        PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(picAppId);
        UserMes appUserMes = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
        UserMes answerUserMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
        specificPicAnswer.setSum(sum);
        specificPicAnswer.setAnswerTIme(picAnswer.getAppTime());
        specificPicAnswer.setAnswerUserId(picAnswer.getUserId());
        //是第几个
        specificPicAnswer.setCount(count);
        specificPicAnswer.setAskTime(picAppMes.getAppTime());
        specificPicAnswer.setAnswerUserName(answerUserMes.getNickname());
        specificPicAnswer.setAnswerUserId(answerUserMes.getUserId());
        specificPicAnswer.setOriginUserId(appUserMes.getUserId());
        specificPicAnswer.setOriginUserName(appUserMes.getNickname());
        specificPicAnswer.setSubject(picAppMes.getAppSubject());
        specificPicAnswer.setPicAnswerId(picAnswer.getPicId());
        specificPicAnswer.setPicAppId(picAppId);

        //查看路径下有多少文件
        String path = picAnswer.getPicAdress();//url路径

        List<String> imgPaths = new ArrayList<String>();
        File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
        File pathth = new File(path1.getAbsolutePath(),"static/images/upload/pic/"+picAppId+"/"+picAnswer.getUserId()+"/");
        File[] files = pathth.listFiles();
        for (File f: files
        ) {
            String fileName = f.getName();
            String imgPath = "images/upload/pic/"+picAppId+"/"+picAnswer.getUserId()+"/"+fileName;
            imgPaths.add(imgPath);
        }
        specificPicAnswer.setImgs(imgPaths);
        return specificPicAnswer;

    }
}
