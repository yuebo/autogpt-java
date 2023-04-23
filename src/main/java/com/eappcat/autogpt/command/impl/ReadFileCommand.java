package com.eappcat.autogpt.command.impl;

import cn.hutool.core.io.FileUtil;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.service.FileStoreService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class ReadFileCommand implements Command {
    private final FileStoreService fileStoreService;

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult commandResult = new CommandResult(this);
        String filename = context.getArgs().get("filename");
        File file = fileStoreService.getWorkSpaceFile(context.getTaskId(),filename);

        if (!file.exists()){
            commandResult.setData(Lists.newArrayList());
            return commandResult;
        }
        String text = FileUtil.readString(file, StandardCharsets.UTF_8);
        commandResult.setData(Lists.newArrayList(text));

        return commandResult;
    }

    @Override
    public boolean accept(String cmd) {
        return "read_file".equals(cmd);
    }
}
