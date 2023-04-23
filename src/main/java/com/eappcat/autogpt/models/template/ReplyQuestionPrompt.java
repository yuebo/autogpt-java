package com.eappcat.autogpt.models.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyQuestionPrompt implements PromptTemplate{
    private String content;
    private String question;

    @Override
    public String model() {
        return "reply_question";
    }
}
