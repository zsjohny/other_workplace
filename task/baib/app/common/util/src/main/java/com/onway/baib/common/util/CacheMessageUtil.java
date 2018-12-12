package com.onway.baib.common.util;


/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 不同的本地缓存内容转换成StringBuffer
 * 
 * @author guangdong.li
 * @version $Id: CacheMessageUtil.java, v 0.1 17 Feb 2016 15:37:28 guangdong.li Exp $
 */
public class CacheMessageUtil {

    /** ENTERSTR */
    public static final String ENTERSTR = "\n";

    /**
     * 对Map的类型转换成
     * 
     * @param map 
     * 
     * @return StringBuffer
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String map2String(Map map) {

        StringBuffer cacheMessage = new StringBuffer();

        for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {

            String name = i.next();

            cacheMessage.append(ENTERSTR
                                + "["
                                + name
                                + "={"
                                + ToStringBuilder.reflectionToString(map.get(name),
                                    ToStringStyle.SHORT_PREFIX_STYLE) + "}]");

        }

        return cacheMessage.toString();
    }
}
