package com.example.demo.controller;

import com.example.demo.dao.Message;
import com.example.demo.dao.UserMes;
import com.example.demo.dto.PicMasterAnswer;
import com.example.demo.dto.VIdeoMasterMaster;
import com.example.demo.dto.ZSMessage;
import com.example.demo.service.impl.PicAnswerImpl;
import com.example.demo.service.impl.UserServiceimpl;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserServiceimpl userServiceimpl;
    @Autowired
    PicAnswerImpl picAnswer;
    @Autowired
    VideoAnswerServiceImpl videoAnswerService;

    @RequestMapping(value = "changemespage")
    public String changeMesMap(HttpServletRequest request,ModelMap model){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        model.addAttribute("level",level);
        UserMes userMes = userServiceimpl.selectById(userId);
        model.addAttribute(userMes);
        return "changemes";
    }
    @RequestMapping(value = "recharge")
    public String recharge(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) throws IOException {
        int money = Integer.parseInt(request.getParameter("money"));
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        int flag = userServiceimpl.recharger(userId,money);
        if(flag==1){
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('充值成功')</script>");
            UserMes um = userServiceimpl.selectById(userId);
            modelMap.addAttribute("user",um);
            return "mymessage";
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('操作失败，请稍后再试')</script>");
            return "deposit";
        }
    }
    //注册，输入账号密码界面
    @RequestMapping(value = "register1")
    public String register1(){
        return "register1";
    }
    //注册，输入个人信息界面
    @RequestMapping(value = "register2")
    public String register2(){
        return "register2";
    }
    //我的信息界面
    @RequestMapping(value ="mymessage")
    public String mymessage(HttpServletRequest request,ModelMap model){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        model.addAttribute("level",level);
        UserMes um = userServiceimpl.selectById(userId);
        model.addAttribute("user",um);
        //插入在线时长，回答积分
        String time = userServiceimpl.getOnlineTime(userId);
        model.addAttribute("time",time);
        String score = userServiceimpl.getAnswerScore(userId);
        model.addAttribute("answerScore",score);
        return "mymessage";
    }

    //登录提交地址
    @RequestMapping(value = "/log",method = RequestMethod.POST)
    public String log(HttpServletResponse response,HttpServletRequest request,ModelMap modelMap) throws IOException {
        String userId = request.getParameter("userId");
        userServiceimpl.satisticTime(userId);
        Integer level;
        try {
            level = userServiceimpl.getLevel(userId);
        }catch (NullPointerException e){
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('账号或密码不正确')</script>");

            return "login";
        }

        modelMap.addAttribute("level",level);
        userServiceimpl.satisticTime(userId);
//        System.out.println(userName==null);
        String password = request.getParameter("password");
        if(userId!=null&&password!=null) {
            int flag = userServiceimpl.checkPass(userId, password);

            if (flag == 1) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("nickName",userServiceimpl.gerNickName(userId));
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println ("<script language=javascript>alert('登录成功')</script>");
                ArrayList<PicMasterAnswer> picWeekMasterAnswers = picAnswer.getWeekAwsomeMaswerAnswer();
                ArrayList<PicMasterAnswer> picMonthMasterAnswers = picAnswer.getMonthAwsomeMaswerAnswer();
                ArrayList<VIdeoMasterMaster> videoWeekMasterAnswers = videoAnswerService.getWeekAwsomeMasterVideo();
                ArrayList<VIdeoMasterMaster> videoMonthMasterAnswers = videoAnswerService.getMonthAwsomeMasterVideo();
                modelMap.addAttribute("picMonthAwosomeAnswers",picMonthMasterAnswers);
                modelMap.addAttribute("picWeekAwosomeAnswers",picWeekMasterAnswers);
                modelMap.addAttribute("videoMonthAwosomeAnswers",videoMonthMasterAnswers);
                modelMap.addAttribute("videoWeekAwosomeAnswers",videoWeekMasterAnswers);
                return "index";
            }else{
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println ("<script language=javascript>alert('账号或密码不正确')</script>");

                return "login";
            }
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('账户或密码不能为空')</script>");
            return "login";
        }
    }

    //注册1提交地址
    @RequestMapping(value = "registermes1",method = RequestMethod.POST)
    public String register1 (HttpServletRequest request,HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        String userId = request.getParameter("userId");
        String password = request.getParameter("password1");
        int flag = userServiceimpl.insertUser(userId,password);
        if(flag==1){
            try {
                //插入成功
                session.setAttribute("userId",userId);
                response.sendRedirect("http://127.0.0.1:8080/register2");
                return "register2";
            }catch (Exception e){
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println ("<script language=javascript>alert('系统繁忙请稍后再试')</script>");
                return "register1";
            }
        }else{
            //插入失败
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('账号已被注册，请重新输入')</script>");
            return "register1";
        }
    }
    //注册2提交个人信息
    @RequestMapping(value = "/registermes2",method = RequestMethod.POST)
