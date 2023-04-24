package com.eappcat.autogpt.command.impl;

import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.service.PromptTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "chatgpt", value = "browser", havingValue = "jsoap", matchIfMissing = true)
@Slf4j
public class JsoapBrowseSiteCommand extends AbstractBrowseSiteCommand implements Command {
    public JsoapBrowseSiteCommand(OpenAiService openAiService, PromptTemplateService templateService) {
        super(openAiService, templateService);
    }

    @Override
    public boolean accept(String cmd) {
        return "browse_website".equals(cmd);
    }

    @Override
    protected @NotNull String scrapeText(String url) throws Exception{
        Document document = Jsoup.connect(url).get();
        //移除样式和脚本，减少数据大小
        document.body().select("style").remove();
        document.body().select("script").remove();
        return document.body().text();
    }
}
