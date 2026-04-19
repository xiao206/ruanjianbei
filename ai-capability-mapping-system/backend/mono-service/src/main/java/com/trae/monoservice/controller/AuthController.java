package com.trae.monoservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> params) {
        return authService.register(params);
    }
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> params) {
        return authService.login(params);
    }
    
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        StpUtil.logout();
        return Map.of("code", 200, "message", "success");
    }
    
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return authService.getInfo();
    }
}