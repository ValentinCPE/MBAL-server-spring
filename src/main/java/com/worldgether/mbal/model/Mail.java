package com.worldgether.mbal.model;

import lombok.Data;

import java.util.Map;

@Data
public class Mail {

    private String from;

    private String to;

    private String subject;

    private String content;

    private Map<String,String> model;

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
