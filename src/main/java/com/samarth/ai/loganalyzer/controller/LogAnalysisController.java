package com.samarth.ai.loganalyzer.controller;

import com.samarth.ai.loganalyzer.model.AnalysisRequest;
import com.samarth.ai.loganalyzer.model.AnalysisResponse;
import com.samarth.ai.loganalyzer.model.LogAnalysis;
import com.samarth.ai.loganalyzer.service.LogAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
// CORS is configured globally in WebConfig — see cors.allowed-origins in application.yml
public class LogAnalysisController {

    private final LogAnalysisService logAnalysisService;

    public LogAnalysisController(LogAnalysisService logAnalysisService) {
        this.logAnalysisService = logAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyzeLog(@Valid @RequestBody AnalysisRequest request) {
        AnalysisResponse response = logAnalysisService.analyzeLog(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LogAnalysis>> getAllLogs() {
        return ResponseEntity.ok(logAnalysisService.getAllLogs());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Log Analyzer is running. Database and OCI services initialized.");
    }
}
