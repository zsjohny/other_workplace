package com.yujj.ext.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.jiuyuan.web.help.JsonMap;

public class JsonMapSerializer extends StdScalarSerializer<JsonMap> {

    public static final JsonMapSerializer INSTANCE = new JsonMapSerializer();

    public JsonMapSerializer() {
        super(null);
    }

    @Override
    public void serialize(JsonMap value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
        JsonGenerationException {
        provider.defaultSerializeValue(value.getMap(), jgen);
    }
}