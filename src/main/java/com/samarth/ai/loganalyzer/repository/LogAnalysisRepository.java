package com.samarth.ai.loganalyzer.repository;

import com.samarth.ai.loganalyzer.model.LogAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAnalysisRepository extends JpaRepository<LogAnalysis, Long> {
    List<LogAnalysis> findByApplicationName(String applicationName);

    List<LogAnalysis> findBySeverity(String severity);
}
