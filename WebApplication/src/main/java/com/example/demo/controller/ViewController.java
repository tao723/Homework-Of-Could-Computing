package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 路径访问控制
 * @author debonet
 */
@Controller
public class ViewController {

    @RequestMapping(value = "index")
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = "stream")
    public String getHello(){
        return "stream";
    }
}
