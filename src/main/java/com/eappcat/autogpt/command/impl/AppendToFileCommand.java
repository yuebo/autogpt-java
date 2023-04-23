package com.eappcat.autogpt.command.impl;

import cn.hutool.core.io.FileUtil;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.service.FileStoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class AppendToFileCommand implements Command {
    private final FileStoreService fileStoreService;

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult result = new CommandResult(this);
        String filename = context.getArgs().get("filename");
        String text = context.getArgs().get("text");

        File fileToWrite = fileStoreService.getWorkSpaceFile(context.getTaskId(),filename);

        FileUtil.appendString(text, fileToWrite, StandardCharsets.UTF_8);

        return result;
    }

    @Override
    public boolean accept(String cmd) {
        return "append_to_file".equals(cmd);
    }
}
