package com.eappcat.autogpt.models.thought;

import lombok.Data;

import java.util.Map;

@Data
public class ThoughtResponse {
    private Thoughts thoughts;

    private Commands command;

    @Data
    public static class Thoughts {
        private String text;
        private String reasoning;
        private String plan;
        private String criticism;
        private String speak;
    }

    @Data
    public static class Commands {
        private String name;
        private Map<String,String> args;
    }

}
