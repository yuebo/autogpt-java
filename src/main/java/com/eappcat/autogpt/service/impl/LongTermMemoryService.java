package com.eappcat.autogpt.service.impl;

import com.eappcat.autogpt.service.IMemoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 用ES和Embeddings查询持久化的内容
 */
@Service
public class LongTermMemoryService implements IMemoryService {
    @Override
    public void addMemory(String taskId, String content) {

    }

    @Override
    public List<String> getMemoryBySize(String taskId,String textToSearch, int size) {
        return new ArrayList<>();
    }
}
