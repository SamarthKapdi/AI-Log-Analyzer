package com.samarth.ai.loganalyzer.service;

import com.oracle.bmc.ailanguage.model.DetectLanguageSentimentsResult;
import com.oracle.bmc.ailanguage.model.SentimentAspect;
import com.samarth.ai.loganalyzer.model.AnalysisRequest;
import com.samarth.ai.loganalyzer.model.AnalysisResponse;
import com.samarth.ai.loganalyzer.model.LogAnalysis;
import com.samarth.ai.loganalyzer.repository.LogAnalysisRepository;
import com.samarth.ai.loganalyzer.util.LogAnalyzerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(LogAnalysisService.class);

    private final OciLanguageService ociLanguageService;
    private final LogAnalysisRepository repository;

    public LogAnalysisService(OciLanguageService ociLanguageService, LogAnalysisRepository repository) {
        this.ociLanguageService = ociLanguageService;
        this.repository = repository;
    }

    public AnalysisResponse analyzeLog(AnalysisRequest request) {
        log.info("Analyzing log for application: {}", request.getApplicationName());

        // 1. Call OCI AI Language Services
        // - Sentiment Analysis
        DetectLanguageSentimentsResult sentimentResult = ociLanguageService.detectSentiment(request.getLogText());

        String detectedSentiment = "Unknown";
        Double confidenceScore = 0.0;

        if (sentimentResult != null && !sentimentResult.getAspects().isEmpty()) {
            // Take the most prominent sentiment
            SentimentAspect mainAspect = sentimentResult.getAspects().get(0);
            detectedSentiment = mainAspect.getSentiment();
            // Scores are map of "Positive": 0.9, etc. We can take the score for the
            // detected sentiment.
            if (mainAspect.getScores() != null && mainAspect.getScores().containsKey(detectedSentiment)) {
                confidenceScore = mainAspect.getScores().get(detectedSentiment);
            }
        }

        // - Key Phrase Extraction
        List<String> keyPhrases = ociLanguageService.extractKeyPhrases(request.getLogText());

        // - Entity Detection
        List<String> entities = ociLanguageService.detectEntities(request.getLogText());

        // 2. Custom Business Logic
        String severity = LogAnalyzerUtil.classifySeverity(request.getLogText());
        String rootCause = LogAnalyzerUtil.generateRootCauseHint(request.getLogText());

        // 3. Persist specific results to DB
        LogAnalysis entity = LogAnalysis.builder()
                .applicationName(request.getApplicationName())
                .logText(request.getLogText())
                .severity(severity)
                .sentiment(detectedSentiment)
                .confidenceScore(confidenceScore)
                .keyPhrases(String.join(", ", keyPhrases))
                .detectedEntities(String.join(", ", entities))
                .rootCauseHint(rootCause)
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(entity);

        // 4. Return Response
        return AnalysisResponse.builder()
                .applicationName(request.getApplicationName())
                .detectedSentiment(detectedSentiment)
                .severityClassification(severity)
                .confidenceScore(confidenceScore)
                .keyPhrases(keyPhrases)
                .detectedEntities(entities)
                .possibleRootCauseHint(rootCause)
                .timestamp(entity.getTimestamp())
                .mockMode(ociLanguageService.isMockMode())
                .build();
    }

    public List<LogAnalysis> getAllLogs() {
        return repository.findAll();
    }
}
