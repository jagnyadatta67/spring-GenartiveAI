package com.app.config;

public class ChatGptConfig {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String API_KEY = "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";
    public static final String MODEL = "gpt-3.5-turbo-1106";
    public static final Integer MAX_TOKEN = 700;
    public static final Double TEMPERATURE = 0.9;
    public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
}
