package com.example.demo.service.impl;

import com.example.demo.dao.LevelScore;
import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.PicAppMes;
import com.example.demo.dao.UserMes;
import com.example.demo.dto.*;
import com.example.demo.mapper.*;
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
    @Autowired
    VideoAnswerMapper videoAnswerMapper;
    @Autowired
    LevelScoreMapper levelScoreMapper;
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
            if(p.getState()==0)
                z1.setOk("审核中");
            if(p.getState()==1)
                z1.setOk("已审核");
            if(p.getState()==2)
                z1.setOk("中榜");
            if(p.getState()==3)
                z1.setOk("违规");

            z1.setSubject(picAppMes.getAppSubject());
            String originUserId = picAppMes.getUserId();
            UserMes userMes = userMesMapper.selectByPrimaryKey(originUserId);
            z1.setAskUserName(userMes.getNickname());
            picAnswers.add(z1);
        }
        return  picAnswers;
    }

    public int deletePicAnswer(int picAnswerId,String userId) throws FileNotFoundException {
        PicAnswer p = picAnswerMapper.selectByPrimaryKey(picAnswerId);
        if (p.getUserId().equals(userId)) {
            String path = p.getPicAdress();
            picAnswerMapper.deleteByPrimaryKey(picAnswerId);
            //删除文件夹
            File savePath = new File(ResourceUtils.getURL("classpath:").getPath());
            DeleteFileFolder.traverseFolder(savePath.getAbsolutePath() + path);
            return 1;
        }else
            return 0;
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
        specificPicAnswer.setIntroction(picAppMes.getIntroduce());
        specificPicAnswer.setSum(sum);
        specificPicAnswer.setAnswerTIme(picAnswer.getAppTime());
        specificPicAnswer.setAnswerUserId(picAnswer.getUserId());
        //是第几个
        specificPicAnswer.setCount(count);
        specificPicAnswer.setAskTime(picAppMes.getAppTime());
        specificPicAnswer.setAnswerUserName(answerUserMes.getNickname());
        specificPicAnswer.setAnswerUserId(answerUserMes.getUserId());
        specificPicAnswer.setOriginUserId(appUserMes.getUserId());
        specificPicAnswer.setRightUserId(picAppMes.getRightUserId());
        specificPicAnswer.setOriginUserName(appUserMes.getNickname());
        specificPicAnswer.setSubject(picAppMes.getAppSubject());
        specificPicAnswer.setPicAnswerId(picAnswer.getPicId());
        specificPicAnswer.setState(picAnswer.getState());
        specificPicAnswer.setAwsome(picAnswer.getAwsome().toString());
        specificPicAnswer.setPicAppId(picAppId);
        //查看路径下有多少文件


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
        if(picAnswers.isEmpty()){
            return null;
        }else {
            PicAnswer picAnswer = picAnswers.get(count - 1);
            PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(picAppId);
            UserMes appUserMes = userMesMapper.selectByPrimaryKey(picAppMes.getUserId());
            UserMes answerUserMes = userMesMapper.selectByPrimaryKey(picAnswer.getUserId());
            specificPicAnswer.setSum(sum);
            specificPicAnswer.setIntroction(picAppMes.getIntroduce());
            specificPicAnswer.setAnswerTIme(picAnswer.getAppTime());
            specificPicAnswer.setAnswerUserId(picAnswer.getUserId());
            specificPicAnswer.setAwsome(picAnswer.getAwsome().toString());
            specificPicAnswer.setRightUserId(picAppMes.getRightUserId());
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
            File pathth = new File(path1.getAbsolutePath(), "static/images/upload/pic/" + picAppId + "/" + picAnswer.getUserId() + "/");
            File[] files = pathth.listFiles();
            for (File f : files
            ) {
                String fileName = f.getName();
                String imgPath = "images/upload/pic/" + picAppId + "/" + picAnswer.getUserId() + "/" + fileName;
                imgPaths.add(imgPath);
            }
            specificPicAnswer.setImgs(imgPaths);
            return specificPicAnswer;
        }
    }

    /**
     * 返回未经审核的答案信息
     * */
    public VerifyPicAnswer verifyPicAnswer() throws FileNotFoundException {
        ArrayList<PicAnswer> picAnswers = picAnswerMapper.selectNoVerifyAnswer();
        int sum = picAnswers.size();
        VerifyPicAnswer v = new VerifyPicAnswer();
        if(sum ==0){
            return null;
        }else {
            PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(picAnswers.get(0).getPicappId());
            v.setSubject(picAppMes.getAppSubject());
            v.setIntroduce(picAppMes.getIntroduce());
            v.setPicAnswerId(picAnswers.get(0).getPicId());
            ArrayList<String> imgPaths = new ArrayList<String>();
            File path1 = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
            File pathth = new File(path1.getAbsolutePath(), "static/images/upload/pic/" + picAppMes.getAppId() + "/" + picAnswers.get(0).getUserId() + "/");
            File[] files = pathth.listFiles();
            for (File f : files
            ) {
                String fileName = f.getName();
                String imgPath = "images/upload/pic/" + picAppMes.getAppId() + "/" + picAnswers.get(0).getUserId() + "/" + fileName;
                imgPaths.add(imgPath);
            }
            v.setImgs(imgPaths);
        }
        return v;
    }
    /**
     * 获取未经过审核的答案总数
     * */
    public VerifyNum getNoVerifiedAnswerNum(){
        VerifyNum v = new VerifyNum();
        v.setPicAnswerNum(picAnswerMapper.selectCountOfNoVerified());
        v.setVideoAnswerNum(videoAnswerMapper.selectCountOfNoVerified());
        return v;
    }
    /**
     * 答案审核通过
     * */
    public int yesPicAnswer(int picAnswerId,int state){
        //给回答的人加经验
        PicAnswer picAnswer = picAnswerMapper.selectByPrimaryKey(picAnswerId);
        LevelScore levelScore = levelScoreMapper.selectSumScoreByUserId(picAnswer.getUserId());
        levelScore.setAnswerScore(levelScore.getAnswerScore()+30);
        levelScoreMapper.updateByPrimaryKey(levelScore);
       return picAnswerMapper.changeState(picAnswerId,state);
    }
    /**
     * 获取本周最佳五个
     * */
    public ArrayList<PicMasterAnswer> getWeekAwsomeMaswerAnswer(){
        List<PicAnswer> answers = picAnswerMapper.selectWeekBestPicAnswer();
        ArrayList<PicMasterAnswer> picMasterAnswers = new ArrayList<PicMasterAnswer>();
        //显示地点、用户、时间
        for (PicAnswer p : answers
             ) {
            PicMasterAnswer picMasterAnswer = new PicMasterAnswer();
            picMasterAnswer.setAnswerTime(p.getAppTime());
            //申请访问该图片答案的链接
            picMasterAnswer.setLink("http://127.0.0.1:8080/picMaster?picId="+p.getPicId());
            PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(p.getPicappId());
            picMasterAnswer.setSubject(picAppMes.getAppSubject());
            UserMes userMes = userMesMapper.selectByPrimaryKey(p.getUserId());
            picMasterAnswer.setNickName(userMes.getNickname());
            picMasterAnswer.setAwsome(p.getAwsome());
            picMasterAnswers.add(picMasterAnswer);

        }
        return picMasterAnswers;
    }
    /**
     * 获取本月最佳五个
     * */
    public ArrayList<PicMasterAnswer> getMonthAwsomeMaswerAnswer(){
        List<PicAnswer> answers = picAnswerMapper.selectMonthBestPicAnswer();
        ArrayList<PicMasterAnswer> picMasterAnswers = new ArrayList<PicMasterAnswer>();
        //显示地点、用户、时间
        for (PicAnswer p : answers
        ) {
            PicMasterAnswer picMasterAnswer = new PicMasterAnswer();
            picMasterAnswer.setAnswerTime(p.getAppTime());
            //申请访问该图片答案的链接
            picMasterAnswer.setLink("http://127.0.0.1:8080/picMaster?picId="+p.getPicId());
            PicAppMes picAppMes = picAppMesMapper.selectByPrimaryKey(p.getPicappId());
            picMasterAnswer.setSubject(picAppMes.getAppSubject());
            UserMes userMes = userMesMapper.selectByPrimaryKey(p.getUserId());
            picMasterAnswer.setNickName(userMes.getNickname());
            picMasterAnswer.setAwsome(p.getAwsome());
            picMasterAnswers.add(picMasterAnswer);
        }
        return picMasterAnswers;
    }
    /**
     * 获取一个答案的悬赏id
     * */
    public int getAppid(int answerId){
        return picAnswerMapper.selectByPrimaryKey(answerId).getPicappId();
    }
    /**
     * 获取一个答案是第几个
     * */
    public int getCountByAnswerId(int answerId){
        ArrayList<PicAnswer> picAnswers = (ArrayList<PicAnswer>) picAnswerMapper.selectByPicAppId(picAnswerMapper.selectByPrimaryKey(answerId).getPicappId());
        int count=0;
        for (int i = 0; i < picAnswers.size() ; i++) {
            if (picAnswers.get(i).getPicId()==answerId){
                count  = i;
                break;
            }
        }
        return count+1;
    }
}
