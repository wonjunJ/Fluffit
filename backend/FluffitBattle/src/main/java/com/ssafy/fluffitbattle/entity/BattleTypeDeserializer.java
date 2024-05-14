package com.ssafy.fluffitbattle.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BattleTypeDeserializer extends JsonDeserializer<BattleType> {
    @Override
    public BattleType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String key = node.get("key").asText();

        for (BattleType type : BattleType.values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null; // 또는 예외 처리
    }
}

