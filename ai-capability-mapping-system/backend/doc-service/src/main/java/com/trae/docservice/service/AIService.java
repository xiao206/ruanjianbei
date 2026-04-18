package com.trae.docservice.service;

import java.util.Map;

public interface AIService {
    Map<String, Object> extractSkills(String text);
    Map<String, Object> extractRequirements(String text);
    Map<String, Object> ocrImage(byte[] imageBytes);
    Map<String, Object> embedding(String text);
}