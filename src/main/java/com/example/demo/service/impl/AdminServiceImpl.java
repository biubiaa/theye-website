package com.example.demo.service.impl;

import com.example.demo.config.AdminRealm;
import com.example.demo.mapper.AdminMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl {
    @Autowired
    AdminMapper adminMapper;
    public String getPwd(String id){
        return adminMapper.selectPwd(id);
    }
    /**
     * 登录验证
     * */
    public boolean login(String adminId,String pwd){
        Md5Hash md5Hash = new Md5Hash(pwd,"xth.com",10);
        pwd = md5Hash.toString();

        //先加密这里还没有写
        UsernamePasswordToken token = new UsernamePasswordToken(adminId,pwd);

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setPwd(getPwd(adminId));
        securityManager.setRealm(adminRealm);
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                return true;
            }
        }catch (AuthenticationException e){
            return false;
        }
        return false;
    }
}
