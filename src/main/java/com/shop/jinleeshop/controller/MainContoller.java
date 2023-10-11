package com.shop.jinleeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainContoller {

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }

}
