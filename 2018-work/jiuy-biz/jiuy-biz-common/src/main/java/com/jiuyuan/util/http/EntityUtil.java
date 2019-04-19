package com.jiuyuan.util.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class EntityUtil {

    public static byte[] toByteArray(HttpEntity entity) throws IOException {
        if (entity == null) {
            return new byte[]{};
        }
        return EntityUtils.toByteArray(entity);
    }
}
