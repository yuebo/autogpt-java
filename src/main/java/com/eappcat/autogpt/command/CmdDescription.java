package com.eappcat.autogpt.command;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface CmdDescription {
    
}
