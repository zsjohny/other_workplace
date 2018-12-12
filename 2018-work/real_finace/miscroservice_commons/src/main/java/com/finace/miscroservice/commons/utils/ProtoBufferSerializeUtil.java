package com.finace.miscroservice.commons.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * protobuffer序列化工具
 * 注意 不加入序列化
 * 同java自带序列化 transient 修饰即可
 */
public class ProtoBufferSerializeUtil {
    private ProtoBufferSerializeUtil() {
    }

    /**
     * 序列化
     *
     * @param obj 需要序列化的对象
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        Schema schema = RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer allocate = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] result = ProtobufIOUtil.toByteArray(obj, schema, allocate);
        allocate.clear();
        return result;
    }


    /**
     * 反序列化
     *
     * @param sources byte数组
     * @param obj     反序列化的类
     * @return
     */
    public static <T> T deserializer(byte[] sources, Class<T> obj) {

        Schema<T> schema = RuntimeSchema.getSchema(obj);

        T result = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(sources, result, schema);

        return result;

    }


}
