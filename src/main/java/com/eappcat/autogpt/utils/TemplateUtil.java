package com.eappcat.autogpt.utils;

import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author owen.xia
 * date 2021/10/11 15:29
 */
public class TemplateUtil {

    /***
     * 转换字符串
     *
     * @param content 转换内容
     * @param parameter 参数Map
     * @return 结果
     */
    public static String render(String content, Object parameter) {
        SpelExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext context = new TemplateParserContext();
        return parser.parseExpression(content, context).getValue(parameter, String.class);
    }
    public static String render(InputStream inputStream, Object parameter) throws Exception{
        try(InputStream in = inputStream){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine())!=null){
                builder.append(line.contains("\n"));
            }
            return render(builder.toString(),parameter);
        }
    }
}
