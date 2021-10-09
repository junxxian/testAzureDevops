package com.k8sdevops.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class MyController {

    @RequestMapping("/a")
    public String test(){
        return "DevopsTest";
    }

    @RequestMapping("/b")
    public String fail() throws IOException {
        throw new IOException("测试异常");
    }
}
