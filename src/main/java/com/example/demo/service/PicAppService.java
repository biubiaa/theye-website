package com.example.demo.service;

import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.PicAppMes;

public interface PicAppService {
    //添加pic申请
    public int addPicApp(PicAppMes picAppMes);
    //撤销申请
    public int deleteApp(int appId);
    //提交回答
    public int addPicAppAnswer(PicAnswer picAnswer);
    //删除回答
    public int deletePicAppAnswer(int PicAnswerId);
}
