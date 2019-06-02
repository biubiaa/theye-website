package com.example.demo.controller;

import com.example.demo.dto.SaPicAnswer;
import com.example.demo.dto.SaVideoAnswer;
import com.example.demo.service.impl.SaServiceImpl;
import com.example.demo.service.impl.UserServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class SaController {
    @Autowired
    SaServiceImpl saService;
    @Autowired
    UserServiceimpl userServiceimpl;

    /**
     * 初次访问刷一刷
     * */
    @RequestMapping(value = "shuayishu")
    public String shuayishua(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        SaPicAnswer saPicAnswer = saService.getRandomPic(userId);
        System.out.println(saPicAnswer);
        if(saPicAnswer==null){//没有满足条件的图片回答
            return "nilshua";
        }
        else {
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
        }
        return "picshua";
    }
    /**
     * 点击选择图片还是视频shua
     * */
    @RequestMapping(value = "shuatype")
    public String choosePicShu(@RequestParam(value = "type")String type, HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws IOException {

        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        if(type.equals("pic")) {//选择的是图片
            SaPicAnswer saPicAnswer = saService.getRandomPic(userId);
            if (saPicAnswer == null) {//没有满足条件的图片回答
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('暂时没有满足条件的图片悬赏回答，请过段时间在来刷一刷')</script>");
                return "nilshua";
            } else {
                modelMap.addAttribute("saVideoAnswer", saPicAnswer);
                return "picshua";
            }

        }else {//选择的是视频
            SaVideoAnswer saVideoAnswer = saService.getRandomVideo(userId);
            if (saVideoAnswer == null) {//没有满足条件的图片回答
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('暂时没有满足条件的视频悬赏回答，请过段时间在来刷一刷')</script>");
                return "nilshua";
            } else {
                modelMap.addAttribute("saVideoAnswer", saVideoAnswer);
                return "videoshua";
            }

        }
    }
    /**
     * 图片刷下一条
     * */
    @RequestMapping(value = "picshuanext")
    public String picShuaNext(@RequestParam(value = "picAnswerId")int picAnswerId, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        SaPicAnswer saPicAnswer = saService.getRandomPic(userId);
        if(saPicAnswer==null){//没有满足条件的图片回答,返回当前的页面，并提示
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您已经刷完了全部的图片悬赏回答，请过段时间在来刷一刷')</script>");
            saPicAnswer = saService.getSaPicAnswerByAnswerId(picAnswerId);
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
        }
        else {
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
        }
        return "picshua";

    }
    /**
     * 视频刷下一条
     * */
    @RequestMapping(value = "videoshuanext")
    public String videoShuaNext(@RequestParam(value = "videoAnswerId")int videoAnswerId, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        SaVideoAnswer saVideoAnswer = saService.getRandomVideo(userId);
        if(saVideoAnswer==null){//没有满足条件的图片回答,返回当前的页面，并提示
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您已经刷完了全部的图片悬赏回答，请过段时间在来刷一刷')</script>");
            return "nilshua";
        }
        else {
            modelMap.addAttribute("saVideoAnswer",saVideoAnswer);
        }
        return "picshua";
    }
    /**
     * 给图片答案点赞
     * */
    @RequestMapping(value = "picawsome",method = RequestMethod.GET)
    public String picAwsome(@RequestParam(value = "picAnswerId")Integer picAnswerId,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        int flag = saService.picAwsome(userId,picAnswerId);
        System.out.println("flag:"+flag);
        if(flag==1){//点赞成功
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('点赞成功')</script>");
            SaPicAnswer saPicAnswer = saService.getSaPicAnswerByAnswerId(picAnswerId);
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
            return "picshua";
        }else {//点赞失败
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您已经点赞过该答案')</script>");
            SaPicAnswer saPicAnswer = saService.getSaPicAnswerByAnswerId(picAnswerId);
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
            return "picshua";
        }

    }


}
