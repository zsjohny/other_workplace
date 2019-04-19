package com.jiuy.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jiuyuan.entity.log.Log;

public class JsonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("JsonUtil");
	
	private static String TAG = "tag";
    
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //对于为null的字段不进行序列化
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //对于未知属性不进行反序列化
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //无论对象中的值只有不为null的才进行序列化
        mapper.setSerializationInclusion(Include.NON_NULL);
    }



    public static Object getValue(String jsonString, String key) {
        Object object = JSON.parse(jsonString);
        return ((JSONObject) object).get(key);
    }
    
    /**
     * 把对象转化成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJSON(Object obj) {
        if (obj == null) {
            return null;
        }

        Writer write = new StringWriter();
        try {
            mapper.writeValue(write, obj);
        } catch (JsonGenerationException e) {
            logger.error(TAG, e.toString() + obj);
        } catch (JsonMappingException e) {
        	logger.error(TAG, e.toString() + obj);
        } catch (IOException e) {
        	logger.error(TAG, e.toString() + obj);
        }
        return write.toString();
    }

}
