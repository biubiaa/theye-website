package com.example.demo.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import sun.plugin2.applet.SecurityManagerHelper;

@Configuration
public class BeanConfig {
    @Bean(name = "jedis")
    public Jedis getJedis() {
        Jedis jedis = new Jedis("127.0.0.1");
        jedis.auth("123456");
        return jedis;
    }
//    @Bean(value = "adminSecurityManager")
//    public SecurityManager getadminSubject(){
//        DefaultSecurityManager securityManager = new DefaultSecurityManager();
//        securityManager.setRealm(new AdminRealm());
//        SecurityUtils.setSecurityManager(securityManager);
//        Subject subject = SecurityUtils.getSubject();
//        return subject;
//    }
//}

    //将自己的验证方式加入容器
    @Bean
    public AdminRealm myShiroRealm() {
        AdminRealm myShiroRealm = new AdminRealm();
//        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}