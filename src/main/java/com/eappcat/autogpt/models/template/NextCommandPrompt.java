package com.eappcat.autogpt.models.template;

import lombok.Data;

@Data
public class NextCommandPrompt implements PromptTemplate{
    @Override
    public String model() {
        return "next_command";
    }
}
