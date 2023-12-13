package com.app.controller;

import com.app.config.OutfitParser;
import com.app.model.request.BotRequest;
import com.app.model.response.ChatGptResponse;
import com.app.service.AlgoliaService;
import com.app.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    private final AlgoliaService algoliaService;

    @PostMapping(value = "/writeContent",produces = { "application/json" })
    public List<String> writeContent(@RequestBody BotRequest botRequest) {
        return OutfitParser.parse(botService.createProductDescprition(botRequest));
    }
    @PostMapping(value = "/suggestion",produces = { "application/json" })
    public List<String> suggestion(@RequestBody BotRequest botRequest) {
        return OutfitParser.parse(botService.suggestKeyWord(botRequest));
    }

    @PostMapping(value = "/algolia",produces = { "application/json" })
    public String algolia(@RequestBody BotRequest botRequest) {
       return algoliaService.algoliaQuery(botRequest);

    }
    }




