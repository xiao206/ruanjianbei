package com.trae.adminservice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.adminservice.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Mock authentication - in real implementation, check against database
            if ("admin".equals(username) && "admin123".equals(password)) {
                // Login with Sa-Token
                StpUtil.login(1);
                String token = StpUtil.getTokenValue();

                result.put("code", 200);
                result.put("message", "success");
                result.put("data", Map.of(
                        "token", token,
                        "user", Map.of(
                                "id", "1",
                                "username", "admin",
                                "name", "管理员",
                                "role", "admin"
                        )
                ));
            } else if ("user1".equals(username) && "user123".equals(password)) {
                // Login with Sa-Token
                StpUtil.login(2);
                String token = StpUtil.getTokenValue();

                result.put("code", 200);
                result.put("message", "success");
                result.put("data", Map.of(
                        "token", token,
                        "user", Map.of(
                                "id", "2",
                                "username", "user1",
                                "name", "张三",
                                "role", "user"
                        )
                ));
            } else {
                result.put("code", 401);
                result.put("message", "用户名或密码错误");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Logout with Sa-Token
            StpUtil.logout();

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", Map.of(
                    "status", "logged out"
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登出失败: " + e.getMessage());
        }
        return result;
    }
}