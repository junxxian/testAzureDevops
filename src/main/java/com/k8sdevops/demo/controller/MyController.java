package com.k8sdevops.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyController {

    @RequestMapping("/a")
    public String test(){
        return "DevopsTest";
    }
}
