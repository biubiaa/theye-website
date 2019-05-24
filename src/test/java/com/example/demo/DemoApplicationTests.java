package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

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
         //得到一个连接
         RedisConnection conn = factory.getConnection();
         conn.set("hello".getBytes(), "world".getBytes());
         System.out.println(new String(conn.get("hello".getBytes())));
     }
}
