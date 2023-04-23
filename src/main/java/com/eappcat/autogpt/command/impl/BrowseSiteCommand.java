package com.eappcat.autogpt.command.impl;

import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.models.template.ReplyQuestionPrompt;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.service.PromptTemplateService;
import com.google.common.collect.Lists;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.eappcat.autogpt.utils.VectorUtil.cosineSimilarity;


@Component
@Data
@Slf4j
@AllArgsConstructor
public class BrowseSiteCommand implements Command {
    private final OpenAiService openAiService;
    private final PromptTemplateService templateService;
    @Override
    public boolean accept(String cmd) {
        return "browse_website".equals(cmd);
    }

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception {
        CommandResult commandResult = new CommandResult(this);
        String url = StringUtils.replaceOnce(context.getArgs().get("url"),"https://www.google.com",System.getenv().getOrDefault("GOOGLE_MIRROR_URL","https://www.google.com"));
        String question = context.getArgs().get("question");

        try {
            Document document = Jsoup.connect(url).get();
            String body = document.body().text();

            log.info("抓取内容大小, {}", body.length());

            //split content to small size;
            int startCount = 1900;
            int index = 0;
            String splitCharacters= ".?!;。？！；";
            StringBuilder stringBuilder = new StringBuilder();
            List<String> pp =new ArrayList<>();
            for (char c:body.toCharArray()){
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


            List<String> summaryList = Lists.newArrayList();
            log.info("数据为大小为{},{}",pp.size(),pp);

            List<BigDecimal> questionVector = openAiService.embeddings(question);

            for (String p : pp){
                List<BigDecimal> vector = openAiService.embeddings(p);
                if (cosineSimilarity(vector,questionVector)>0.8){
                    String template = templateService.parse(new ReplyQuestionPrompt(p,question),context.getLang());
                    Message message = Message.builder().role(Message.Role.USER).content(template).build();
                    String result = openAiService.chat(Lists.newArrayList(message),null);
                    summaryList.add(result);
                }
            }
            log.info("生成摘要：{}", summaryList);
            commandResult.setData(summaryList);
        }catch (Exception e){
            commandResult.setData(Lists.newArrayList("Error :",e.getMessage()));
            return commandResult;
        }

        return commandResult;
    }
}
