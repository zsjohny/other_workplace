package com.finace.miscroservice.official_website.utils;

import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Created by hyf on 2018/3/6.
 */
public class SysUtil {
    /**
     * 校验 实体类 是否有空 参数
     *
     * 使用java中的反射机制，来获取对象的属性清单，进而获取该属性的值。
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static boolean checkObjFieldIsNull(Object obj)  {
        boolean flag = false;
        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                if(f.get(obj) == null){
                    flag = true;
                    return flag;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


}
