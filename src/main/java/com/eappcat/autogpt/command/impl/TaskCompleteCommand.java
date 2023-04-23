package com.eappcat.autogpt.command.impl;

import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteCommand implements Command {
    @Override
    public boolean accept(String cmd) {
        return "do_nothing".equals(cmd);
    }

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult result = new CommandResult(this);
        return result;
    }
}
