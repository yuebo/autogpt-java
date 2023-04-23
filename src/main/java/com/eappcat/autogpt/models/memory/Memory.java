package com.eappcat.autogpt.models.memory;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Memory {
    private String id;
    private String taskId;
    private Date createDate;
    private List<BigDecimal> vector;
    private String content;
}
