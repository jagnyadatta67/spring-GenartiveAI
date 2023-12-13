package com.app.service;

import com.app.model.request.BotRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.app.config.AlgoliaConfig.*;
@Service
public class AlgoliaServiceImpl implements AlgoliaService {

    private static RestTemplate restTemplate = new RestTemplate();

   @Override
   public  String algoliaQuery(BotRequest botRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Algolia-API-Key", apiKey);
        headers.set("X-Algolia-Application-Id", applicationId);
        headers.setContentType(MediaType.TEXT_PLAIN);
        String requestBody = String.format("{ \"params\": \"query=%s&hitsPerPage=%d&getRankingInfo=%b\" }", botRequest.getMessage(), hitsPerPage, getRankingInfo);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        return responseBody;
   }

}
