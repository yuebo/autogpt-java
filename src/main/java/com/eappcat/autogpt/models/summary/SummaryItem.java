package com.eappcat.autogpt.models.summary;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SummaryItem {
    private String id;
    private String url;
    private String taskId;
    private List<BigDecimal> vector;
    private String content;
}
