package com.trae.graphservice.service;

import java.util.Map;

public interface GraphService {
    Map<String, Object> getPersonCapabilityGraph(String personId);
    Map<String, Object> getRequirementGraph(String requirementId);
    Map<String, Object> searchGraph(String keyword);
}