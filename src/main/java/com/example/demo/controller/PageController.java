package com.example.demo.controller;

import com.example.demo.dto.ZSPicAppMes;
import com.example.demo.dto.ZSVideoAppMes;
import com.example.demo.service.impl.PicAppServiceImpl;
import com.example.demo.service.impl.VideoAppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class PageController {
    @Autowired
    PicAppServiceImpl picAppService;
    @Autowired
    VideoAppServiceImpl videoAppService;
    @RequestMapping(value = "/")
//    @ResponseBody
    public String hhh(HttpServletRequest request){
        request.getSession().setAttribute("userId","1");
        request.getSession().setAttribute("nickName","天天");
        return "index";

    }
    //登录界面
    @RequestMapping(value = "login")
    public String login(){
        return "login";
    }
    //问题大厅界面
    @RequestMapping(value = "questionhall")
    public String questionhall(ModelMap modelMap){
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
    public String deposit(){
        return "deposit";
    }


    @RequestMapping(value = "addpicapppage")
    public String picAddMes() {
        return "addpicapp";
    }
    @RequestMapping(value = "addvideoapppage")
    public String picVideoMes() {
        return "addvideoapp";
    }
}
