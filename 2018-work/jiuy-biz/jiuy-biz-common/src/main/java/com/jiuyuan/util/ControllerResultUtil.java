/**
 * 
 */
package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

import com.yujj.util.StringUtil;


/**
 * 用于将通用的查询结果进行转换
 * 
 * @author LWS
 *
 */

public class ControllerResultUtil<T extends Object> {

    public static <T> boolean parseMap2ModelMap(Map<String,T> sourceResult, ModelMap destResult){
        if(null == sourceResult || sourceResult.size() == 0){
            return false;
        }else if(null == destResult){
            return false;
        }else{
            Set<Entry<String, T>> entries = sourceResult.entrySet();
            Iterator<Entry<String, T>> it = entries.iterator();
            while(it.hasNext()){
                Entry<String, T> en = it.next();
                destResult.addAttribute(en.getKey(), en.getValue());
            }
            return true;
        }
        
    }
    
    public static <T> String parseMap2Httpparamstring(Map<String,T> sourceResult,boolean needSort, boolean needQuote){
        if(null == sourceResult || sourceResult.size() == 0){
            return "";
        }else{
            // 排序
            List<String> keys = new ArrayList<String>(sourceResult.keySet());
            if(needSort){
                Collections.sort(keys);
            }
            int count = 0;
            StringBuffer paramSb = new StringBuffer();
            for (int i = 0; i < keys.size(); i++) {
                String _key = keys.get(i);
                T _value = sourceResult.get(_key);
                ++ count;
                if(1 == count){
                    if(needQuote){
                        paramSb.append(StringUtil.join(_key,_ASSIGNMENT,StringUtils.join(_QUOTE,_value,_QUOTE)));
                    }else{
                        paramSb.append(StringUtil.join(_key,_ASSIGNMENT,_value));
                    }
                }else{
                    if(needQuote){
                        paramSb.append(StringUtil.join(_CONCAT,_key,_ASSIGNMENT,StringUtils.join(_QUOTE,_value,_QUOTE)));
                    }else{
                        paramSb.append(StringUtil.join(_CONCAT,_key,_ASSIGNMENT,_value));
                    }
                }
            }
            return paramSb.toString();
        }
    }
    
    private static final String _CONCAT = "&";
    private static final String _ASSIGNMENT = "=";
    private static final String _QUOTE = "\"";
}
