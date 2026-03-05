package com.samarth.ai.loganalyzer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration.
 * <p>
 * Allowed origins are controlled via the {@code CORS_ALLOWED_ORIGINS} environment
 * variable (comma-separated). This replaces the wildcard {@code @CrossOrigin(origins="*")}
 * annotation that was previously on the controller, ensuring that only explicitly
 * whitelisted origins can call the API.
 * </p>
 *
 * Example:
 * <pre>
 *   export CORS_ALLOWED_ORIGINS=https://myapp.example.com,https://demo.oracle.com
 * </pre>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String allowedOriginsRaw;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String[] origins = allowedOriginsRaw != null
                ? allowedOriginsRaw.split(",")
                : new String[]{"http://localhost:3000", "http://localhost:8080"};
        log.info("CORS configured. Allowed origins: {}", allowedOriginsRaw);

        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
