package com.eappcat.autogpt.models.template;

import lombok.Data;

import java.util.List;

@Data
public class MainPrompt implements PromptTemplate {
    private String aiName;
    private String role;
    private List<String> goals;
    private List<String> cmds;

    public String getGoalsDesc(){
        StringBuilder builder = new StringBuilder();
        if (this.goals!=null) {
            for (int i = 0; i < this.goals.size(); i++) {
                builder.append(i + 1).append(". ").append(goals.get(i)).append("\n");
            }
        }
        return builder.toString();
    }

    public String getCmdsDesc(){
        StringBuilder builder = new StringBuilder();
        if (this.cmds!=null){
            for (int i = 0; i < this.cmds.size(); i++) {
                builder.append(i + 1).append(". ").append(cmds.get(i)).append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public String model() {
        return "main";
    }
}
