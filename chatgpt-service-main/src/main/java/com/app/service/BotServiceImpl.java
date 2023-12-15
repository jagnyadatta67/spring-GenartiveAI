package com.app.service;

import com.app.config.ChatGptConfig;
import com.app.model.request.BotRequest;
import com.app.model.request.ChatGptRequest;
import com.app.model.response.ChatGptResponse;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class BotServiceImpl implements BotService {

    private static RestTemplate restTemplate = new RestTemplate();
    @Resource
    private Environment environment;

    //    Build headers
    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
      String apiKey= Objects.nonNull(environment.getProperty("api_key"))? environment.getProperty("api_key"):ChatGptConfig.API_KEY;
      headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        return new HttpEntity<>(chatRequest, headers);
    }

    //    Generate response
    public String getResponse(HttpEntity<ChatGptRequest> chatRequestHttpEntity) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatRequestHttpEntity,
                String.class);

        return responseEntity.getBody();
    }
//
//    public String createProductDescprition(BotRequest botRequest) {
//        return this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequest(
//                                ChatGptConfig.MODEL,
//                               "Write the Product description for "+ botRequest.getL2category(),
//                                ChatGptConfig.TEMPERATURE,
//                                ChatGptConfig.MAX_TOKEN,
//                                ChatGptConfig.TOP_P)));
//    }
//    public String suggestKeyWord(BotRequest botRequest) {
//        var c= this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequest(
//                                ChatGptConfig.MODEL,
//                                "Suggest Best outfit with "+ botRequest.getColour()+","+botRequest+"  as comma separated",
//                                ChatGptConfig.TEMPERATURE,
//                                ChatGptConfig.MAX_TOKEN,
//                                ChatGptConfig.TOP_P)));
//        return c;
//    }
//
//    public String suggestClothes(BotRequest botRequest) {
//        return this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequest(
//                                ChatGptConfig.MODEL,
//                                "Suggest Best outfit with  "+ botRequest.getL2category()+"  comma separated",
//                                ChatGptConfig.TEMPERATURE,
//                                ChatGptConfig.MAX_TOKEN,
//                                ChatGptConfig.TOP_P)));
//    }

    @Override
    public String suggestStyles(BotRequest botRequest) {
        Map<String,String> msg=new HashMap<>();
        String content= "top 3 best outfit with outfit name for "+ botRequest.getColour()+","+botRequest.getCode().replace("-"," ")+" in json array format with a list of objects named \"outfits\" having attribute \"outfitName\" and a list of objects \"items\" object having attribute \"color\" and \"itemType\" and \"style\" ";
        msg.put("role","system");
        msg.put("content","You will be provided with a clothing item name and you need to provide best outfit style names, outfit items and item categories in comma separated values");
        msg.put("role","user");
        msg.put("content",content);

        var styles= this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                Arrays.asList(msg),
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TOP_P)));
        return styles;
    }
}






