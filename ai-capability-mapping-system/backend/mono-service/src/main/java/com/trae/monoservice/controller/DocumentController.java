package com.trae.monoservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;
    
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file, @RequestParam("docType") String docType) {
        long userId = StpUtil.getLoginIdAsLong();
        return documentService.upload(file, docType, userId);
    }
    
    @GetMapping("/{id}/status")
    public Map<String, Object> getStatus(@PathVariable Long id) {
        return documentService.getStatus(id);
    }
    
    @GetMapping("/{id}/result")
    public Map<String, Object> getResult(@PathVariable Long id) {
        return documentService.getResult(id);
    }
}