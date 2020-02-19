package com.example.demo.controller;

import com.example.demo.dto.JwtRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @GetMapping("/{hello}")
    public JwtRequest echoHello(@PathVariable String hello) {
        return new JwtRequest();
    }

}
