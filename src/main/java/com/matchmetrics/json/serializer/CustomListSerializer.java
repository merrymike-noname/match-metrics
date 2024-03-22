package com.matchmetrics.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.matchmetrics.entity.Match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListSerializer extends StdSerializer<List<Match>> {
    @Override
    public void serialize(List<Match> matches, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Integer> idList = new ArrayList<>();
        for (Match match : matches) {
            idList.add(match.getId());
        }
        jsonGenerator.writeObject(idList);
    }

    public CustomListSerializer() {
        this(null);
    }

    public CustomListSerializer(Class<List<Match>> t) {
        super(t);
    }
}
