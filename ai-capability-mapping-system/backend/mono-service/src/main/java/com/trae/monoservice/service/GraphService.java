package com.trae.monoservice.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GraphService {
    
    private Driver driver;
    
    public GraphService(@Value("${spring.data.neo4j.uri}") String uri,
                        @Value("${spring.data.neo4j.username}") String username,
                        @Value("${spring.data.neo4j.password}") String password) {
        // 初始化Neo4j驱动
        driver = GraphDatabase.driver(uri, org.neo4j.driver.AuthTokens.basic(username, password));
    }
    
    public Map<String, Object> getPersonGraph(Long personId) {
        // 获取个人能力图谱
        try (Session session = driver.session()) {
            String cypher = "MATCH (p:Person {id: $personId})-[r:HAS_SKILL]->(s:Skill) RETURN p, r, s";
            var result = session.run(cypher, Map.of("personId", personId));
            
            // 处理查询结果
            return Map.of("code", 200, "message", "success", "data", result.list());
        }
    }
    
    public Map<String, Object> getJobGraph(Long jobId) {
        // 获取职位能力图谱
        try (Session session = driver.session()) {
            String cypher = "MATCH (j:Job {id: $jobId})-[r:REQUIRES_SKILL]->(s:Skill) RETURN j, r, s";
            var result = session.run(cypher, Map.of("jobId", jobId));
            
            // 处理查询结果
            return Map.of("code", 200, "message", "success", "data", result.list());
        }
    }
    
    public Map<String, Object> buildPersonGraph(Long personId, Map<String, Object> skillData) {
        // 构建个人能力图谱
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                // 创建个人节点
                tx.run("MERGE (p:Person {id: $personId}) SET p.name = $name", 
                       Map.of("personId", personId, "name", skillData.get("name")));
                
                // 添加技能关系
                if (skillData.containsKey("skills")) {
                    var skills = (Map<String, Object>) skillData.get("skills");
                    for (Map.Entry<String, Object> entry : skills.entrySet()) {
                        String skillName = entry.getKey();
                        Map<String, Object> skillInfo = (Map<String, Object>) entry.getValue();
                        
                        // 创建技能节点
                        tx.run("MERGE (s:Skill {name: $skillName})", Map.of("skillName", skillName));
                        
                        // 创建个人与技能的关系
                        tx.run("MATCH (p:Person {id: $personId}), (s:Skill {name: $skillName}) " +
                               "MERGE (p)-[r:HAS_SKILL]->(s) " +
                               "SET r.level = $level, r.years = $years",
                               Map.of("personId", personId, "skillName", skillName,
                                      "level", skillInfo.get("level"), "years", skillInfo.get("years")));
                    }
                }
                
                return null;
            });
            
            return Map.of("code", 200, "message", "图谱构建成功");
        }
    }
    
    public Map<String, Object> buildJobGraph(Long jobId, Map<String, Object> jobData) {
        // 构建职位能力图谱
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                // 创建职位节点
                tx.run("MERGE (j:Job {id: $jobId}) SET j.title = $title, j.company = $company", 
                       Map.of("jobId", jobId, "title", jobData.get("title"), "company", jobData.get("company")));
                
                // 添加技能要求
                if (jobData.containsKey("skills")) {
                    var skills = (Map<String, Object>) jobData.get("skills");
                    for (Map.Entry<String, Object> entry : skills.entrySet()) {
                        String skillName = entry.getKey();
                        Map<String, Object> skillInfo = (Map<String, Object>) entry.getValue();
                        
                        // 创建技能节点
                        tx.run("MERGE (s:Skill {name: $skillName})", Map.of("skillName", skillName));
                        
                        // 创建职位与技能的关系
                        tx.run("MATCH (j:Job {id: $jobId}), (s:Skill {name: $skillName}) " +
                               "MERGE (j)-[r:REQUIRES_SKILL]->(s) " +
                               "SET r.level = $level, r.required = $required",
                               Map.of("jobId", jobId, "skillName", skillName,
                                      "level", skillInfo.get("level"), "required", skillInfo.get("required")));
                    }
                }
                
                return null;
            });
            
            return Map.of("code", 200, "message", "图谱构建成功");
        }
    }
    
    public Map<String, Object> searchGraph(String keyword) {
        // 搜索图谱节点
        try (Session session = driver.session()) {
            String cypher = "MATCH (n) WHERE n.name CONTAINS $keyword OR n.title CONTAINS $keyword RETURN n LIMIT 20";
            var result = session.run(cypher, Map.of("keyword", keyword));
            
            // 处理查询结果
            return Map.of("code", 200, "message", "success", "data", result.list());
        }
    }
    
    public void close() {
        driver.close();
    }
}