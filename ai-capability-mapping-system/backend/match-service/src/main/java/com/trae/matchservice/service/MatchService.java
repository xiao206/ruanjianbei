package com.trae.matchservice.service;

import java.util.Map;

public interface MatchService {
    Map<String, Object> executeMatch(String requirementId);
    Map<String, Object> getMatchDetail(String matchId);
    Map<String, Object> getMatchHistory();
    Map<String, Object> submitFeedback(String recordId, Integer rating, String comment);
}