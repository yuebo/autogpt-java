package com.eappcat.autogpt.service;

import com.eappcat.autogpt.models.summary.SummaryItem;
import com.eappcat.autogpt.models.summary.SummaryItemQuery;

import java.util.List;

/**
 * 长期记忆实现类
 */
public interface SummaryService {
    /**
     * 保存内容
     * @param item
     */
    void saveSummaryItem(SummaryItem item);
    /**
     * 加载内容
     * @param query
     */
    List<SummaryItem> list(SummaryItemQuery query);
    /**
     * 清除内容
     * @param taskId
     */
    void clear(String taskId);
}
