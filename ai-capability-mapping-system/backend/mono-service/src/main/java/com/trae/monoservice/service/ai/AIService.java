package com.trae.monoservice.service.ai;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class AIService {
    
    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;
    
    @Value("${ollama.model}")
    private String ollamaModel;
    
    @Value("${rapidocr.url}")
    private String rapidOcrUrl;
    
    private OllamaChatModel chatModel;
    private OllamaEmbeddingModel embeddingModel;
    
    public AIService() {
        // 初始化Ollama模型
        chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .model("qwen2.5:3b-instruct")
                .build();
        
        embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .model("bge-m3")
                .build();
    }
    
    public Map<String, Object> parseDocument(String filePath, String docType) throws Exception {
        // 读取文件内容
        String content = readFileContent(filePath);
        
        // 根据文档类型选择不同的解析策略
        if ("resume".equals(docType)) {
            return parseResume(content);
        } else if ("job".equals(docType)) {
            return parseJobDescription(content);
        } else {
            throw new IllegalArgumentException("不支持的文档类型");
        }
    }
    
    private String readFileContent(String filePath) throws IOException {
        // 读取文件内容，根据文件类型选择不同的读取方式
        Path path = Path.of(filePath);
        if (filePath.endsWith(".pdf")) {
            // 使用PDFBox读取PDF文件
            return readPdfContent(path);
        } else if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
            // 使用POI读取Word文件
            return readWordContent(path);
        } else {
            // 直接读取文本文件
            return Files.readString(path);
        }
    }
    
    private String readPdfContent(Path path) throws IOException {
        // 简化实现，实际应用中需要使用PDFBox
        return "PDF文件内容";
    }
    
    private String readWordContent(Path path) throws IOException {
        // 简化实现，实际应用中需要使用POI
        return "Word文件内容";
    }
    
    private Map<String, Object> parseResume(String content) {
        // 使用LLM解析简历内容
        String prompt = "请解析以下简历内容，提取以下信息：\n" +
                "1. 个人基本信息（姓名、性别、年龄、联系方式）\n" +
                "2. 教育背景（学校、专业、学历、毕业时间）\n" +
                "3. 工作经历（公司名称、职位、工作时间、职责）\n" +
                "4. 项目经验（项目名称、项目描述、角色、技术栈）\n" +
                "5. 技能（技能名称、熟练程度）\n" +
                "\n简历内容：" + content + "\n" +
                "\n请以JSON格式返回结果，字段名使用英文。";
        
        String response = chatModel.generate(prompt);
        
        // 解析LLM返回的JSON
        return Map.of("resume", response);
    }
    
    private Map<String, Object> parseJobDescription(String content) {
        // 使用LLM解析职位描述
        String prompt = "请解析以下职位描述，提取以下信息：\n" +
                "1. 职位基本信息（职位名称、公司名称、工作地点、薪资范围）\n" +
                "2. 岗位职责\n" +
                "3. 任职要求（技能要求、经验要求、学历要求）\n" +
                "4. 加分项\n" +
                "\n职位描述：" + content + "\n" +
                "\n请以JSON格式返回结果，字段名使用英文。";
        
        String response = chatModel.generate(prompt);
        
        // 解析LLM返回的JSON
        return Map.of("job", response);
    }
    
    public float[] embed(String text) {
        // 使用Embedding模型生成向量
        return embeddingModel.embed(text).content();
    }
}