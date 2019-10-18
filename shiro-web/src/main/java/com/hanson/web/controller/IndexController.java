package com.hanson.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class IndexController {
    @RequestMapping("/init")
    public String init() {
        return "user";
    }
}
