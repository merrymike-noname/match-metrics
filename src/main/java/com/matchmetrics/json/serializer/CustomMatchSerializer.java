package com.matchmetrics.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.matchmetrics.entity.Match;

import java.io.IOException;

public class CustomMatchSerializer extends StdSerializer<Match> {
    @Override
    public void serialize(Match match, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(match.getId());
    }

    public CustomMatchSerializer() {
        this(null);
    }

    public CustomMatchSerializer(Class<Match> t) {
        super(t);
    }
}
