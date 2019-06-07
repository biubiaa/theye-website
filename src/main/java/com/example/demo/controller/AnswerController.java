package com.example.demo.controller;

import com.example.demo.config.BeanConfig;
import com.example.demo.dao.PicAnswer;
import com.example.demo.dao.VideoAnswer;
import com.example.demo.dto.*;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    @Autowired
    PicAppServiceImpl picAppService;
    @Autowired
    VideoAppServiceImpl videoAppService;
    @Autowired
    UserServiceimpl userServiceimpl;

    @RequestMapping(value = "addpicpic")
    public String addPicPic1(@RequestParam("appid") String  picAppId,HttpServletResponse response,@RequestParam("images")MultipartFile file, HttpServletRequest request,ModelMap modelMap) throws IOException {
//        System.out.println(file.getOriginalFilename());
        System.out.printf("jicila");
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        String pathname = "";
        String returnPath = "";
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            File path = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
//            System.out.println(path.getPath());//如果你的图片存储路径在static下，可以参考下面的写法
            File uploadFile = new File(path.getAbsolutePath(), "static/images/upload/pic/"+picAppId+"/"+userId+"/");//开发测试模式中 获取到的是/target/classes/static/images/upload/
            if (!uploadFile.exists()){
                uploadFile.mkdirs();
            }
            //获取文件后缀名
            String end = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            //图片名称 采取时间拼接随机数
            String name = df.format(new Date());
            Random r = new Random(new Date().getTime());
            name+=r.nextInt();
            String diskFileName = name + "." +end; //目标文件的文件名
            String savePath = uploadFile.getPath();
            pathname = savePath+ "/" + diskFileName;
            file.transferTo(new File(pathname));//文件转存
            PicAnswer picAnswer = new PicAnswer();
            picAnswer.setPicAdress("images/upload/pic/"+picAppId+"/"+userId+"/");
            picAnswer.setPicappId(Integer.parseInt(picAppId));
            picAnswer.setUserId((String) request.getSession().getAttribute("userId"));
            if(picAnswerimpl.insertNewAnswer(picAnswer)==1){
                //上传成功
                List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
                List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
                modelMap.addAttribute("picAnswer",pics);
                modelMap.addAttribute("videoAnswer",videos);
                return"myanswer";
            }else {
                //上传失败
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('您已经回答过该问题，请删除原答案在进行上传')</script>");
                List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
                List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
                modelMap.addAttribute("picAnswers",pics);
                modelMap.addAttribute("videoAnswers",videos);
                return "myanswer";

            }
        }else {
            //上传的文件为空
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您已经回答过该问题，请删除原答案在进行上传')</script>");
            String region = "北京";
            String type = "未解决";
            List<ZSPicAppMes> zsPicAppMes = picAppService.selectByCity(region, type);
            modelMap.addAttribute("picapps", zsPicAppMes);
            List<ZSVideoAppMes> zsVideoAppMes = videoAppService.selectByCity(region,type);
            modelMap.addAttribute("videoapps",zsVideoAppMes);
            return "questionhall";
        }
    }
    /**
     * 上传视频答案
     * */
    @RequestMapping(value = "addvideovideo")
    public String addVideoAnswer(@RequestParam("appid") int  picAppId, @RequestParam("video")MultipartFile file, HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        String pathname = "";
        String returnPath = "";
        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            File path = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
//            System.out.println(path.getPath());//如果你的图片存储路径在static下，可以参考下面的写法
            File uploadFile = new File(path.getAbsolutePath(), "static/images/upload/video/"+picAppId+"/"+userId+"/");//开发测试模式中 获取到的是/target/classes/static/images/upload/
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
            VideoAnswer videoAnswer = new VideoAnswer();
            videoAnswer.setVedioAdress("images/upload/video/"+picAppId+"/"+userId+"/"+name+"."+end);
            videoAnswer.setVedioappId(picAppId);
            videoAnswer.setUserId(userId);

            if(videoAnswerService.insertVideoAnswer(videoAnswer)==1){
                List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
                List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
                modelMap.addAttribute("picAnswer",pics);
                modelMap.addAttribute("videoAnswer",videos);
                return "myanswer";
            }else {
                //已经填写过该答案
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('您已经回答过该问题，请删除原答案在进行上传')</script>");
                List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
                List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
                modelMap.addAttribute("picAnswers",pics);
                modelMap.addAttribute("videoAnswers",videos);
                return "myanswer";
            }

        }else {
            //上传的文件为空
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您已经回答过该问题，请删除原答案在进行上传')</script>");
            String region = "北京";
            String type = "未解决";
            List<ZSPicAppMes> zsPicAppMes = picAppService.selectByCity(region, type);
            modelMap.addAttribute("picapps", zsPicAppMes);
            List<ZSVideoAppMes> zsVideoAppMes = videoAppService.selectByCity(region,type);
            modelMap.addAttribute("videoapps",zsVideoAppMes);
            return "questionhall";
        }

    }
    /**
     * 跳转到我的答案页面
     * */
    @RequestMapping(value = "myanswer")
    public String myAnswer(HttpServletRequest request, ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        List<ZSPicMyAnswer> pics = picAnswerimpl.getPicAnswerByuserId(userId);
        List<ZSVideoMyAnswer> videos = videoAnswerService.getVidelAnswerByUserId(userId);
        modelMap.addAttribute("picAnswers",pics);
        modelMap.addAttribute("videoAnswers",videos);
        return "myanswer";
    }
    /**
     * 查看我的图片答案
     * */
    @RequestMapping(value = "picanswer",method = RequestMethod.GET)
    public String sPicAnswer(@RequestParam(value = "picAnswerId")int picAnswerId, HttpServletRequest request, ModelMap modelMap) throws FileNotFoundException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
