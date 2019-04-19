package com.yujj.ext.jackson.serializer.key;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.jiuyuan.util.enumeration.IntEnum;

public class IntEnumKeySerializer extends StdScalarSerializer<Object> {

    public static final IntEnumKeySerializer INSTANCE = new IntEnumKeySerializer();

    public IntEnumKeySerializer() {
        super(null);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
        JsonGenerationException {
        IntEnum intEnum = (IntEnum) value;
        jgen.writeFieldName(String.valueOf(intEnum.getIntValue()));
    }
}