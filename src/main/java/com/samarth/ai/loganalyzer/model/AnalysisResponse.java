package com.samarth.ai.loganalyzer.model;

import java.time.LocalDateTime;
import java.util.List;

public class AnalysisResponse {

    private String applicationName;
    private String detectedSentiment;
    private String severityClassification;
    private Double confidenceScore;
    private List<String> keyPhrases;
    private List<String> detectedEntities;
    private String possibleRootCauseHint;
    private LocalDateTime timestamp;
    /** false in all normal flows; true only when running under the 'test' Spring profile. */
    private boolean mockMode;

    // --- Getters ---
    public String getApplicationName() { return applicationName; }
    public String getDetectedSentiment() { return detectedSentiment; }
    public String getSeverityClassification() { return severityClassification; }
    public Double getConfidenceScore() { return confidenceScore; }
    public List<String> getKeyPhrases() { return keyPhrases; }
    public List<String> getDetectedEntities() { return detectedEntities; }
    public String getPossibleRootCauseHint() { return possibleRootCauseHint; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isMockMode() { return mockMode; }

    // --- Setters ---
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
    public void setDetectedSentiment(String detectedSentiment) { this.detectedSentiment = detectedSentiment; }
    public void setSeverityClassification(String severityClassification) { this.severityClassification = severityClassification; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setKeyPhrases(List<String> keyPhrases) { this.keyPhrases = keyPhrases; }
    public void setDetectedEntities(List<String> detectedEntities) { this.detectedEntities = detectedEntities; }
    public void setPossibleRootCauseHint(String possibleRootCauseHint) { this.possibleRootCauseHint = possibleRootCauseHint; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setMockMode(boolean mockMode) { this.mockMode = mockMode; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String applicationName;
        private String detectedSentiment;
        private String severityClassification;
        private Double confidenceScore;
        private List<String> keyPhrases;
        private List<String> detectedEntities;
        private String possibleRootCauseHint;
        private LocalDateTime timestamp;
        private boolean mockMode;

        public Builder applicationName(String v) { this.applicationName = v; return this; }
        public Builder detectedSentiment(String v) { this.detectedSentiment = v; return this; }
        public Builder severityClassification(String v) { this.severityClassification = v; return this; }
        public Builder confidenceScore(Double v) { this.confidenceScore = v; return this; }
        public Builder keyPhrases(List<String> v) { this.keyPhrases = v; return this; }
        public Builder detectedEntities(List<String> v) { this.detectedEntities = v; return this; }
        public Builder possibleRootCauseHint(String v) { this.possibleRootCauseHint = v; return this; }
        public Builder timestamp(LocalDateTime v) { this.timestamp = v; return this; }
        public Builder mockMode(boolean v) { this.mockMode = v; return this; }

        public AnalysisResponse build() {
            AnalysisResponse r = new AnalysisResponse();
            r.applicationName = this.applicationName;
            r.detectedSentiment = this.detectedSentiment;
            r.severityClassification = this.severityClassification;
            r.confidenceScore = this.confidenceScore;
            r.keyPhrases = this.keyPhrases;
            r.detectedEntities = this.detectedEntities;
            r.possibleRootCauseHint = this.possibleRootCauseHint;
            r.timestamp = this.timestamp;
            r.mockMode = this.mockMode;
            return r;
        }
    }
}
