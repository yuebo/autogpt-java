package com.eappcat.autogpt.service;

import com.eappcat.autogpt.config.AutoGptConfigProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
public class FileStoreService {
    private final AutoGptConfigProperties autoGptConfigProperties;
    public File getWorkSpace(String id){
        File workspace = new File(autoGptConfigProperties.getFileRoot(),id);
        if (!workspace.exists()){
            workspace.mkdirs();
        }
        return workspace;
    }
    public File getWorkSpaceFile(String id,String file){
        return new File(getWorkSpace(id), StringUtils.stripStart(file,"/\\"));
    }
}
