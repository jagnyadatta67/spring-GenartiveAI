package com.app.service;

import com.app.model.request.BotRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.app.config.AlgoliaConfig.*;

@Service
public class AlgoliaServiceImpl implements AlgoliaService
{
    public static final String EMPTY_STRING = "";
    private static RestTemplate restTemplate = new RestTemplate();

    @Override
    public  String algoliaQuery(BotRequest botRequest)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Algolia-API-Key", apiKey);
        headers.set("X-Algolia-Application-Id", applicationId);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String filterQuery = botRequest.getItemType() + "%20" + botRequest.getStyle()+ "%3Acolor.en%3A" + botRequest.getColour() + "%3AallCategories%3A"+ botRequest.getCategory();
        String requestBody = String.format("{ \"params\": \"query=%s&hitsPerPage=%d&getRankingInfo=%b\" }", filterQuery, hitsPerPage, getRankingInfo);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        try {
            if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful()) {
                return filterAttributes(responseEntity.getBody());
            } else {
                return "Error: " + responseEntity.getStatusCodeValue();
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error occured ="+ ex.getStackTrace());
        }
        return "{}";
    }

    private String filterAttributes(String responseBody)
    {
        List<JsonNode> extractedObjects = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode hitsNode = rootNode.path("hits");
            Iterator<JsonNode> hitsIterator = hitsNode.elements();

            while (hitsIterator.hasNext()) {
                JsonNode hitNode = hitsIterator.next();

                // Extract specific attributes
                String objectId = hitNode.path("objectID").asText();
                String galleryImagesNode =Objects.nonNull(hitNode.path("gallaryImages"))?hitNode.path("gallaryImages").get(0).asText(): EMPTY_STRING;
                JsonNode childDetail = hitNode.path("childDetail");
                String price = hitNode.path("price").asText();
                String pdpUrl= "https://uat1.lifestylestores.com/in/en/p/"+ objectId;
                boolean isNameNotEmpty = Objects.nonNull(hitNode.path("_highlightResult")) && Objects.nonNull(hitNode.path("_highlightResult").get("name")) && Objects.nonNull(hitNode.path("_highlightResult").get("name").get("en"));
                String productName = isNameNotEmpty?hitNode.path("_highlightResult").get("name").get("en").get("value").asText():"";


                // Create a new JsonNode for the extracted data
                JsonNode extractedObject = objectMapper.createObjectNode()
                        .put("productId", objectId)
                        .put("imageUrl", galleryImagesNode)
                        .put("price",price)
                        .put("pdpUrl",pdpUrl)
                        .put("productName",productName)
                        .set("skuDetail", childDetail);

                extractedObjects.add(extractedObject);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JsonNode representing the result
        JsonNode resultNode = objectMapper.valueToTree(extractedObjects);

        // Convert the JsonNode to a JSON string
        try {
            return objectMapper.writeValueAsString(resultNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Return an empty JSON object in case of an error
        }
    }

}
