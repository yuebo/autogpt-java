package com.eappcat.autogpt.config;

import com.unfbx.chatgpt.OpenAiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAiClientConfiguration {

    @Bean
    public OpenAiClient openAiClient(OpenAiConfigProperties configProperties){
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList(configProperties.getApiKey()))
                .apiHost(configProperties.getUrl().concat("/"))
                .build();

        return openAiClient;
    }
}
