package com.eappcat.autogpt.command.impl;

import cn.hutool.core.util.URLUtil;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.models.template.ReplyQuestionPrompt;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.service.PromptTemplateService;
import com.google.common.collect.Lists;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.eappcat.autogpt.utils.VectorUtil.cosineSimilarity;

@AllArgsConstructor
@Slf4j
public abstract class AbstractBrowseSiteCommand implements Command {
    protected final OpenAiService openAiService;
    protected final PromptTemplateService templateService;

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult commandResult = new CommandResult(this);
        String question = context.getArgs().get("question");

        String url = processUrl(context, question);

        try {
            String body = scrapeText(url);
            log.info("抓取内容大小, {}", body.length());
            List<String> pp = generateParagraph(body);
            List<String> summaryList = generateSummary(context, question, pp);
            commandResult.setData(summaryList);
        }catch (Exception e){
            commandResult.setData(Lists.newArrayList("Error :",e.getMessage()));
            return commandResult;
        }

        return commandResult;
    }

    @NotNull
    private List<String> generateSummary(ExecuteContext context, String question, List<String> pp) {
        List<String> summaryList = Lists.newArrayList();

        log.info("数据为大小为{},{}", pp.size(), pp);

        List<BigDecimal> questionVector = openAiService.embeddings(question);

        for (String p : pp){
            List<BigDecimal> vector = openAiService.embeddings(p);
            if (cosineSimilarity(vector,questionVector)>0.8){
                String template = templateService.parse(new ReplyQuestionPrompt(p, question), context.getLang());
                Message message = Message.builder().role(Message.Role.USER).content(template).build();
                String result = openAiService.chat(Lists.newArrayList(message),null);
                summaryList.add(result);
            }
        }
        log.info("生成摘要：{}", summaryList);
        return summaryList;
    }

    @NotNull
    private static List<String> generateParagraph(String body) {
        //分段内容，后续方便生成摘要;
        int startCount = 1900;
        int index = 0;
        String splitCharacters= ".?!;。？！；";
        StringBuilder stringBuilder = new StringBuilder();
        List<String> pp =new ArrayList<>();
        for (char c: body.toCharArray()){
            if (index++<startCount){
                stringBuilder.append(c);
            }else {
                if (splitCharacters.contains(String.format("%s", c))){
                    stringBuilder.append(c);
                    pp.add(stringBuilder.toString());
                    stringBuilder.delete(0,stringBuilder.length());
                    index = 0;
                }
            }
        }
        if (stringBuilder.length()>0){
            pp.add(stringBuilder.toString());
            stringBuilder.delete(0,stringBuilder.length());
        }
        return pp;
    }

    @NotNull
    protected abstract String scrapeText(String url) throws Exception;

    @NotNull
    private static String processUrl(ExecuteContext context, String question) {
        String url = StringUtils.replaceOnce(context.getArgs().get("url"),"https://www.google.com",System.getenv().getOrDefault("GOOGLE_MIRROR_URL","https://www.google.com"));
        if (url.equals("https://www.baidu.com/")){
            url = url + "s?wd="+ URLUtil.encode(question);
        }
        return url;
    }

}
