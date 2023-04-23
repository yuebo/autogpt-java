package com.eappcat.autogpt.models.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryStorePrompt implements PromptTemplate{
    private String assistantReply;
    private String result;
    @Override
    public String model() {
        return "memory_store";
    }
}
