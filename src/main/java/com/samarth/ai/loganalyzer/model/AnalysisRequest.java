package com.samarth.ai.loganalyzer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnalysisRequest {

    @NotBlank(message = "Application name is required")
    private String applicationName;

    @NotBlank(message = "Log text cannot be empty")
    @Size(max = 4000, message = "Log text must be under 4000 characters")
    private String logText;

    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }

    public String getLogText() { return logText; }
    public void setLogText(String logText) { this.logText = logText; }
}