//    @ResponseBody
    public String register2(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String nickName = (String)request.getParameter("nickName");
        String email = (String)request.getParameter("email");
        String phone = (String ) request.getParameter("phone");
        String region = (String) request.getParameter("city-picker3");
        String signature =  request.getParameter("signature");
        session.setAttribute("nickName",nickName);
        UserMes um = new UserMes();
        um.setUserId(userId);
        um.setNickname(nickName);
        um.setEmail(email);
        um.setPhone(phone);
        um.setRegion(region);
        um.setSignature(signature);

        if(userServiceimpl.updateMes(um)==1)
            return "index";
        else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('注册失败，请稍后再试')</script>");
        }
        return "/";
    }

    //注册2提交个人信息
    @RequestMapping(value = "/changemes",method = RequestMethod.POST)
//    @ResponseBody
    public String changemes(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        model.addAttribute("level",level);

        //插入在线时长，回答积分
        String time = userServiceimpl.getOnlineTime(userId);
        model.addAttribute("time",time);
        String nickName = (String)request.getParameter("nickName");
        String email = (String)request.getParameter("email");
        String phone = (String ) request.getParameter("phone");
        String region = (String) request.getParameter("city-picker3");
        String signature =  request.getParameter("signature");
        request.getSession().setAttribute("nickName",nickName);
        UserMes um = userServiceimpl.selectById(userId);
        //插入在线时长，回答积分
        model.addAttribute("time",time);
        if(nickName!=""){
            um.setNickname(nickName);
        }
        if(email!=""){
            um.setEmail(email);
        }
        if(phone!=""){
            um.setPhone(phone);
        }
        if(region!=""){
            um.setSignature(region);
        }
        if(signature!=""){
            um.setSignature(signature);
        }
        model.addAttribute("user",um);
        if(userServiceimpl.updateMes(um)==1) {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script language=javascript>alert('账户信息修改成功')</script>");
            return "mymessage";
        }
        else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('修改失败，请稍后再试')</script>");
        }
        return "mymessage";
    }
    //注销
    @RequestMapping(value = "signout")
    public String signout(HttpServletRequest request,ModelMap modelMap){
        HttpSession session = request.getSession();
        session.invalidate();
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
    /**
     * 修改账号密码
     * */
    @RequestMapping("ccpassword")
    public String changePassword(@RequestParam("oldpassword")String oldPassword,@RequestParam("password1")String newPassword, HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws IOException {
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        int flag = userServiceimpl.changepPsswod(userId,oldPassword,newPassword);
        if(flag == 1) {//审核成功
            ArrayList<PicMasterAnswer> picWeekMasterAnswers = picAnswer.getWeekAwsomeMaswerAnswer();
            ArrayList<PicMasterAnswer> picMonthMasterAnswers = picAnswer.getMonthAwsomeMaswerAnswer();
            ArrayList<VIdeoMasterMaster> videoWeekMasterAnswers = videoAnswerService.getWeekAwsomeMasterVideo();
            ArrayList<VIdeoMasterMaster> videoMonthMasterAnswers = videoAnswerService.getMonthAwsomeMasterVideo();
            modelMap.addAttribute("picMonthAwosomeAnswers", picMonthMasterAnswers);
            modelMap.addAttribute("picWeekAwosomeAnswers", picWeekMasterAnswers);
            modelMap.addAttribute("videoMonthAwosomeAnswers", videoMonthMasterAnswers);
            modelMap.addAttribute("videoWeekAwosomeAnswers", videoWeekMasterAnswers);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('修改成功')</script>");
            return "index";
        }else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('您的原始密码输入错误，请重新尝试')</script>");
            return "changepassword";
        }
    }
    /**
     * 跳转到我的消息界面
     * */
    @RequestMapping(value = "notice")
    public String getNotice(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
        String userId = (String) request.getSession().getAttribute("userId");
        userServiceimpl.satisticTime(userId);
        Integer level = userServiceimpl.getLevel(userId);
        modelMap.addAttribute("level",level);
        List<ZSMessage> messages = userServiceimpl.getMessageByUserId(userId);
        modelMap.addAttribute("messages",messages);
        return "notice";
    }
}
