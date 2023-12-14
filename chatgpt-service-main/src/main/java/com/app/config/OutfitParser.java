package com.app.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class OutfitParser {


    public  static String parseStyles(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> string =new ArrayList<>();
        String outfitText = null;
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode choicesNode = rootNode.get("choices");
            JsonNode outfitNode = choicesNode.get(0);
            
            outfitText = outfitNode.get("message").get("content").asText();
            outfitText.replace("\n","");
            outfitText.replace("\\","");
            // Printing the outfit details
            
            } catch (Exception e) {
            e.printStackTrace();
        }
        return outfitText;
    }
}