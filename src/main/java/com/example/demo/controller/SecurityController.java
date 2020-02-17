package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/hello")
    public String getHelloPage() {
        return "Hello";
    }
    @GetMapping("/")
    public String getSitemapPage() {
        return "Sitemap";
    }
    @GetMapping({"/home"})
    public String getHoePage() {
        return "home";
    }

    @GetMapping({"/login"})
    public String getLoginPage() {
        return "login";
    }
}
