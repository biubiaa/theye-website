package com.example.demo.controller;

import com.example.demo.dao.PicAppMes;
import com.example.demo.dao.VideoAppMes;
import com.example.demo.dto.ZSPicAppMes;
import com.example.demo.dto.ZSVideoAppMes;
import com.example.demo.service.impl.PicAppServiceImpl;
import com.example.demo.service.impl.UserServiceimpl;
import com.example.demo.service.impl.VideoAppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    UserServiceimpl userServiceimpl;
    @Autowired
    PicAppServiceImpl picAppService;
    @Autowired
    VideoAppServiceImpl videoAppService;

    /**
     * 添加图片请求
     * */
    @RequestMapping(value = "addpicappmes")
    public String addPicMes(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
        HttpSession session = request.getSession();
        String userId = (String )session.getAttribute("userId");
        String subject = request.getParameter("subject");
        String introduce = request.getParameter("introduce");
        String region = request.getParameter("city-picker3");
        Double money = Double.parseDouble(request.getParameter("money"));
        if(userServiceimpl.judgeMoney(userId,money)==1){
            PicAppMes picAppMes = new PicAppMes();
            picAppMes.setUserId(userId);

            picAppMes.setAppSubject(subject);
            picAppMes.setIntroduce(introduce);
            picAppMes.setMoney(money);
            picAppMes.setRightUserId("");
            picAppMes.setSolve(0);
            picAppMes.setAmazing(0);
            picAppMes.setRegion(region);
            if(picAppService.addPicApp(picAppMes)==1) {
//                model.addAttribute("subject",subject);
//                model.addAttribute("nickname",userServiceimpl.gerNickName(userId));
//                model.addAttribute("introduce",introduce);
//                Date date = picAppService.getfabuDate(userId,subject);
//                model.addAttribute("date",date);
//                model.addAttribute("money",money);
//                model.addAttribute("state","未解决");
//                String userId = (String) request.getSession().getAttribute("userId");
                List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
                List<ZSVideoAppMes> videoAppMes = videoAppService.selectByUserId(userId);
                model.addAttribute("picapps",zsAppMesss);
                model.addAttribute("videoapps",videoAppMes);
                return "myquestion";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('系统错误，请稍后再试‘)</script>");
                return "addpicapp";
            }
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您的账户余额不足，请充值或修改奖赏金额')</script>");
            return "addpicapp";
        }

    }
    /**
     * 添加视频请求
     * */
    @RequestMapping(value = "addvideoappmes")
    public String addVideoAppMes(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
        HttpSession session = request.getSession();
        String userId = (String )session.getAttribute("userId");
        String subject = request.getParameter("subject");
        String introduce = request.getParameter("introduce");
        String region = request.getParameter("city-picker3");
        System.out.println("money:"+request.getParameter("money"));
        System.out.println(userId+"   "+subject+"   "+introduce+"   "+region);
        Double money = Double.parseDouble(request.getParameter("money"));
        if(userServiceimpl.judgeMoney(userId,money)==1){
            VideoAppMes videoAppMes = new VideoAppMes();
            videoAppMes.setUserId(userId);
            videoAppMes.setAppSubject(subject);
            videoAppMes.setIntroduction(introduce);
            videoAppMes.setMoney(money);
            videoAppMes.setRightUserId("");
            videoAppMes.setSolve(0);
            videoAppMes.setAmazing(0);
            videoAppMes.setRegion(region);
            if(videoAppService.addApp(videoAppMes)==1) {
                List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
                List<ZSVideoAppMes> videoAppMes1 = videoAppService.selectByUserId(userId);
                model.addAttribute("picapps",zsAppMesss);
                model.addAttribute("videoapps",videoAppMes1);
                return "myquestion";
            }else {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('系统错误，请稍后再试‘)</script>");
                return "addpicmes";
            }
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('您的账户余额不足，请充值或修改奖赏金额')</script>");
            return "addpicmes";
        }

    }
    /**
     * 查看我的请求
     * */
    @RequestMapping(value = "myquestion")
    public String myQuestion(HttpServletRequest request,ModelMap model)
    {
        String userId = (String) request.getSession().getAttribute("userId");
        List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
        List<ZSVideoAppMes> videoAppMes = videoAppService.selectByUserId(userId);
        model.addAttribute("picapps",zsAppMesss);
        model.addAttribute("videoapps",videoAppMes);
        return "myquestion";
    }
    /**
     * 删除我的图片请求
     * */
    @RequestMapping(value = "deletepicapp",method = RequestMethod.GET)
    public String deletePicApp(@RequestParam Integer id,HttpServletRequest request,ModelMap model){
        picAppService.deleteApp(id);
        String userId = (String) request.getSession().getAttribute("userId");
        List<ZSPicAppMes> zsAppMesss = picAppService.getPicAppsByUserId(userId);
        model.addAttribute("picapps",zsAppMesss);
        return "myquestion";
    }
    /**
     *查询一个城市的申请
     * */
    @RequestMapping(value = "selectbycity" ,method = RequestMethod.POST)
    public String selectByCity(HttpServletRequest request,ModelMap modelMap,@RequestParam String region,@RequestParam String type){
        String userId = (String) request.getSession().getAttribute("userId");
        //未解决返回questionhall界面
        if (type.equals("未解决")) {
            List<ZSPicAppMes> zsPicAppMes = picAppService.selectByCity(region, type);
            modelMap.addAttribute("picapps", zsPicAppMes);
            List<ZSVideoAppMes> zsVideoAppMes = videoAppService.selectByCity(region,type);
            modelMap.addAttribute("videoapps",zsVideoAppMes);
            return "questionhall";
        }else {//已解决返回questionhallready界面
            List<ZSPicAppMes> zsPicAppMes = picAppService.selectByCity(region, type);
            modelMap.addAttribute("picapps", zsPicAppMes);
            List<ZSVideoAppMes> zsVideoAppMes = videoAppService.selectByCity(region,type);
            modelMap.addAttribute("videoapps",zsVideoAppMes);
            return "questionhallalready";
        }

    }
}
