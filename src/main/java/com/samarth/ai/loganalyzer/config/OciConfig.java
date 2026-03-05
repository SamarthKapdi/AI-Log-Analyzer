package com.samarth.ai.loganalyzer.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.ailanguage.AIServiceLanguageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OciConfig {

        private static final Logger log = LoggerFactory.getLogger(OciConfig.class);

        @Value("${oci.config.file-path}")
        private String configFilePath;

        @Value("${oci.config.profile}")
        private String profile;

        @Value("${oci.ai.mock-mode:false}")
        private boolean mockMode;

        @Bean
        public AIServiceLanguageClient aiServiceLanguageClient() {
                if (mockMode) {
                        log.warn("Running in MOCK MODE! Bypass OCI Config File Loading...");
                        return null; // The service handles null client in mock mode
                }

                try {
                        // Load config from file
                        ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configFilePath, profile);
                        ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(
                                        configFile);

                        AIServiceLanguageClient client = AIServiceLanguageClient.builder()
                                        .build(provider);

                        log.info("OCI AI Language client initialised successfully. Profile='{}', ConfigFile='{}'",
                                        profile, configFilePath);
                        return client;
                } catch (java.io.IOException e) {
                        log.error("Failed to initialize OCI AI Language client. " +
                                        "Verify ~/.oci/config exists and OCI_COMPARTMENT_ID is set. Error: {}",
                                        e.getMessage());
                        throw new IllegalStateException(
                                        "OCI AI client could not be initialized: " + e.getMessage(), e);
                }
        }
}
