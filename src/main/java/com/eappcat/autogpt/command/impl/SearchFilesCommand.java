package com.eappcat.autogpt.command.impl;

import cn.hutool.core.io.FileUtil;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.service.FileStoreService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class SearchFilesCommand implements Command {
    private final FileStoreService fileStoreService;

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult result = new CommandResult(this);
        String dir = context.getArgs().get("directory");
        File relative = null;

        File workspace = fileStoreService.getWorkSpace(context.getTaskId());
        if (StringUtils.isEmpty(dir)||StringUtils.equals(dir,"/")){
            log.debug("使用目录：{}",workspace.getAbsolutePath());
            relative = workspace;
        }else {
            relative = fileStoreService.getWorkSpaceFile(context.getTaskId(),dir);
        }

        if (!relative.isFile()){
            List<String> filenames = Lists.newArrayList();
            FileUtil.walkFiles(relative, file -> {
                if (file.isFile()){
                    filenames.add(StringUtils.replaceOnce(file.getAbsolutePath(),workspace.getAbsolutePath(),""));
                }
            });

            result.setData(filenames);
        }

        return result;
    }

    @Override
    public boolean accept(String cmd) {
        return "search_files".equals(cmd);
    }
}
