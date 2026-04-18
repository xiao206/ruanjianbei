package com.trae.matchservice.controller;

import com.trae.matchservice.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeMatch(@RequestBody Map<String, Object> request) {
        String requirementId = (String) request.get("requirementId");
        return ResponseEntity.ok(matchService.executeMatch(requirementId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<Map<String, Object>> getMatchDetail(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatchDetail(id));
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getMatchHistory() {
        return ResponseEntity.ok(matchService.getMatchHistory());
    }

    @PostMapping("/feedback")
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Map<String, Object> request) {
        String recordId = (String) request.get("recordId");
        Integer rating = (Integer) request.get("rating");
        String comment = (String) request.get("comment");
        return ResponseEntity.ok(matchService.submitFeedback(recordId, rating, comment));
    }
}