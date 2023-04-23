package com.eappcat.autogpt.command;

import com.eappcat.autogpt.enums.PromptTemplateLang;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ExecuteContext {
    private Map<String,String> args=new HashMap<>();
    private String taskId;
    private PromptTemplateLang lang;
}
