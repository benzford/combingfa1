package com.seckill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
    @RequestMapping(value = "/aaa",method = RequestMethod.GET)
    public String haha(){
        return "index";
    }
}
