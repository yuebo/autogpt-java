package com.eappcat.autogpt.command.impl;

import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.models.template.ReplyQuestionPrompt;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.service.PromptTemplateService;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
        String url = context.getArgs().get("url");
        String question = context.getArgs().get("question");
        Document document = Jsoup.connect(url).get();

        String body = document.body().text();

        log.info("抓取内容大小, {}", body.length());

        //split content to small size;
        int startCount = 900;
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
                }
            }

        }
        if (stringBuilder.length()>0){
            pp.add(stringBuilder.toString());
            stringBuilder.delete(0,stringBuilder.length());
        }

        commandResult.setData(pp);

        log.info("数据为{}",pp);

        //TODO save to database
        for (String p : pp){
            String template = templateService.parse(new ReplyQuestionPrompt(p,question),context.getLang());

            Message message = Message.builder().role(Message.Role.USER).content(template).build();

            List<BigDecimal> vector = openAiService.embeddings(p);
        }

        //TODO using vector search to find the similar with the same url and taskId


        return commandResult;
    }
}
