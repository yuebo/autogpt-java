package com.eappcat.autogpt.models.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryPrompt implements PromptTemplate{

    private List<String> memories = new ArrayList<>();

    public String getMemoryDesc(){
        StringBuilder builder = new StringBuilder();
        if (this.memories!=null){
            for (int i = 0; i < this.memories.size(); i++) {
                builder.append(i + 1).append(". ").append(memories.get(i)).append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public String model() {
        return "memory";
    }
}
