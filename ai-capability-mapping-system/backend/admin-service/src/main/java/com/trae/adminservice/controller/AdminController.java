package com.trae.adminservice.controller;

import com.trae.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @GetMapping("/skills")
    public ResponseEntity<Map<String, Object>> getSkills() {
        return ResponseEntity.ok(adminService.getSkills());
    }

    @GetMapping("/monitor")
    public ResponseEntity<Map<String, Object>> getMonitorData() {
        return ResponseEntity.ok(adminService.getMonitorData());
    }
}