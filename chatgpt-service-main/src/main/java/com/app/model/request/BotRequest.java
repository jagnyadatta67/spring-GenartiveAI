package com.app.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BotRequest implements Serializable {
    private String colour;
    private String code;
    private String category;
    private String style;
    private String itemType;
}



