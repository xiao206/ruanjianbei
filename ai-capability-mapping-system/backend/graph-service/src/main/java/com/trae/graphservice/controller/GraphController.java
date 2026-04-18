package com.trae.graphservice.controller;

import com.trae.graphservice.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @GetMapping("/person/{id}")
    public ResponseEntity<Map<String, Object>> getPersonGraph(@PathVariable String id) {
        return ResponseEntity.ok(graphService.getPersonCapabilityGraph(id));
    }

    @GetMapping("/requirement/{id}")
    public ResponseEntity<Map<String, Object>> getRequirementGraph(@PathVariable String id) {
        return ResponseEntity.ok(graphService.getRequirementGraph(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> searchGraph(@RequestBody Map<String, Object> request) {
        String keyword = (String) request.get("keyword");
        return ResponseEntity.ok(graphService.searchGraph(keyword));
    }
}