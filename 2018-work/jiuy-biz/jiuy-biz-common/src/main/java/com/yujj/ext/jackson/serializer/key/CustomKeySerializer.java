//package com.yujj.ext.jackson.serializer.key;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
//import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
//import com.fasterxml.jackson.databind.type.SimpleType;
//import com.jiuyuan.util.enumeration.IntEnum;
//import com.jiuyuan.util.enumeration.StringEnum;
//
//import java.io.IOException;
//
//public class CustomKeySerializer extends StdScalarSerializer<Object> {
//
//    private static final JsonSerializer<Object> stdKeySerializer =
//            StdKeySerializers.getStdKeySerializer(SimpleType.construct(Object.class));
//
//    public CustomKeySerializer() {
//        super(null);
//    }
//
//    @Override
//    public void serialize(Object key, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//
//        if (key != null) {
//
//            Class<?>[] interfaces = key.getClass().getInterfaces();
//            for (Class<?> clazz : interfaces) {
//                if (StringEnum.class.isAssignableFrom(clazz)) {
//                    StringEnumKeySerializer.INSTANCE.serialize(key, jgen, provider);
//                    return;
//                } else if (IntEnum.class.isAssignableFrom(clazz)) {
//                    IntEnumKeySerializer.INSTANCE.serialize(key, jgen, provider);
//                    return;
//                }
//            }
//        }
//
//        stdKeySerializer.serialize(key, jgen, provider);
//    }
//}