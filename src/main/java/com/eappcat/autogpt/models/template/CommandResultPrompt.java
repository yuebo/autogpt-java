package com.eappcat.autogpt.models.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandResultPrompt implements PromptTemplate{
    private String command;
    private List data;
    @Override
    public String model() {
        return "command_return";
    }

    public String getCommandResultDesc(){
        return JSON.toJSONString(this.data);
    }
}
