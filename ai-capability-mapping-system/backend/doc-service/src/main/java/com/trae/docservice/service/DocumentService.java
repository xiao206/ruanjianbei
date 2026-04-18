package com.trae.docservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DocumentService {
    Map<String, Object> uploadFile(MultipartFile file, String type);
    Map<String, Object> getParseStatus(String id);
    Map<String, Object> getParseResult(String id);
}