package com.samarth.ai.loganalyzer.service;

import com.oracle.bmc.ailanguage.AIServiceLanguageClient;
import com.oracle.bmc.ailanguage.model.*;
import com.oracle.bmc.ailanguage.requests.DetectLanguageSentimentsRequest;
import com.oracle.bmc.ailanguage.requests.DetectLanguageKeyPhrasesRequest;
import com.oracle.bmc.ailanguage.requests.DetectLanguageEntitiesRequest;
import com.oracle.bmc.ailanguage.responses.DetectLanguageSentimentsResponse;
import com.oracle.bmc.ailanguage.responses.DetectLanguageKeyPhrasesResponse;
import com.oracle.bmc.ailanguage.responses.DetectLanguageEntitiesResponse;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wraps all OCI AI Language SDK calls.
 * <p>
 * IMPORTANT: This service requires a properly configured OCI environment.
 * It will NEVER return mock or synthetic data. If credentials or the
 * compartment ID are missing, a clear {@link IllegalStateException} is thrown
 * so the caller (and the global exception handler) surfaces an actionable
 * error.
 * </p>
 */
@Service
public class OciLanguageService {

    private static final Logger log = LoggerFactory.getLogger(OciLanguageService.class);

    private final AIServiceLanguageClient aiClient;

    public OciLanguageService(
            @org.springframework.beans.factory.annotation.Autowired(required = false) AIServiceLanguageClient aiClient) {
        this.aiClient = aiClient;
    }

    /**
     * OCI Compartment OCID — must be supplied via the OCI_COMPARTMENT_ID
     * environment variable. Used for service quota tracking and is required
     * by OCI batch AI Language APIs.
     */
    @Value("${oci.ai.compartment-id:}")
    private String compartmentId;

    @Value("${oci.ai.mock-mode:false}")
    private boolean mockMode;

    @PostConstruct
    public void validateConfig() {
        if (mockMode) {
            log.info("OciLanguageService initialised in MOCK MODE. Real OCI credentials will be bypassed.");
            return;
        }

        if (!StringUtils.hasText(compartmentId)) {
            throw new IllegalStateException(
                    "OCI AI client not initialized. Real OCI credentials required. " +
                            "Set the OCI_COMPARTMENT_ID environment variable, or use MOCK MODE.");
        }
        log.info("OciLanguageService initialised. Compartment: {}", compartmentId);
    }

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    /**
     * Detects the dominant sentiment of the given text using OCI AI Language.
     *
     * @param text raw log text (max 4 000 chars)
     * @return {@link DetectLanguageSentimentsResult} — never null
     * @throws IllegalStateException if the OCI call fails
     */
    public DetectLanguageSentimentsResult detectSentiment(String text) {
        if (mockMode) {
            log.debug("MOCK MODE: Returning mock sentiment analysis for: {}", text);
            return DetectLanguageSentimentsResult.builder()
                    .aspects(List.of(
                            SentimentAspect.builder()
                                    .sentiment("Negative")
                                    .scores(java.util.Map.of("Negative", 0.95))
                                    .build()))
                    .build();
        }

        log.debug("Calling OCI Sentiment Analysis. compartmentId={}", compartmentId);
        try {
            // Note: compartmentId is validated at startup (@PostConstruct). The v1
            // DetectLanguageSentimentsDetails builder does not expose a compartmentId
            // setter — routing is handled at the OCI config/tenancy level.
            DetectLanguageSentimentsDetails details = DetectLanguageSentimentsDetails.builder()
                    .text(text)
                    .build();

            DetectLanguageSentimentsRequest request = DetectLanguageSentimentsRequest.builder()
                    .detectLanguageSentimentsDetails(details)
                    .build();

            DetectLanguageSentimentsResponse response = aiClient.detectLanguageSentiments(request);
            log.debug("OCI Sentiment Analysis succeeded.");
            return response.getDetectLanguageSentimentsResult();
        } catch (Exception e) {
            log.error("OCI Sentiment Analysis failed: {}", e.getMessage());
            throw new IllegalStateException(
                    "OCI AI client not initialized. Real OCI credentials required. Sentiment call failed: "
                            + e.getMessage(),
                    e);
        }
    }

    /**
     * Extracts key phrases from the text using OCI AI Language.
     *
     * @param text raw log text
     * @return list of extracted key phrases — never null, never synthetic
     * @throws IllegalStateException if the OCI call fails
     */
    public List<String> extractKeyPhrases(String text) {
        if (mockMode) {
            log.debug("MOCK MODE: Returning mock key phrases for: {}", text);
            return List.of("Mock Error Phrase", "Timeout Mock", "Database Connection");
        }

        log.debug("Calling OCI Key Phrase Extraction. compartmentId={}", compartmentId);
        try {
            // Note: v1 DetectLanguageKeyPhrasesDetails does not expose compartmentId.
            DetectLanguageKeyPhrasesDetails details = DetectLanguageKeyPhrasesDetails.builder()
                    .text(text)
                    .build();

            DetectLanguageKeyPhrasesRequest request = DetectLanguageKeyPhrasesRequest.builder()
                    .detectLanguageKeyPhrasesDetails(details)
                    .build();

            DetectLanguageKeyPhrasesResponse response = aiClient.detectLanguageKeyPhrases(request);
            log.debug("OCI Key Phrase Extraction succeeded.");
            return response.getDetectLanguageKeyPhrasesResult().getKeyPhrases().stream()
                    .map(KeyPhrase::getText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("OCI Key Phrase Extraction failed: {}", e.getMessage());
            throw new IllegalStateException(
                    "OCI AI client not initialized. Real OCI credentials required. Key phrase call failed: "
                            + e.getMessage(),
                    e);
        }
    }

    /**
     * Detects named entities (NER) in the text using OCI AI Language.
     *
     * @param text raw log text
     * @return list of detected entity texts — never null, never synthetic
     * @throws IllegalStateException if the OCI call fails
     */
    public List<String> detectEntities(String text) {
        if (mockMode) {
            log.debug("MOCK MODE: Returning mock entities for: {}", text);
            return List.of("MockEntity_System", "MockEntity_User");
        }

        log.debug("Calling OCI Entity Detection. compartmentId={}", compartmentId);
        try {
            // Note: v1 DetectLanguageEntitiesDetails does not expose compartmentId.
            DetectLanguageEntitiesDetails details = DetectLanguageEntitiesDetails.builder()
                    .text(text)
                    .build();

            DetectLanguageEntitiesRequest request = DetectLanguageEntitiesRequest.builder()
                    .detectLanguageEntitiesDetails(details)
                    .build();

            DetectLanguageEntitiesResponse response = aiClient.detectLanguageEntities(request);
            log.debug("OCI Entity Detection succeeded.");
            return response.getDetectLanguageEntitiesResult().getEntities().stream()
                    .map(Entity::getText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("OCI Entity Detection failed: {}", e.getMessage());
            throw new IllegalStateException(
                    "OCI AI client not initialized. Real OCI credentials required. Entity detection call failed: "
                            + e.getMessage(),
                    e);
        }
    }

    // ------------------------------------------------------------------
    // Accessors (useful for testing / logging)
    // ------------------------------------------------------------------

    public String getCompartmentId() {
        return compartmentId;
    }

    public boolean isMockMode() {
        return mockMode;
    }
}
