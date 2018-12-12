package com.onway.web.controller.convertor;

import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import com.onway.common.lang.Money;
import net.sf.cglib.core.Converter;
/**
 * 
 * 商品GoodInfo的转换器
 * @author kay
 * @version $Id: GoodsConverter.java, v 0.1 2016年9月26日 上午10:09:30 Administrator Exp $
 */
public class GoodsConverter implements Converter {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    /**
     * value:源对象属性，target:目标对象属性类，context:目标对象setter方法名
     * 
     */
    @Override
    public Object convert(Object value, Class target, Object context) {
        if(value==null){
            return null;
        }
        if( value.getClass()==JSONObject.class ){
           return (JSONObject)value;
        }else if(value.getClass()==String.class){ //String
            String str=  (String)value;
            if("setParameterJson".equals(context) || "setLabelJson".equals(context)){
                JSONObject jsonObj= JSONObject.parseObject(str);
                return jsonObj;
            }
            return  str;
        }else if (value.getClass() == Integer.class) {
            return  (Integer) value;
        } else if (value.getClass() == Double.class) {
            return  (Double) value;
        } else if (value.getClass() == Long.class) {
            return  (Long) value;
        } else if (value.getClass() == Float.class) {
            return  (Float) value;
        }else if(value.getClass() == java.util.Date.class){
            return  (java.util.Date) value;  
        }else if (value.getClass() == Money.class) {
           return  (Money)value;
        }
        return value;  
    }
}
