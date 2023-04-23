package com.eappcat.autogpt.models.template;

import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
public class CurrentDatePrompt implements PromptTemplate{
    private Date date= new Date();


    public String getCurrentDateDesc(){
        return DateFormatUtils.format(this.date,"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String model() {
        return "current_date";
    }
}
