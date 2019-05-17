package com.example.demo.controller;

import com.example.demo.dao.PicAnswer;
import com.example.demo.dto.ZSPicMyAnswer;
import com.example.demo.dto.ZSVideoMyAnswer;
import com.example.demo.service.impl.PicAnswerImpl;
import com.example.demo.service.impl.VideoAnswerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class AnswerController {
    @Autowired
    PicAnswerImpl picAnswerimpl;
    @Autowired
    VideoAnswerServiceImpl videoAnswerService;

    @RequestMapping(value = "addpicpic")
    public String addPicPic1(@RequestParam("appid") String  picAppId,@RequestParam("images")MultipartFile file, HttpServletRequest request,ModelMap modelMap) throws IOException {
        System.out.println(file.getOriginalFilename());

        String pathname = "";
        String returnPath = "";
        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            File path = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
//            System.out.println(path.getPath());//如果你的图片存储路径在static下，可以参考下面的写法
            File uploadFile = new File(path.getAbsolutePath(), "static/images/upload/pic/"+picAppId+"/");//开发测试模式中 获取到的是/target/classes/static/images/upload/
            if (!uploadFile.exists()){
                uploadFile.mkdirs();
            }
            //获取文件后缀名
            String end = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            //图片名称 采取时间拼接随机数
            String name = df.format(new Date());
            Random r = new Random();


            String diskFileName = name + "." +end; //目标文件的文件名
            String savePath = uploadFile.getPath();
            pathname = savePath+ "/" + diskFileName;
            file.transferTo(new File(pathname));//文件转存
            PicAnswer picAnswer = new PicAnswer();
            picAnswer.setPicAdress("images/upload/pic/"+picAppId+"/");
            picAnswer.setPicappId(Integer.parseInt(picAppId));
            picAnswer.setUserId((String) request.getSession().getAttribute("userId"));
            picAnswerimpl.insertNewAnswer(picAnswer);


//            System.out.println(pathname);



        }//UploadResponseData 自定义返回数据类型实体{int code, String msg, Object data}
        String userId = (String) request.getSession().getAttribute("userId");
        List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
        List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
        modelMap.addAttribute("picAnswer",pics);
        modelMap.addAttribute("videoAnswer",videos);
        return"myanswer";
    }
    /**
     * 跳转到我的答案页面
     * */
    @RequestMapping(value = "myanswer")
    public String myAnswer(HttpServletRequest request, ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
        List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
        modelMap.addAttribute("picAnswers",pics);
        modelMap.addAttribute("videoAnswers",videos);
        return "myanswer";

    }
    /**
     * 具体图片答案界面
     * */
    @RequestMapping(value = "ppp",method = RequestMethod.GET)
    public String sPicAnswer(@RequestParam(value = "picAnswerId")int picAnswerId, HttpServletRequest request, ModelMap modelMap) throws FileNotFoundException {
        String userId = (String) request.getSession().getAttribute("userId");
        picAnswerimpl.getAnswersBy(picAnswerId);
        //查询该问题所有答案

        return "pecificAnswer/picAnswer";
    }
    /**
     * 具体图片答案界面
     * */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String sVideoAnswer(HttpServletRequest request, ModelMap modelMap){

        return "pecificAnswer/videoiAnswer";
    }
}
