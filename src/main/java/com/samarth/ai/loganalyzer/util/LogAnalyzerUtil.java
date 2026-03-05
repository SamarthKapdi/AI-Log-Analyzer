package com.samarth.ai.loganalyzer.util;

public class LogAnalyzerUtil {

    /**
     * Determine severity based on keywords in the log text.
     */
    public static String classifySeverity(String logText) {
        if (logText == null)
            return "INFO";
        String lowerLog = logText.toLowerCase();

        if (lowerLog.contains("exception") || lowerLog.contains("error") || lowerLog.contains("failure")
                || lowerLog.contains("fatal")) {
            return "ERROR";
        } else if (lowerLog.contains("warn") || lowerLog.contains("warning")) {
            return "WARNING";
        } else {
            return "INFO";
        }
    }

    /**
     * Generate a root cause hint based on common patterns.
     */
    public static String generateRootCauseHint(String logText) {
        if (logText == null)
            return "No log content provided.";

        String lowerLog = logText.toLowerCase();

        if (lowerLog.contains("nullpointerexception")) {
            return "Possible uninitialized object reference. Check for null values before accessing object properties.";
        } else if (lowerLog.contains("timeout") || lowerLog.contains("timed out")) {
            return "Check network connectivity, firewall settings, or external service availability.";
        } else if (lowerLog.contains("sql") || lowerLog.contains("database")) {
            return "Possible database connectivity issue or invalid query. Check DB status and credentials.";
        } else if (lowerLog.contains("memory") || lowerLog.contains("outofmemory")) {
            return "Application is running out of memory. Check for memory leaks or increase heap size.";
        } else if (lowerLog.contains("accessdenied") || lowerLog.contains("security")) {
            return "Security violation. Check user permissions and authentication tokens.";
        }

        return "Manual review recommended. Analyze stack trace for more details.";
    }
}
