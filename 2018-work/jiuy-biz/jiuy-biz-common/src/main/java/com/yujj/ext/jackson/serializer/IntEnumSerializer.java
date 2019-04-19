package com.yujj.ext.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.jiuyuan.util.enumeration.IntEnum;

public class IntEnumSerializer extends StdScalarSerializer<IntEnum> {

    public static final IntEnumSerializer INSTANCE = new IntEnumSerializer();

    public IntEnumSerializer() {
        super(null);
    }

    @Override
    public void serialize(IntEnum value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
        JsonGenerationException {
        jgen.writeNumber(value.getIntValue());
    }
}