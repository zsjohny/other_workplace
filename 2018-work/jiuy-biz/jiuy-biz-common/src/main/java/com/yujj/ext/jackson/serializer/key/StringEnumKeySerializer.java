package com.yujj.ext.jackson.serializer.key;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.jiuyuan.util.enumeration.StringEnum;

public class StringEnumKeySerializer extends StdScalarSerializer<Object> {

    public static final StringEnumKeySerializer INSTANCE = new StringEnumKeySerializer();

    public StringEnumKeySerializer() {
        super(null);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
        JsonGenerationException {
        StringEnum stringEnum = (StringEnum) value;
        jgen.writeFieldName(stringEnum.getStringValue());
    }
}