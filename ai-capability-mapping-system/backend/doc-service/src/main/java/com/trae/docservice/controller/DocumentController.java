package com.trae.docservice.controller;

import com.trae.docservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file, 
                                                        @RequestParam("type") String type) {
        return ResponseEntity.ok(documentService.uploadFile(file, type));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(documentService.getParseStatus(id));
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<Map<String, Object>> getResult(@PathVariable String id) {
        return ResponseEntity.ok(documentService.getParseResult(id));
    }
}