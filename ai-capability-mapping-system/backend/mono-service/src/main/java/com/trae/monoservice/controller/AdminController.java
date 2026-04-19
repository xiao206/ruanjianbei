package com.trae.monoservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/users")
    public Map<String, Object> getUsers(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return adminService.getUsers(pageNum, pageSize);
    }
    
    @PostMapping("/users")
    public Map<String, Object> addUser(@RequestBody Map<String, Object> user) {
        return adminService.addUser(user);
    }
    
    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> user) {
        return adminService.updateUser(id, user);
    }
    
    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id);
    }
    
    @GetMapping("/skills")
    public Map<String, Object> getSkills() {
        return adminService.getSkills();
    }
    
    @PostMapping("/skills")
    public Map<String, Object> addSkill(@RequestBody Map<String, Object> skill) {
        return adminService.addSkill(skill);
    }
    
    @GetMapping("/monitor")
    public Map<String, Object> getMonitorData() {
        return adminService.getMonitorData();
    }
}