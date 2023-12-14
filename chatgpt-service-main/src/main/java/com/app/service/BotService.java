package com.app.service;

import com.app.model.request.BotRequest;

public interface BotService {

//    String createProductDescprition(BotRequest botRequest);
//    public String suggestKeyWord(BotRequest botRequest);
//    public String suggestMatch(BotRequest botRequest);

    String suggestStyles(BotRequest botRequest);
}
