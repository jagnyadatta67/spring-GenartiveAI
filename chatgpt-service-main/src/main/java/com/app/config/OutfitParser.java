package com.app.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class OutfitParser {


    public  static List<String> parse(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> string =new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode choicesNode = rootNode.get("choices");
            JsonNode outfitNode = choicesNode.get(0);
            String outfitText = outfitNode.get("text").asText();
            String[] outfitItems = outfitText.split("\\n\\n")[1].split(", ");
            // Printing the outfit details
            System.out.println("Outfit Details:");
            for (String item : outfitItems) {
                string.add(item);
            }} catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }
}