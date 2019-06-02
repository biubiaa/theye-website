package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RunSatisticUserTime implements CommandLineRunner {
    @Autowired
    SatisticUserTime satisticUserTime;
    @Override
    public void run(String... args) throws Exception {
        satisticUserTime.start();
    }
}
