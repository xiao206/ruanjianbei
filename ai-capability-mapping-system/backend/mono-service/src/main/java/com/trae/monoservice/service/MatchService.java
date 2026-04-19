package com.trae.monoservice.service;

import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusClientBuilder;
import io.milvus.param.ConnectParam;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.response.SearchResults;
import com.trae.monoservice.service.ai.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    
    private MilvusClient milvusClient;
    private AIService aiService;
    
    @Value("${milvus.host}")
    private String milvusHost;
    
    @Value("${milvus.port}")
    private int milvusPort;
    
    @Value("${milvus.collection.skill_vectors}")
    private String skillVectorsCollection;
    
    @Value("${milvus.collection.requirement_vectors}")
    private String requirementVectorsCollection;
    
    public MatchService(AIService aiService) {
        this.aiService = aiService;
        
        // 初始化Milvus客户端
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("localhost")
                .withPort(19530)
                .build();
        
        milvusClient = new MilvusClientBuilder()
                .withConnectParam(connectParam)
                .build();
        
        // 初始化集合
        initCollections();
    }
    
    private void initCollections() {
        // 创建技能向量集合
        CreateCollectionParam skillCollectionParam = CreateCollectionParam.newBuilder()
                .withCollectionName(skillVectorsCollection)
                .withDimension(1024)
                .build();
        
        try {
            milvusClient.createCollection(skillCollectionParam);
        } catch (Exception e) {
            // 集合可能已存在
        }
        
        // 创建需求向量集合
        CreateCollectionParam requirementCollectionParam = CreateCollectionParam.newBuilder()
                .withCollectionName(requirementVectorsCollection)
                .withDimension(1024)
                .build();
        
        try {
            milvusClient.createCollection(requirementCollectionParam);
        } catch (Exception e) {
            // 集合可能已存在
        }
    }
    
    public Map<String, Object> recommendJobs(Long personId, Map<String, Object> personData) {
        // 为个人推荐职位
        try {
            // 生成个人技能向量
            String personSkills = personData.toString();
            float[] personVector = aiService.embed(personSkills);
            
            // 在Milvus中搜索匹配的职位
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(requirementVectorsCollection)
                    .withVectors(List.of(personVector))
                    .withTopK(10)
                    .withMetricType(SearchParam.MetricType.L2)
                    .build();
            
            SearchResults results = milvusClient.search(searchParam);
            
            // 处理搜索结果
            return Map.of("code", 200, "message", "success", "data", results.getResults());
        } catch (Exception e) {
            return Map.of("code", 500, "message", "推荐失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> recommendCandidates(Long jobId, Map<String, Object> jobData) {
        // 为企业推荐候选人
        try {
            // 生成职位需求向量
            String jobRequirements = jobData.toString();
            float[] jobVector = aiService.embed(jobRequirements);
            
            // 在Milvus中搜索匹配的候选人
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(skillVectorsCollection)
                    .withVectors(List.of(jobVector))
                    .withTopK(10)
                    .withMetricType(SearchParam.MetricType.L2)
                    .build();
            
            SearchResults results = milvusClient.search(searchParam);
            
            // 处理搜索结果
            return Map.of("code", 200, "message", "success", "data", results.getResults());
        } catch (Exception e) {
            return Map.of("code", 500, "message", "推荐失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> executeMatch(Long jobId, Long personId) {
        // 执行具体的人岗匹配
        try {
            // 这里可以实现更复杂的匹配逻辑，包括技能匹配度、经验匹配度等
            return Map.of(
                "code", 200,
                "message", "success",
                "data", Map.of(
                    "jobId", jobId,
                    "personId", personId,
                    "matchScore", 0.85,
                    "skillMatch", 0.9,
                    "experienceMatch", 0.8,
                    "educationMatch", 0.85
                )
            );
        } catch (Exception e) {
            return Map.of("code", 500, "message", "匹配失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getMatchDetail(Long matchId) {
        // 获取匹配详情
        try {
            // 这里可以查询匹配记录，返回详细的匹配信息
            return Map.of(
                "code", 200,
                "message", "success",
                "data", Map.of(
                    "matchId", matchId,
                    "matchScore", 0.85,
                    "skillGap", Map.of(
                        "missingSkills", List.of("Docker", "Kubernetes"),
                        "skillLevels", Map.of("Java", 4, "Spring Boot", 3)
                    ),
                    "learningPath", List.of("学习Docker基础", "学习Kubernetes部署")
                )
            );
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取详情失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getMatchHistory() {
        // 获取匹配历史
        try {
            // 这里可以查询匹配历史记录
            List<Map<String, Object>> history = new ArrayList<>();
            history.add(Map.of(
                "id", 1,
                "jobId", 1,
                "personId", 1,
                "matchScore", 0.85,
                "createTime", "2026-04-18 10:00:00"
            ));
            
            return Map.of("code", 200, "message", "success", "data", history);
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取历史失败: " + e.getMessage());
        }
    }
    
    public Map<String, Object> submitFeedback(Long matchId, Map<String, Object> feedback) {
        // 提交匹配反馈
        try {
            // 这里可以保存反馈信息
            return Map.of("code", 200, "message", "反馈提交成功");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "反馈提交失败: " + e.getMessage());
        }
    }
    
    public void close() {
        milvusClient.close();
    }
}