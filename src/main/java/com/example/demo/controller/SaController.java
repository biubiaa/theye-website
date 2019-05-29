package com.example.demo.controller;

import com.example.demo.dto.SaPicAnswer;
import com.example.demo.service.impl.SaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class SaController {
    @Autowired
    SaServiceImpl saService;
    /**
     * 初次访问刷一刷
     * */
    @RequestMapping(value = "shuayishu")
    public String shuayishua(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        SaPicAnswer saPicAnswer = saService.getRandomPic(userId);
        if(saPicAnswer==null){//没有满足条件的图片回答
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('暂时没有满足条件的图片悬赏回答，请过段时间在来刷一刷')</script>");
        }
        else {
            modelMap.addAttribute("saPicAnswer",saPicAnswer);
        }
        return "picshua";
    }
    /**
     * 刷下一条
     * */
    @RequestMapping(value = "picshuanext")
    public String picShuaNext(@RequestParam(value = "picAnswerId")int picAnswerId, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
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

}
