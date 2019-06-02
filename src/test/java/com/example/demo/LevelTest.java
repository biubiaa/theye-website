package com.example.demo;

import com.example.demo.mapper.LevelMapper;
import com.example.demo.mapper.LevelScoreMapper;
import com.example.demo.service.impl.UserServiceimpl;
import com.example.demo.util.SatisticUserTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static java.lang.Thread.sleep;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelTest {
    @Autowired
    LevelMapper levelMapper;
    @Autowired
    LevelScoreMapper levelScoreMapper;
    @Autowired
    UserServiceimpl serviceimpl;
    @Autowired
    SatisticUserTime satisticUserTime;
    @Test
    public void testGetLevel(){
        System.out.println(levelMapper.selectLevel(2));
        System.out.println(levelMapper.selectLevel(3222));
    }
    @Test
    public void a(){
        int a = (int) (10 +0.1*99);
        System.out.println(a);
    }
@Test
    public void b(){
        int a = serviceimpl.getLevel("1");
    System.out.println(a);
    }
//    @Test
//    public void c() throws InterruptedException {
//        satisticUserTime.start();
//        sleep(5000);
//    }
}
