package com.trae.monoservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.trae.monoservice.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/match")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    @PostMapping("/recommend-jobs")
    public Map<String, Object> recommendJobs(@RequestBody Map<String, Object> params) {
        Long personId = Long.parseLong(params.get("personId").toString());
        return matchService.recommendJobs(personId, params);
    }
    
    @PostMapping("/recommend-candidates")
    public Map<String, Object> recommendCandidates(@RequestBody Map<String, Object> params) {
        Long jobId = Long.parseLong(params.get("jobId").toString());
        return matchService.recommendCandidates(jobId, params);
    }
    
    @PostMapping("/execute")
    public Map<String, Object> executeMatch(@RequestBody Map<String, Object> params) {
        Long jobId = Long.parseLong(params.get("jobId").toString());
        Long personId = Long.parseLong(params.get("personId").toString());
        return matchService.executeMatch(jobId, personId);
    }
    
    @GetMapping("/{id}/detail")
    public Map<String, Object> getMatchDetail(@PathVariable Long id) {
        return matchService.getMatchDetail(id);
    }
    
    @GetMapping("/history")
    public Map<String, Object> getMatchHistory() {
        return matchService.getMatchHistory();
    }
    
    @PostMapping("/feedback")
    public Map<String, Object> submitFeedback(@RequestBody Map<String, Object> params) {
        Long matchId = Long.parseLong(params.get("matchId").toString());
        return matchService.submitFeedback(matchId, params);
    }
}