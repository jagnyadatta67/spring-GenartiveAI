package com.app.service;

import com.app.model.request.BotRequest;

public interface AlgoliaService {
    String algoliaQuery(BotRequest botRequest);
}
