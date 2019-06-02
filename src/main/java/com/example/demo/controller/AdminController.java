package com.example.demo.controller;

import com.example.demo.dto.VerifyNum;
import com.example.demo.dto.VerifyPicAnswer;
import com.example.demo.dto.VerifyVideoAnswer;
import com.example.demo.mapper.VideoAnswerMapper;
import com.example.demo.service.impl.AdminServiceImpl;
import com.example.demo.service.impl.PicAnswerImpl;
import com.example.demo.service.impl.VideoAnswerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
@Controller
public class AdminController {
    @Autowired
    AdminServiceImpl adminService;
    @Autowired
    PicAnswerImpl picAnswer;
    @Autowired
    VideoAnswerServiceImpl videoAnswerService;
    //进入管理员登录界面
    @RequestMapping(value = "admin")
    public String adminLoginPage(){
        return "adminlogin";
    }
    //输入密码提交地址
    @RequestMapping(value = "adminloginin",method = RequestMethod.POST)
    public String log(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "adminId")String adminId,@RequestParam(value = "password")String pwd) throws IOException {
        boolean flag = adminService.login(adminId,pwd);
        if(flag==true){
            //给session打上管理员标记
            request.getSession().setAttribute("type","admin");
            VerifyPicAnswer verifyPicAnswer = picAnswer.verifyPicAnswer();
            VerifyNum sum = picAnswer.getNoVerifiedAnswerNum();
            if(verifyPicAnswer == null){
                modelMap.addAttribute("sum",sum);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
                return "adminpicnil";
            }else {
                modelMap.addAttribute("sum",sum);
                modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
                return "adminpic";
            }
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('帐号或密码输入错误，请重新尝试')</script>");
            return "adminlogin";
        }
    }
    /**
     * 展示未审核的图片信息
     * */
    @RequestMapping(value = "verifypic")
    public String verifyPic(ModelMap modelMap,HttpServletResponse response) throws IOException {
        VerifyPicAnswer verifyPicAnswer = picAnswer.verifyPicAnswer();
        VerifyNum sum = picAnswer.getNoVerifiedAnswerNum();
        if(verifyPicAnswer == null){
            modelMap.addAttribute("sum",sum);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
            return "adminpicnil";
        }else {
            modelMap.addAttribute("sum",sum);
            modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
            return "adminpic";
        }
    }
    /**
     * 展示未审核的视频信息
     * */
    @RequestMapping(value = "verifyvedio")
    public String verifyVideo(ModelMap modelMap,HttpServletResponse response) throws IOException {
        VerifyVideoAnswer verifyVideoAnswer = videoAnswerService.vv();
        VerifyNum sum = picAnswer.getNoVerifiedAnswerNum();
        if(verifyVideoAnswer==null){
            modelMap.addAttribute("sum",sum);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
            return "adminvideonil";
        }else {
            modelMap.addAttribute("sum",sum);
            modelMap.addAttribute("videoAnswerInfo",verifyVideoAnswer);
            return "adminvideo";
        }
    }
    /**
     * 图片通过审核
     * */
    @RequestMapping(value = "yespicanswer")
    public String yesPicAnswer(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response,@RequestParam(value = "picAnswerId")Integer picAnswerId) throws IOException {
        int flag = picAnswer.yesPicAnswer(picAnswerId,1);
        VerifyPicAnswer verifyPicAnswer = picAnswer.verifyPicAnswer();
        VerifyNum sum = picAnswer.getNoVerifiedAnswerNum();
        if(verifyPicAnswer == null){
            if (flag==1) {
                modelMap.addAttribute("sum", sum);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
                return "adminpicnil";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('操作失败请重新尝试')</script>");
                modelMap.addAttribute("sum",sum);
                modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
                return "adminpic";
            }
        }else {
            modelMap.addAttribute("sum",sum);
            modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
            return "adminpic";
        }

    }
    /**
     * 视频通过审核
     * */
    @RequestMapping(value = "yesvideoanswer")
    public String yesVideoAnswer(ModelMap modelMap,HttpServletResponse response,@RequestParam(value = "videoAnswerId")Integer videoAnswerId) throws IOException {
        int flag = videoAnswerService.changeState(videoAnswerId,1);
        VerifyVideoAnswer verifyVideoAnswer = videoAnswerService.vv();
        VerifyNum num = picAnswer.getNoVerifiedAnswerNum();
        if(verifyVideoAnswer == null){
            if (flag==1) {
                modelMap.addAttribute("sum", num);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
                return "adminpicnil";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('操作失败请重新尝试')</script>");
                modelMap.addAttribute("sum",num);
                modelMap.addAttribute("videoAnswerInfo",verifyVideoAnswer);
                return "adminvideo";
            }
        }else {
            modelMap.addAttribute("sum",num);
            modelMap.addAttribute("videoAnswerInfo",verifyVideoAnswer);
            return "adminpicvideo";
        }
    }

    /**
     * 图片答案违规
     * */
    @RequestMapping(value = "nopicanswer")
    public String noPicAnswer(ModelMap modelMap,HttpServletResponse response,@RequestParam(value = "picAnswerId")Integer picAnswerId) throws IOException {
        int flag = picAnswer.yesPicAnswer(picAnswerId,3);

        VerifyPicAnswer verifyPicAnswer = picAnswer.verifyPicAnswer();
        VerifyNum sum = picAnswer.getNoVerifiedAnswerNum();
        if(verifyPicAnswer == null){
            if (flag==1) {
                modelMap.addAttribute("sum", sum);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
                return "adminpicnil";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('操作失败请重新尝试')</script>");
                modelMap.addAttribute("sum",sum);
                modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
                return "adminpic";
            }
        }else {
            modelMap.addAttribute("sum",sum);
            modelMap.addAttribute("picAnswerInfo",verifyPicAnswer);
            return "adminpic";
        }
    }
    /**
     * 视频答案违规
     * */
    @RequestMapping(value = "novideoanswer")
    public String noVideoAnswer(ModelMap modelMap,HttpServletResponse response,@RequestParam(value = "videoAnswerId")Integer videoAnswerId) throws IOException {
        int flag = videoAnswerService.changeState(videoAnswerId,3);
        VerifyVideoAnswer verifyVideoAnswer = videoAnswerService.vv();
        VerifyNum num = picAnswer.getNoVerifiedAnswerNum();
        if(verifyVideoAnswer == null){
            if (flag==1) {
                modelMap.addAttribute("sum", num);
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('当前没有未通过审核的答案')</script>");
                return "adminpicnil";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('操作失败请重新尝试')</script>");
                modelMap.addAttribute("sum",num);
                modelMap.addAttribute("videoAnswerInfo",verifyVideoAnswer);
                return "adminvideo";
            }
        }else {
            modelMap.addAttribute("sum",num);
            modelMap.addAttribute("videoAnswerInfo",verifyVideoAnswer);
            return "adminpicvideo";
        }
    }

}
