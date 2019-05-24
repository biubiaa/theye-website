package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SaController {

    @RequestMapping(value = "shuayishu")
    public String shuayishua(){
        return "picshua";
    }

}
