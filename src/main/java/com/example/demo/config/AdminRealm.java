package com.example.demo.config;

import com.example.demo.mapper.AdminMapper;
import com.example.demo.service.impl.AdminServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
public class AdminRealm extends AuthorizingRealm {

    @Override
    public String getName(){
        return "AdminRealm";
    }
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String adminId = (String) token.getPrincipal();
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(adminId,pwd,getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


}
