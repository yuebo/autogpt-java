package com.eappcat.autogpt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "chatgpt")
@Component
@Data
public class AutoGptConfigProperties {
    private String apiKey;
    private String url;
    private String model = "gpt-3.5-turbo";
    private int maxToken = 4000;

    private String fileRoot="/opt/autogpt";


}
