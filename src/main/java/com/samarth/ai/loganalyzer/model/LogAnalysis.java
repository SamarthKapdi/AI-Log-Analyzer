package com.samarth.ai.loganalyzer.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOG_ANALYSIS")
public class LogAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String applicationName;

    @Lob
    @Column(nullable = false)
    private String logText;

    @Column(nullable = false)
    private String severity;

    private String sentiment;

    private Double confidenceScore;

    @Column(length = 4000)
    private String keyPhrases;

    @Column(length = 4000)
    private String detectedEntities;

    @Column(length = 2000)
    private String rootCauseHint;

    @CreationTimestamp
    private LocalDateTime timestamp;

    // --- No-arg constructor (required by JPA) ---
    public LogAnalysis() {}

    // --- All-arg constructor ---
    public LogAnalysis(Long id, String applicationName, String logText, String severity,
                       String sentiment, Double confidenceScore, String keyPhrases,
                       String detectedEntities, String rootCauseHint, LocalDateTime timestamp) {
        this.id = id;
        this.applicationName = applicationName;
        this.logText = logText;
        this.severity = severity;
        this.sentiment = sentiment;
        this.confidenceScore = confidenceScore;
        this.keyPhrases = keyPhrases;
        this.detectedEntities = detectedEntities;
        this.rootCauseHint = rootCauseHint;
        this.timestamp = timestamp;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getApplicationName() { return applicationName; }
    public String getLogText() { return logText; }
    public String getSeverity() { return severity; }
    public String getSentiment() { return sentiment; }
    public Double getConfidenceScore() { return confidenceScore; }
    public String getKeyPhrases() { return keyPhrases; }
    public String getDetectedEntities() { return detectedEntities; }
    public String getRootCauseHint() { return rootCauseHint; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
    public void setLogText(String logText) { this.logText = logText; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setKeyPhrases(String keyPhrases) { this.keyPhrases = keyPhrases; }
    public void setDetectedEntities(String detectedEntities) { this.detectedEntities = detectedEntities; }
    public void setRootCauseHint(String rootCauseHint) { this.rootCauseHint = rootCauseHint; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String applicationName;
        private String logText;
        private String severity;
        private String sentiment;
        private Double confidenceScore;
        private String keyPhrases;
        private String detectedEntities;
        private String rootCauseHint;
        private LocalDateTime timestamp;

        public Builder id(Long v) { this.id = v; return this; }
        public Builder applicationName(String v) { this.applicationName = v; return this; }
        public Builder logText(String v) { this.logText = v; return this; }
        public Builder severity(String v) { this.severity = v; return this; }
        public Builder sentiment(String v) { this.sentiment = v; return this; }
        public Builder confidenceScore(Double v) { this.confidenceScore = v; return this; }
        public Builder keyPhrases(String v) { this.keyPhrases = v; return this; }
        public Builder detectedEntities(String v) { this.detectedEntities = v; return this; }
        public Builder rootCauseHint(String v) { this.rootCauseHint = v; return this; }
        public Builder timestamp(LocalDateTime v) { this.timestamp = v; return this; }

        public LogAnalysis build() {
            LogAnalysis e = new LogAnalysis();
            e.id = this.id;
            e.applicationName = this.applicationName;
            e.logText = this.logText;
            e.severity = this.severity;
            e.sentiment = this.sentiment;
            e.confidenceScore = this.confidenceScore;
            e.keyPhrases = this.keyPhrases;
            e.detectedEntities = this.detectedEntities;
            e.rootCauseHint = this.rootCauseHint;
            e.timestamp = this.timestamp;
            return e;
        }
    }
}
