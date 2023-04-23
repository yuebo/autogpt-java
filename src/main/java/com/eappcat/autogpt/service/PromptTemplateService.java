package com.eappcat.autogpt.service;

import com.eappcat.autogpt.enums.PromptTemplateLang;
import com.eappcat.autogpt.models.template.PromptTemplate;
import com.eappcat.autogpt.utils.TemplateUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 指令模板类，用于支持多语言指令集合
 */
@Service
public class PromptTemplateService {
    private LoadingCache<String, String> cache;

    /**
     * 初始化模板缓存
     */
    @PostConstruct
    protected void init(){
        cache = CacheBuilder.newBuilder()
                .maximumSize(5000).recordStats().build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String template) throws Exception {
                                ClassPathResource resource = new ClassPathResource("prompt".concat("/").concat(template).concat(".txt"));
                                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                                StringBuilder builder = new StringBuilder();
                                String line;
                                while ((line = reader.readLine())!=null){
                                    builder.append(line).append("\n");
                                }
                                return builder.toString();
                            }
                        }
                );
    }

    /**
     * 使用Spring EL 解析参数
     * @param template 模板文件名称，不包含扩展名
     * @param lang 语言
     * @param params 参数对象，Spring EL的Root
     * @return
     */
    private String parse(String template, PromptTemplateLang lang ,Object params){
        if (lang == null){
            lang = PromptTemplateLang.EN;
        }
        String templateToUse = cache.getUnchecked(lang.name().toLowerCase().concat("/").concat(template));
        return TemplateUtil.render(templateToUse,params);
    }

    /**
     * 使用Spring EL 解析参数
     * @param template 模板的模型类
     * @param lang 语言
     * @return
     */
    public String parse(PromptTemplate template, PromptTemplateLang lang){
        return this.parse(template.model(),lang,template);
    }

}
