package com.example.demo;

import com.example.demo.util.SpringUtil;
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
}
