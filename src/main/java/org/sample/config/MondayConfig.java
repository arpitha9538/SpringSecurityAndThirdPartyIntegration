package org.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "monday")
public class MondayConfig {

    @Value("${monday.apikey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}

