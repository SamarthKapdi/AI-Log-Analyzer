package com.samarth.ai.loganalyzer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LogAnalyzerUtil}.
 * <p>
 * These are pure unit tests — no Spring context is loaded, no DB, no OCI.
 * They run as part of {@code mvn test} and confirm the rule-engine logic
 * in isolation.
 * </p>
 */
class LogAnalyzerUtilTest {

    // ---------------------------------------------------------------
    // classifySeverity
    // ---------------------------------------------------------------

    @Test
    @DisplayName("NullPointerException in log → severity ERROR")
    void classifySeverity_nullPointerException_returnsError() {
        String log = "java.lang.NullPointerException: Cannot invoke method on null";
        assertEquals("ERROR", LogAnalyzerUtil.classifySeverity(log));
    }

    @Test
    @DisplayName("'error' keyword in log → severity ERROR")
    void classifySeverity_errorKeyword_returnsError() {
        assertEquals("ERROR", LogAnalyzerUtil.classifySeverity("Application error during startup"));
    }

    @Test
    @DisplayName("'failure' keyword in log → severity ERROR")
    void classifySeverity_failureKeyword_returnsError() {
        assertEquals("ERROR", LogAnalyzerUtil.classifySeverity("Connection failure on port 5432"));
    }

    @Test
    @DisplayName("'fatal' keyword in log → severity ERROR")
    void classifySeverity_fatalKeyword_returnsError() {
        assertEquals("ERROR", LogAnalyzerUtil.classifySeverity("FATAL: JVM terminated unexpectedly"));
    }

    @Test
    @DisplayName("'Warning detected' in log → severity WARNING")
    void classifySeverity_warnKeyword_returnsWarning() {
        assertEquals("WARNING", LogAnalyzerUtil.classifySeverity("Warning detected in configuration"));
    }

    @Test
    @DisplayName("'warn' keyword in log → severity WARNING")
    void classifySeverity_warningKeyword_returnsWarning() {
        assertEquals("WARNING", LogAnalyzerUtil.classifySeverity("Low disk space warn"));
    }

    @Test
    @DisplayName("Informational message → severity INFO")
    void classifySeverity_plainInfo_returnsInfo() {
        assertEquals("INFO", LogAnalyzerUtil.classifySeverity("Info log: application started successfully"));
    }

    @Test
    @DisplayName("Null input → severity INFO (no NPE)")
    void classifySeverity_nullInput_returnsInfo() {
        assertEquals("INFO", LogAnalyzerUtil.classifySeverity(null));
    }

    @Test
    @DisplayName("Empty string → severity INFO")
    void classifySeverity_emptyInput_returnsInfo() {
        assertEquals("INFO", LogAnalyzerUtil.classifySeverity(""));
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" → {1}")
    @DisplayName("Parameterised severity classification table")
    @CsvSource({
        "NullPointerException thrown at line 42, ERROR",
        "Warning detected in retry logic,         WARNING",
        "Info log: heartbeat ok,                  INFO",
        "Database failure during INSERT,           ERROR",
        "Disk usage warning at 85%,               WARNING",
        "User login successful,                    INFO"
    })
    void classifySeverity_parameterised(String input, String expected) {
        assertEquals(expected, LogAnalyzerUtil.classifySeverity(input.trim()));
    }

    // ---------------------------------------------------------------
    // generateRootCauseHint
    // ---------------------------------------------------------------

    @Test
    @DisplayName("NullPointerException → hint mentions null reference check")
    void generateRootCauseHint_nullPointer_mentionsNullCheck() {
        String hint = LogAnalyzerUtil.generateRootCauseHint("NullPointerException at UserService.java:42");
        assertTrue(hint.toLowerCase().contains("null"),
                "Expected hint to mention null: " + hint);
    }

    @Test
    @DisplayName("Timeout → hint mentions network / firewall")
    void generateRootCauseHint_timeout_mentionsNetwork() {
        String hint = LogAnalyzerUtil.generateRootCauseHint("Connection timed out after 30s");
        assertTrue(hint.toLowerCase().contains("network") || hint.toLowerCase().contains("firewall"),
                "Expected hint to mention network or firewall: " + hint);
    }

    @Test
    @DisplayName("SQL error → hint mentions database")
    void generateRootCauseHint_sqlError_mentionsDatabase() {
        String hint = LogAnalyzerUtil.generateRootCauseHint("SQL exception: ORA-00942 table not found");
        assertTrue(hint.toLowerCase().contains("database") || hint.toLowerCase().contains("db"),
                "Expected hint to mention database: " + hint);
    }

    @Test
    @DisplayName("Unknown error → returns manual review recommendation")
    void generateRootCauseHint_unknown_returnsDefaultRecommendation() {
        String hint = LogAnalyzerUtil.generateRootCauseHint("Some completely unknown message xyz");
        assertNotNull(hint);
        assertFalse(hint.isBlank());
    }

    @Test
    @DisplayName("Null input → returns safe default message (no NPE)")
    void generateRootCauseHint_nullInput_returnsSafeDefault() {
        String hint = LogAnalyzerUtil.generateRootCauseHint(null);
        assertNotNull(hint);
        assertFalse(hint.isBlank());
    }
}
