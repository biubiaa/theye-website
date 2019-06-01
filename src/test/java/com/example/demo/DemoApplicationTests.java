package com.example.demo;

import com.example.demo.config.AdminRealm;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.service.impl.AdminServiceImpl;
import com.example.demo.service.impl.UserServiceimpl;
import com.example.demo.util.SpringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    RedisConnectionFactory factory;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AdminServiceImpl adminService;
    @Autowired
    UserServiceimpl userServiceimpl;
    @Test
    public void contextLoads() {
    }
    @Test
    public void test1(){
        File file = new File("/opt");
        String [] filename = file.list();
        for (String a : filename
             ) {
            System.out.println(a);
        }
     }
     @Test
    public void test2() throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());//获取Spring boot项目的根路径，在开发时获取到的是/target/classes/
         File file = new File(path.getAbsolutePath(),"static/images/upload/pic/7/");
         System.out.println(file.getAbsolutePath());
         File[] files = file.listFiles();
         for (File f: files
              ) {
             System.out.println(f.getName());
         }
     }
     @Test
    public void testRedis(){
        Jedis jedis = (Jedis) SpringUtil.getBean("jedis");
//         Jedis jedis = new Jedis("127.0.0.1");
//         jedis.auth("123456");
         jedis.set("a","xutianhaoniubi");
         String get = jedis.get("a");
         System.out.println("a:"+get);
     }
     @Test
    public void setTest(){
        String a = new String("aaaa");
        String b = new String("aaaa");
        TreeSet<String > c = new TreeSet<String>();
        c.add(a);
         System.out.println("$$$$$$$$$$$$$$$$:"+c.contains(b));

     }
     @Test
     public void getAdminPwd(){
        String pwd = adminMapper.selectPwd("admin");
         System.out.println(pwd);
     }
     @Test
    public void shi1roTest(){
        String pwd = new AdminRealm().getPwd();
         System.out.println(pwd);
     }
    @Test
    public void shiroTest(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setPwd(adminMapper.selectPwd("admin"));
        defaultSecurityManager.setRealm(adminRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        try {
            if (subject.isAuthenticated() == true) {
                System.out.println("登录成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("登录失败");
        }
    }
    @Test
    public void adminLoginTest(){
        System.out.println(adminService.login("admin","123456"));
    }
    @Test
    public void md5Test(){
        Md5Hash md5Hash = new Md5Hash("1","xth.com",10);
        System.out.println(md5Hash.toString());
    }
    @Test
    public void userLoginTest(){
        System.out.println(userServiceimpl.checkPass("1","1"));
    }
}
