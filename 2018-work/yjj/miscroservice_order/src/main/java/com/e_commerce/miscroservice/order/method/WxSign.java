package com.e_commerce.miscroservice.order.method;


import com.e_commerce.miscroservice.commons.utils.pay.MD5Util;

import java.util.*;

/**
 * 微信签名工具类
 * 
 * @author zhaoxinglin
 *
 * @version 2017年4月11日下午7:32:09
 */
public class WxSign {

    private static String characterEncoding = "UTF-8";
    
    /**
     * 验证签名
     * @param my_sign
     * @param wx_sign
     * @return
     */
    public static boolean checkSign(String my_sign,String wx_sign){
    	return my_sign.equalsIgnoreCase(wx_sign);
    }
    

    @SuppressWarnings("rawtypes")
    public static String createSign(SortedMap<Object,Object> parameters,String key){ 
      StringBuffer sb = new StringBuffer(); 
      Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序） 
      Iterator it = es.iterator(); 
      while(it.hasNext()) { 
        Map.Entry entry = (Map.Entry)it.next(); 
        String k = (String)entry.getKey(); 
        Object v = entry.getValue(); 
       // System.out.println(sb.toString());
        if(null != v && !"".equals(v)  
            && !"sign".equals(k) && !"key".equals(k)) { 
          sb.append(k + "=" + v + "&"); 
        } 
      } 
      sb.append("key=" + key);
      System.out.println(sb.toString());
      String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
      return sign; 
    }

    public static String getNonceStr() {
      Random random = new Random();
      return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    public static String getTimeStamp() {
      return String.valueOf(System.currentTimeMillis() / 1000);
    }

  }