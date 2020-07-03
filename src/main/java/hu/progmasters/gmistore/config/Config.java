package hu.progmasters.gmistore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class Config {

    @Value("${cloudinary.cloudName}")
    private String cloudinaryCloudName;

    @Value("${cloudinary.apiKey}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.apiSecret}")
    private String cloudinaryApiSecret;

    @Bean
    public String cloudinaryCloudName() {
        return cloudinaryCloudName;
    }

    @Bean
    public String cloudinaryApiKey() {
        return cloudinaryApiKey;
    }

    @Bean
    public String cloudinaryApiSecret() {
        return cloudinaryApiSecret;
    }
}
