package com.example.demo.controller;

import com.example.demo.dao.Message;
import com.example.demo.dto.PicMasterAnswer;
import com.example.demo.dto.VIdeoMasterMaster;
import com.example.demo.dto.ZSPicAppMes;
import com.example.demo.dto.ZSVideoAppMes;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Controller
public class PageController {
    @Autowired
    PicAppServiceImpl picAppService;
    @Autowired
    VideoAppServiceImpl videoAppService;
    @Autowired
    UserServiceimpl userServiceimpl;
    @Autowired
    PicAnswerImpl picAnswer;
    @Autowired
    VideoAnswerServiceImpl videoAnswerService;
    /**
     * 主页
     * */
    @RequestMapping(value = "/")
    public String hhh(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId!=null) {
            userServiceimpl.satisticTime(userId);
            modelMap.addAttribute("level",userServiceimpl.getLevel(userId));
            List<Message> messages = userServiceimpl.getNoReadMessage(userId);
            if(messages!=null && messages.size()!=0) {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script language=javascript>alert('您有" + messages.size() + "条未读信息，请及时查阅')</script>");
            }

        }
        //展示最佳信息
        ArrayList<PicMasterAnswer> picWeekMasterAnswers = picAnswer.getWeekAwsomeMaswerAnswer();
        ArrayList<PicMasterAnswer> picMonthMasterAnswers = picAnswer.getMonthAwsomeMaswerAnswer();
        ArrayList<VIdeoMasterMaster> videoWeekMasterAnswers = videoAnswerService.getWeekAwsomeMasterVideo();
        ArrayList<VIdeoMasterMaster> videoMonthMasterAnswers = videoAnswerService.getMonthAwsomeMasterVideo();
        modelMap.addAttribute("picMonthAwosomeAnswers",picMonthMasterAnswers);
        modelMap.addAttribute("picWeekAwosomeAnswers",picWeekMasterAnswers);
        modelMap.addAttribute("videoMonthAwosomeAnswers",videoMonthMasterAnswers);
        modelMap.addAttribute("videoWeekAwosomeAnswers",videoWeekMasterAnswers);

        return "index";

    }
    //登录界面
    @RequestMapping(value = "login")
    public String login(){
        return "login";
    }
    //问题大厅界面
    @RequestMapping(value = "questionhall")
    public String questionhall(ModelMap modelMap,HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute("userId");
        if(userId!=null) {
            Integer level = userServiceimpl.getLevel(userId);
            modelMap.addAttribute("level",level);
            //加时长
            userServiceimpl.satisticTime(userId);
        }

        String region = "北京";
        String type = "未解决";
        List<ZSPicAppMes> zsPicAppMes = picAppService.selectByCity(region, type);
        modelMap.addAttribute("picapps", zsPicAppMes);
        List<ZSVideoAppMes> zsVideoAppMes = videoAppService.selectByCity(region,type);
        modelMap.addAttribute("videoapps",zsVideoAppMes);
        return "questionhall";
}
    //充值界面
    @RequestMapping(value = "deposit")
    public String deposit(HttpServletRequest request,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        return "deposit";
    }


    @RequestMapping(value = "addpicapppage")
    public String picAddMes(HttpServletRequest request,ModelMap modelMap) {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        return "addpicapp";
    }
    @RequestMapping(value = "addvideoapppage")
    public String picVideoMes(HttpServletRequest request,ModelMap modelMap)
    {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        return "addvideoapp";
    }
    @RequestMapping("changepassword")
    public String changePassword(HttpServletRequest request,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        return "changepassword";
    }

}
