package com.trae.adminservice.service.impl;

import com.trae.adminservice.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public Map<String, Object> getUsers() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> users = new ArrayList<>();
            users.add(Map.of(
                    "id", "1",
                    "username", "admin",
                    "name", "管理员",
                    "role", "admin",
                    "dept", "技术部",
                    "status", "active"
            ));
            users.add(Map.of(
                    "id", "2",
                    "username", "user1",
                    "name", "张三",
                    "role", "user",
                    "dept", "技术部",
                    "status", "active"
            ));

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", Map.of(
                    "users", users,
                    "total", users.size()
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "Failed to get users: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getSkills() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> skills = new ArrayList<>();
            skills.add(Map.of(
                    "id", "1",
                    "name", "Java",
                    "category", "开发技术",
                    "level", "5"
            ));
            skills.add(Map.of(
                    "id", "2",
                    "name", "Spring Boot",
                    "category", "开发技术",
                    "level", "4"
            ));
            skills.add(Map.of(
                    "id", "3",
                    "name", "MySQL",
                    "category", "数据库",
                    "level", "4"
            ));

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", Map.of(
                    "skills", skills,
                    "total", skills.size()
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "Failed to get skills: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getMonitorData() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> monitorData = Map.of(
                    "services", Map.of(
                            "doc-service", "healthy",
                            "graph-service", "healthy",
                            "match-service", "healthy",
                            "admin-service", "healthy"
                    ),
                    "databases", Map.of(
                            "mysql", "healthy",
                            "neo4j", "healthy",
                            "milvus", "healthy",
                            "redis", "healthy"
                    ),
                    "ai-models", Map.of(
                            "qwen2.5", "healthy",
                            "bge-m3", "healthy",
                            "paddleocr", "healthy"
                    )
            );

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", monitorData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "Failed to get monitor data: " + e.getMessage());
        }
        return result;
    }
}