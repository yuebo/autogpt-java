package com.eappcat.autogpt.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommandResult {
    private List data;

    private Command command;

    public CommandResult(Command cmd){
        this.command = cmd;
    }
}
