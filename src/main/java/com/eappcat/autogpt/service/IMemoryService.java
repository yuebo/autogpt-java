package com.eappcat.autogpt.service;

import java.util.List;

/**
 * 短期记忆实现类
 */
public interface IMemoryService {

    /**
     * 添加记忆内容
     * @param taskId
     * @param content
     */
    void addMemory(String taskId,String content);

    /**
     * 获取最新的几条记忆内容
     * @param taskId
     * @param size
     * @return
     */
    List<String> getMemoryBySize(String taskId, String textToSearch ,int size);

    void clear(String taskId);
}
