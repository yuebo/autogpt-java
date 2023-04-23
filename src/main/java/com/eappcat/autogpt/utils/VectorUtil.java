package com.eappcat.autogpt.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class VectorUtil {
    /**
     * 计算余弦相似度
     * @param data1
     * @param data2
     * @return
     */
    public static double cosineSimilarity(List<BigDecimal> data1, List<BigDecimal> data2) {
        List<Double> vectorA = data1.stream().map(BigDecimal::doubleValue).collect(Collectors.toList());
        List<Double> vectorB = data2.stream().map(BigDecimal::doubleValue).collect(Collectors.toList());
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}
