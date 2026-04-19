package com.trae.monoservice.service;

import com.trae.monoservice.entity.User;
import com.trae.monoservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    
    @Autowired
    private UserMapper userMapper;
    
    public Map<String, Object> getUsers(int pageNum, int pageSize) {
        // 获取用户列表
        try {
            // 这里可以实现分页查询
            List<User> users = userMapper.selectList(null);
            
            // 模拟分页
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, users.size());
            List<User> pageUsers = users.subList(start, end);
            
            return Map.of(
                "code", 200,
                "message", "success",
                "data", Map.of(
                    "list", pageUsers,
                    "total", users.size(),
                    "pageNum", pageNum,
                    "pageSize", pageSize
                )
            );
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取用户列表失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> addUser(Map<String, Object> userData) {
        // 添加用户
        try {
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setUserType((String) userData.get("userType"));
            user.setStatus("active");
            
            userMapper.insert(user);
            
            return Map.of("code", 200, "message", "添加用户成功");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "添加用户失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> updateUser(Long id, Map<String, Object> userData) {
        // 更新用户
        try {
            User user = userMapper.findById(id);
            if (user == null) {
                return Map.of("code", 404, "message", "用户不存在");
            }
            
            if (userData.containsKey("username")) {
                user.setUsername((String) userData.get("username"));
            }
            if (userData.containsKey("password")) {
                user.setPassword((String) userData.get("password"));
            }
            if (userData.containsKey("userType")) {
                user.setUserType((String) userData.get("userType"));
            }
            if (userData.containsKey("status")) {
                user.setStatus((String) userData.get("status"));
            }
            
            userMapper.updateById(user);
            
            return Map.of("code", 200, "message", "更新用户成功");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "更新用户失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteUser(Long id) {
        // 删除用户
        try {
            userMapper.deleteById(id);
            return Map.of("code", 200, "message", "删除用户成功");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "删除用户失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getSkills() {
        // 获取技能列表
        try {
            // 这里可以从数据库或缓存中获取技能列表
            List<Map<String, Object>> skills = new ArrayList<>();
            skills.add(Map.of("id", 1, "name", "Java", "category", "编程语言"));
            skills.add(Map.of("id", 2, "name", "Python", "category", "编程语言"));
            skills.add(Map.of("id", 3, "name", "Spring Boot", "category", "框架"));
            skills.add(Map.of("id", 4, "name", "Vue", "category", "前端"));
            skills.add(Map.of("id", 5, "name", "MySQL", "category", "数据库"));
            
            return Map.of("code", 200, "message", "success", "data", skills);
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取技能列表失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> addSkill(Map<String, Object> skillData) {
        // 添加技能
        try {
            // 这里可以实现添加技能的逻辑
            return Map.of("code", 200, "message", "添加技能成功");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "添加技能失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getMonitorData() {
        // 获取系统监控数据
        try {
            // 这里可以实现系统监控数据的获取
            Map<String, Object> monitorData = Map.of(
                "cpu", Map.of("usage", 45.2, "cores", 4),
                "memory", Map.of("usage", 60.5, "total", 8192, "used", 4915),
                "disk", Map.of("usage", 30.2, "total", 256, "used", 76.8),
                "services", Map.of(
                    "backend", "running",
                    "neo4j", "running",
                    "milvus", "running",
                    "ollama", "running"
                )
            );
            
            return Map.of("code", 200, "message", "success", "data", monitorData);
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取监控数据失败: " + e.getMessage());
        }
    }
}