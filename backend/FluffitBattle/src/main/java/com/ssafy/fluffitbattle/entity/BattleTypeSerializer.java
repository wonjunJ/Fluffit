package com.ssafy.fluffitbattle.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class BattleTypeSerializer extends JsonSerializer<BattleType> {
    @Override
    public void serialize(BattleType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("key", value.getKey());
        gen.writeStringField("title", value.getTitle());
        gen.writeNumberField("time", value.getTime());
        gen.writeStringField("description", value.getDescription());
        gen.writeEndObject();
    }
}