package com.yujj.ext.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.jiuyuan.util.enumeration.StringEnum;

public class StringEnumSerializer extends StdScalarSerializer<StringEnum> {

    public static final StringEnumSerializer INSTANCE = new StringEnumSerializer();

    public StringEnumSerializer() {
        super(null);
    }

    @Override
    public void serialize(StringEnum value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
        JsonGenerationException {
        StringEnum stringEnum = value;
        jgen.writeString(stringEnum.getStringValue());
    }
}