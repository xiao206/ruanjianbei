package com.trae.monoservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {
    
    @Autowired
    private GraphService graphService;
    
    @GetMapping("/person/{id}")
    public Map<String, Object> getPersonGraph(@PathVariable Long id) {
        return graphService.getPersonGraph(id);
    }
    
    @GetMapping("/job/{id}")
    public Map<String, Object> getJobGraph(@PathVariable Long id) {
        return graphService.getJobGraph(id);
    }
    
    @PostMapping("/person/{id}")
    public Map<String, Object> buildPersonGraph(@PathVariable Long id, @RequestBody Map<String, Object> skillData) {
        return graphService.buildPersonGraph(id, skillData);
    }
    
    @PostMapping("/job/{id}")
    public Map<String, Object> buildJobGraph(@PathVariable Long id, @RequestBody Map<String, Object> jobData) {
        return graphService.buildJobGraph(id, jobData);
    }
    
    @PostMapping("/search")
    public Map<String, Object> searchGraph(@RequestBody Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        return graphService.searchGraph(keyword);
    }
}