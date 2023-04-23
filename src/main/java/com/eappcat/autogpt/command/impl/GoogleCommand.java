package com.eappcat.autogpt.command.impl;

import com.alibaba.fastjson.JSON;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class GoogleCommand implements Command {

    @Override
    public CommandResult execute(ExecuteContext context) throws Exception{
        CommandResult commandResult = new CommandResult(this);

        String google = System.getenv().getOrDefault("GOOGLE_MIRROR_URL","https://www.google.com");

        String query = context.getArgs().get("query");
        String num = context.getArgs().getOrDefault("num","5");
        Document document = Jsoup.connect(google.concat("/search?q=").concat(query).concat("&num=").concat(num)).get();

        Elements elements = document.select("h3");
        List<GoogleResult> resultList = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            Element el = elements.get(i);
            Element a = el.closest("a");
            Element p = null;
            String body = "";
            String href = "";
            String title = el.text();
            if (a!=null){
                href = a.attr("href");
                if (href.equals("&sa=")){
                    href = StringUtils.substringBetween(href,"q=","&sa=");
                }
                if (a.parent()!=null){

                    p = a.parent().parent();
                    if (p!=null){
                        Element sb = p.nextElementSibling();
                        if (sb!=null){
                            body = sb.text();
                        }
                    }
                }
            }
            if (StringUtils.isEmpty(title)||StringUtils.isEmpty(href)){
                continue;
            }
            GoogleResult result = new GoogleResult(title,body,href);
            resultList.add(result);
        }
        commandResult.setData(resultList);
        return commandResult;
    }

    @Override
    public boolean accept(String cmd) {
        return "google_search".equals(cmd);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoogleResult {
        private String title;
        private String body;
        private String href;
    }
}
