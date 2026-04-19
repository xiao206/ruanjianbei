package com.trae.monoservice.service;

import com.trae.monoservice.entity.Document;
import com.trae.monoservice.mapper.DocumentMapper;
import com.trae.monoservice.service.ai.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {
    
    @Autowired
    private DocumentMapper documentMapper;
    
    @Autowired
    private AIService aiService;
    
    @Value("${system.max-file-size}")
    private String maxFileSize;
    
    private String uploadDir = "/tmp/documents/";
    
    public Map<String, Object> upload(MultipartFile file, String docType, long userId) {
        // 检查文件大小
        long maxSize = Long.parseLong(maxFileSize.replace("MB", "")) * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Map.of("code", 400, "message", "文件大小超过限制");
        }
        
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".doc") && !originalFilename.endsWith(".docx") && !originalFilename.endsWith(".pdf")) {
            return Map.of("code", 400, "message", "只支持DOC、DOCX、PDF格式");
        }
        
        // 保存文件
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        File dest = new File(uploadDir + fileName);
        
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return Map.of("code", 500, "message", "文件保存失败");
        }
        
        // 保存文档记录
        Document document = new Document();
        document.setFileName(originalFilename);
        document.setFilePath(dest.getAbsolutePath());
        document.setDocType(docType);
        document.setStatus("pending");
        document.setUserId(userId);
        
        documentMapper.insert(document);
        
        // 异步解析文档
        parseDocument(document.getId(), dest.getAbsolutePath(), docType);
        
        return Map.of(
            "code", 200,
            "message", "上传成功",
            "data", Map.of("id", document.getId())
        );
    }
    
    @Async
    public void parseDocument(Long docId, String filePath, String docType) {
        // 更新状态为处理中
        Document document = documentMapper.findById(docId);
        document.setStatus("processing");
        documentMapper.updateById(document);
        
        try {
            // 调用AI服务解析文档
            Map<String, Object> result = aiService.parseDocument(filePath, docType);
            
            // 更新解析结果
            document.setStatus("completed");
            document.setResultJson(result.toString());
            documentMapper.updateById(document);
            
        } catch (Exception e) {
            // 更新状态为失败
            document.setStatus("failed");
            document.setResultJson("{\"error\": \"" + e.getMessage() + "\"}");
            documentMapper.updateById(document);
        }
    }
    
    public Map<String, Object> getStatus(Long id) {
        Document document = documentMapper.findById(id);
        if (document == null) {
            return Map.of("code", 404, "message", "文档不存在");
        }
        
        return Map.of(
            "code", 200,
            "message", "success",
            "data", Map.of(
                "id", document.getId(),
                "status", document.getStatus(),
                "fileName", document.getFileName()
            )
        );
    }
    
    public Map<String, Object> getResult(Long id) {
        Document document = documentMapper.findById(id);
        if (document == null) {
            return Map.of("code", 404, "message", "文档不存在");
        }
        
        if (!"completed".equals(document.getStatus())) {
            return Map.of("code", 400, "message", "文档解析尚未完成");
        }
        
        return Map.of(
            "code", 200,
            "message", "success",
            "data", Map.of(
                "id", document.getId(),
                "fileName", document.getFileName(),
                "result", document.getResultJson()
            )
        );
    }
}