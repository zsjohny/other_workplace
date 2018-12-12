package com.onway.baib.common.util;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.onway.common.lang.Money;
import com.onway.platform.common.utils.LogUtil;
/**
 * 
 * DO对象转Info对象工具类
 * @author kay
 * @version $Id: DOToInfo.java, v 0.1 2016年9月8日 上午11:47:55 Administrator Exp $
 */
public class DOToInfo {
    /** logger */
    private static final Logger          logger = Logger.getLogger(DOToInfo.class);
    /**
     * @param DOobj DO对象
     * @param infoClass info对象class
     * @return  info对象
     */
      public  static <T1,T2> T2  getInfo( T1 DOobj,Class<T2> infoClass){
          T2 returnInfo=null; 
         try {
            returnInfo=  infoClass.newInstance();
            Field[] infoFields= infoClass.getDeclaredFields();
            for(Field infoField:infoFields){
                infoField.setAccessible(true);
                System.out.println("filedNane:"+infoField.getName());
                Field doField=DOobj.getClass().getDeclaredField(infoField.getName());
                Object value=null;
                if(doField!=null){
                    doField.setAccessible(true);
                    value=doField.get(DOobj); 
                }
                if(value==null){ continue;  }
                if( "serialVersionUID".equals(infoField.getName()) ){
                    continue;
                }
                //如果info对象的字段是JSONObject类型  将json串转换成JSONObject
                if( infoField.getType()==JSONObject.class ){
                    String json=(String) value;
                    if(StringUtils.isNotBlank(json)){
                        JSONObject jsonObj= JSONObject.parseObject(json);
                        infoField.set(returnInfo, jsonObj);
                    }
                }else if(infoField.getType()==String.class){ //String
                    infoField.set(returnInfo, (String)value);
                }else if (value.getClass() == Integer.class) {
                    infoField.set(returnInfo, (Integer) value);
                } else if (value.getClass() == Double.class) {
                    infoField.set(returnInfo, (Double) value);
                } else if (value.getClass() == Long.class) {
                    infoField.set(returnInfo, (Long) value);
                } else if (value.getClass() == Float.class) {
                    infoField.set(returnInfo, (Float) value);
                } else if (value.getClass() == java.sql.Timestamp.class) {
                    Date date=new Date();
                    date=(Date) value;
                    infoField.set(returnInfo,  date);
                }else if(value.getClass() == java.util.Date.class){
                    infoField.set(returnInfo, (java.util.Date) value);
                }else if (value.getClass() == Money.class) {
                    infoField.set(returnInfo, (Money)value);
                }
            }
        } catch (Exception e) {
            LogUtil.info(logger, (MessageFormat.format(
                "DO对象转Info对象失败...", e)));
        }
        return returnInfo;
      }
          
    public static void main(String[] args) {
     /*   GoodDO good=new GoodDO();
        good.setId(100);
        good.setCode("10086");
        good.setDiscountPrice(new Money(1100));
        good.setGmtCreate(new Date());
        good.setParameterJson("{name:\"zhangsan\"}");
        GoodsInfo info=getInfo(good,GoodsInfo.class);
        System.out.println(info);*/
    }   
          
          
}
