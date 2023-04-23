package com.eappcat.autogpt.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.eappcat.autogpt.models.memory.ComputedMemory;
import com.eappcat.autogpt.models.memory.Memory;
import com.eappcat.autogpt.service.FileStoreService;
import com.eappcat.autogpt.service.IMemoryService;
import com.eappcat.autogpt.service.OpenAiService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.eappcat.autogpt.utils.VectorUtil.cosineSimilarity;

/**
 * TODO 用ES和Embeddings查询持久化的内容
 */
@Service
@AllArgsConstructor
public class LongTermMemoryService implements IMemoryService {
    private final FileStoreService fileStoreService;
    private final OpenAiService openAiService;
    @Override
    public void addMemory(String taskId, String content) {

        Memory memory = new Memory();
        memory.setId(IdUtil.fastUUID());
        memory.setContent(content);
        memory.setCreateDate(new Date());
        memory.setVector(openAiService.embeddings(content));

        File file = fileStoreService.getWorkSpaceFile(taskId,"memory.txt");

        FileUtil.appendLines(Lists.newArrayList(JSONObject.toJSONString(memory)),file, StandardCharsets.UTF_8);

    }

    @Override
    public List<String> getMemoryBySize(String taskId,String textToSearch, int size) {

        File file = fileStoreService.getWorkSpaceFile(taskId,"memory.txt");
        if (!file.exists()){
            return Lists.newArrayList();
        }

        List<BigDecimal> searchVector = openAiService.embeddings(textToSearch);

        List<String> lines = FileUtil.readLines(file,StandardCharsets.UTF_8);

        List<ComputedMemory> memories = Lists.newArrayList();

        for (String line: lines){
            Memory memory = JSONObject.parseObject(line).toJavaObject(Memory.class);
            double similarity = cosineSimilarity(memory.getVector(),searchVector);

            memories.add(new ComputedMemory(memory,similarity));

        }

        List<String> result = memories.stream().filter(score-> score.getSimilarRate()>= 0.8).sorted(Comparator.comparingDouble(ComputedMemory::getSimilarRate).reversed()).map(data -> data.getMemory().getContent()).limit(size).collect(Collectors.toList());
        return result;
    }

    @Override
    public void clear(String taskId) {
        File file = fileStoreService.getWorkSpaceFile(taskId,"memory.txt");
        FileUtil.del(file);
    }



}
