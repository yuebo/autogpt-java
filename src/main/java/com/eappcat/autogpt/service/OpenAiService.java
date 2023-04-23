package com.eappcat.autogpt.service;

import com.eappcat.autogpt.config.AutoGptConfigProperties;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OpenAiService {
    private final OpenAiClient openAiClient;
    private final AutoGptConfigProperties configProperties;


    @Retryable(include = Exception.class,maxAttempts = 5,backoff = @Backoff(delay = 5000))
    public List<BigDecimal> embeddings(String content) {
        return openAiClient.embeddings(content).getData().get(0).getEmbedding();
    }

    @Retryable(include = Exception.class,maxAttempts = 5,backoff = @Backoff(delay = 5000))
    public String chat(List<Message> messages, ChatCompletion.Model model){
        if (model == null){
            model = ChatCompletion.Model.GPT_3_5_TURBO;
        }

        int tokens = configProperties.getMaxToken()-calculateToken(messages,model.getName());
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).model(model.getName()).maxTokens(tokens).build();
        return openAiClient.chatCompletion(chatCompletion).getChoices().get(0).getMessage().getContent();
    }

    private Integer calculateToken(List<Message> messages,String model) {
        int total = 0;
        for (int i = 0; i < messages.size(); i++) {
            total += TikTokensUtil.tokens(model.toLowerCase(),messages.get(i).getContent());
        }
        return total;
    }
}
