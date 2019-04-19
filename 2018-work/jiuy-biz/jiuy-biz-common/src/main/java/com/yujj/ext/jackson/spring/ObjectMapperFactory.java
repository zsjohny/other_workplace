package com.yujj.ext.jackson.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jiuyuan.util.enumeration.IntEnum;
import com.jiuyuan.util.enumeration.StringEnum;
import com.jiuyuan.web.help.JsonMap;
import com.yujj.ext.jackson.serializer.IntEnumSerializer;
import com.yujj.ext.jackson.serializer.JsonMapSerializer;
import com.yujj.ext.jackson.serializer.StringEnumSerializer;

public class ObjectMapperFactory implements FactoryBean<ObjectMapper> {

    @Override
    public ObjectMapper getObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CustomSerializerModule", Version.unknownVersion());
        // 注意：如果一个被序列化的类实现了以下的多个接口，那么结果会使用哪个Serializer，取决于这个类implements接口的顺序——声明在前的接口优先。
        module.addSerializer(StringEnum.class, StringEnumSerializer.INSTANCE);
        module.addSerializer(IntEnum.class, IntEnumSerializer.INSTANCE);
        module.addSerializer(JsonMap.class, JsonMapSerializer.INSTANCE);
        mapper.registerModule(module);

        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static void main(String[] args) throws Exception {

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(SI.SV, SI.SV);
        map.put(IS.IV, IS.IV);
        map.put("cValue", new C());

        ObjectMapper mapper = new ObjectMapperFactory().getObject();
    }

    private static enum SI implements StringEnum, IntEnum {
        SV();

        private int intValue = 3;

        private String stringValue = "stringThree";

        @Override
        public int getIntValue() {
            return intValue;
        }

        @Override
        public String getStringValue() {
            return stringValue;
        }
    }

    private static enum IS implements IntEnum, StringEnum {
        IV();

        private int intValue = 3;

        private String stringValue = "stringThree";

        @Override
        public int getIntValue() {
            return intValue;
        }

        @Override
        public String getStringValue() {
            return stringValue;
        }
    }

    private static class C {

        private String theField = "fieldValue";

        @SuppressWarnings("unused")
        public String getTheField() {
            return theField;
        }
    }
}