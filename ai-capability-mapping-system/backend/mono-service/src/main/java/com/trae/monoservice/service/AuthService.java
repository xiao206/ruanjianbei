package com.trae.monoservice.service;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.entity.User;
import com.trae.monoservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    public Map<String, Object> register(Map<String, Object> params) {
        // 实现用户注册逻辑
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String userType = (String) params.get("userType"); // personal 或 enterprise
        
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(username);
        if (existingUser != null) {
            return Map.of("code", 400, "message", "用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 实际应用中需要加密
        user.setUserType(userType);
        user.setStatus("active");
        
        userMapper.insert(user);
        
        return Map.of("code", 200, "message", "注册成功");
    }
    
    public Map<String, Object> login(Map<String, Object> params) {
        // 实现用户登录逻辑
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        
        // 查找用户
        User user = userMapper.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Map.of("code", 401, "message", "用户名或密码错误");
        }
        
        // 登录
        StpUtil.login(user.getId());
        
        return Map.of(
            "code", 200,
            "message", "登录成功",
            "data", Map.of(
                "token", StpUtil.getTokenValue(),
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "userType", user.getUserType()
                )
            )
        );
    }
    
    public Map<String, Object> getInfo() {
        // 获取用户信息
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.findById(userId);
        
        return Map.of(
            "code", 200,
            "message", "success",
            "data", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "userType", user.getUserType()
            )
        );
    }
}