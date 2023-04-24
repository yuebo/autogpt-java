package com.eappcat.autogpt.command.impl;

;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.service.PromptTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;


@ConditionalOnProperty(prefix = "chatgpt", value = "browser", havingValue = "chrome", matchIfMissing = false)
@Component
@Slf4j
public class ChromeBrowseSiteCommand extends AbstractBrowseSiteCommand implements Command {

    public ChromeBrowseSiteCommand(OpenAiService openAiService, PromptTemplateService templateService) {
        super(openAiService, templateService);
    }

    @Override
    public boolean accept(String cmd) {
        return "browse_website".equals(cmd);
    }

    @Override
    protected @NotNull String scrapeText(String url) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--headless", "--disable-gpu");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.5615.49 Safari/537.36");
        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        try{
            chromeDriver.get(url);
            WebDriverWait webDriverWait = new WebDriverWait(chromeDriver,Duration.ofSeconds(10));
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            String body = (String)chromeDriver.executeScript("return document.body.outerText\n;");
            return body;
        }finally {
            chromeDriver.quit();
        }
    }
}