//        System.out.println(picAnswerId);
        //这里读取的answerId在之前写入的是appId
        SpecificPicAnswer specificPicAnswer = picAnswerimpl.getAnswersBy(picAnswerId,userId);

        modelMap.addAttribute("picAnswerInfo",specificPicAnswer);
        //查询该问题所有答案

        return "picanswer";
    }
    /**
     *查看我的视频答案
     **/
    @RequestMapping(value = "videoanswer",method = RequestMethod.GET)
    public String sVideoAnswer(@RequestParam(value = "videoAnswerId")int videoAnswerId,HttpServletRequest request,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        //这里读取的answerId在之前写入的是appId*****************************************8
        SpecificVideoAnswer specificVideoAnswer = videoAnswerService.getAnswerByAppId_UserId(videoAnswerId,userId);
        modelMap.addAttribute("videoAnswerInfo",specificVideoAnswer);
        return "videoanswer";
    }
    /**
     * 根据某请求的第几个回答显示图片答案
     * */
    @RequestMapping(value = "ppicanswer",method = RequestMethod.GET)
    public String ppicAnswer(@RequestParam("count") int count,@RequestParam("picId")int picId,HttpServletRequest request,ModelMap modelMap) throws FileNotFoundException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        SpecificPicAnswer specificPicAnswer = picAnswerimpl.selectByAppIdAndCount(picId,count);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        modelMap.addAttribute("picAnswerInfo",specificPicAnswer);
        return "picanswer";
    }
    /**
     * 根据某请求的第几个回答显示视频答案
     * */
    @RequestMapping(value = "vvideoanswer",method = RequestMethod.GET)
    public String vvideoAnswer(@RequestParam("count")int count,@RequestParam("videoId")int videoId,HttpServletRequest request,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        SpecificVideoAnswer specificVideoAnswer = videoAnswerService.selectByAppIdAndCount(videoId,count);
        modelMap.addAttribute("videoAnswerInfo",specificVideoAnswer);
        return "videoanswer";
    }
    /**
     * 查看一个请求的第一个图片回答
     * */
    @RequestMapping(value = "getPicFirstAnswer",method = RequestMethod.GET)
    public String getPicFirstAnswer(@RequestParam("picAppId")int picAppId,ModelMap modelMap,HttpServletResponse response,HttpServletRequest request) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        SpecificPicAnswer specificPicAnswer = picAnswerimpl.selectByAppIdAndCount(picAppId,1);
        if(specificPicAnswer==null){
            //没有人回答

            List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
            List<ZSVideoAppMes> videoAppMes = videoAppService.selectByUserId(userId);
            modelMap.addAttribute("picapps",zsAppMesss);
            modelMap.addAttribute("videoapps",videoAppMes);

            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您的请求目前没有人回答')</script>");
            return "myquestion";
        }else {
            modelMap.addAttribute("picAnswerInfo", specificPicAnswer);
            return "picanswer";
        }
    }
    /**
     * 查看一个请求的第一个视频答案
     * */
    @RequestMapping(value = "getVideoFirstAnswer",method = RequestMethod.GET)
    public String getVideoFirstAnswer(@RequestParam("videoAppId") int videoAppId,ModelMap modelMap,HttpServletRequest request,HttpServletResponse response) throws IOException {
        SpecificVideoAnswer specificVideoAnswer = videoAnswerService.selectByAppIdAndCount(videoAppId,1);
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);

        if(specificVideoAnswer==null){
            //没有人回答
            List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
            List<ZSVideoAppMes> videoAppMes = videoAppService.selectByUserId(userId);
            modelMap.addAttribute("picapps",zsAppMesss);
            modelMap.addAttribute("videoapps",videoAppMes);

            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您的请求目前没有人回答')</script>");
            return "myquestion";
        }else {
            modelMap.addAttribute("videoAnswerInfo", specificVideoAnswer);
            return "videoanswer";
        }
    }
    /**
     * 删除图片答案
     * */
    @RequestMapping(value = "deletepicanswer")
    public String deletePicAnswer(@RequestParam(value = "picAnswerId")int picAnswerId,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        List<ZSPicMyAnswer> pics = null;
        List<ZSVideoMyAnswer> videos = null;
        try {
            int flag = picAnswerimpl.deletePicAnswer(picAnswerId, userId);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('删除成功')</script>");
                //返回我的答案界面
                 pics = picAnswerimpl.getPicAnswerByuserId(userId);
                 videos = videoAnswerService.getVidelAnswerByUserId(userId);
        }catch (FileNotFoundException e){
        } catch (IOException e) {
        }
        modelMap.addAttribute("picAnswers",pics);
        modelMap.addAttribute("videoAnswers",videos);
        return "myanswer";
    }
    /**
     * 删除视频答案
     * */
    @RequestMapping(value = "deletevideoanswer")
    public String deleteVideoAnswer(@RequestParam(value = "videoAnswerId")int videoAnswerId,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        List<ZSPicMyAnswer> pics = null;
        List<ZSVideoMyAnswer> videos = null;
        try {
//            int flag = picAnswerimpl.deletePicAnswer(picAnswerId, userId);
            videoAnswerService.deleteVideoAnswer(videoAnswerId,userId);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('删除成功')</script>");
            //返回我的答案界面
            pics = picAnswerimpl.getPicAnswerByuserId(userId);
            videos = videoAnswerService.getVidelAnswerByUserId(userId);
        }catch (FileNotFoundException e){
        } catch (IOException e) {
        }
        modelMap.addAttribute("picAnswers",pics);
        modelMap.addAttribute("videoAnswers",videos);
        return "myanswer";
    }
    /**
     * 图片答案中标
     * */
//    @RequestMapping(value = "picAnswerOOk",method = RequestMethod.GET)
//    public String picAnswerOOK(@RequestParam(value = "picAppId")int picAppId,@RequestParam(value = "rightUserId")String rightUserId){
//
//    }

}
