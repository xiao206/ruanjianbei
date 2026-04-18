package com.trae.adminservice.service;

import java.util.Map;

public interface AdminService {
    Map<String, Object> getUsers();
    Map<String, Object> getSkills();
    Map<String, Object> getMonitorData();
}