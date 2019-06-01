package com.example.demo.controller;

import com.example.demo.dao.UserMes;
import com.example.demo.service.impl.UserServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class UserController {
    @Autowired
    UserServiceimpl userServiceimpl;

    @RequestMapping(value = "changemespage")
    public String changeMesMap(HttpServletRequest request,ModelMap model){
        String userId = (String )request.getSession().getAttribute("userId");
        UserMes userMes = userServiceimpl.selectById(userId);
        model.addAttribute(userMes);
        return "changemes";
    }
    @RequestMapping(value = "recharge")
    public String recharge(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) throws IOException {
        int money = Integer.parseInt(request.getParameter("money"));
        String userId = (String)request.getSession().getAttribute("userId");
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
        UserMes um = userServiceimpl.selectById(userId);
        model.addAttribute("user",um);
        return "mymessage";
    }

    //登录提交地址
    @RequestMapping(value = "/log",method = RequestMethod.POST)
    public String log(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String userId = request.getParameter("userId");
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
        System.out.println("password:"+password);
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
    public String changemes(HttpServletRequest request,HttpServletResponse response) throws IOException {
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
            return "mymessage";
        else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println ("<script language=javascript>alert('注册失败，请稍后再试')</script>");
        }
        return "/";
    }
    //注销
    @RequestMapping(value = "signout")
    public String signout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "index";
    }
}
