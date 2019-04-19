package com.jiuyuan.util;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.security.GeneralSecurityException;  
import java.security.MessageDigest;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
import java.util.TreeSet;  
/** 
* http接口请求参数签名工具类的实现和测试代码
*/  
public class ParamsSignUtil {  
      
    public static void main(String[] args)  
    {  
          
        //参数签名算法测试例子  
        HashMap<String, String> signMap = new HashMap<String, String>();  
        signMap.put("devid","920BF4CE-B9CC-449C-A146-BC5549D899ED");  
        signMap.put("devtype","1");  
        signMap.put("pcode","010210000");  
        signMap.put("t","1472701601599");  
        signMap.put("uid","0");
        signMap.put("v","4.5.3");  
        signMap.put("ver","1.0");  
        String secret_key="$3@%$$#"; //加密secret  
          
        System.out.println("得到签名sign1:"+getSign(signMap,secret_key));  
        HashMap<String, String> SignHashMap=ParamsSignUtil.sign(signMap, secret_key);  
        //System.out.println("SignHashMap:"+SignHashMap);  
        String sign=(String)SignHashMap.get("appSign");  
        //获取参数签名字符串  
        System.out.println("得到签名sign2:"+sign);  
  
  
  
      
    }  
  
    public static HashMap<String, String> sign(Map<String, String> paramValues,  
            String secret) {  
        return sign(paramValues, null, secret);  
    }  
  
    /** 
     * @param paramValues 
     * @param ignoreParamNames 
     * @param secret 
     * @return 
     */  
    public static HashMap<String, String> sign(Map<String, String> paramValues,  
            List<String> ignoreParamNames, String secret) {  
        try {  
            HashMap<String, String> signMap = new HashMap<String, String>();  
            StringBuilder sb = new StringBuilder();  
            List<String> paramNames = new ArrayList<String>(paramValues.size());  
            paramNames.addAll(paramValues.keySet());  
            if (ignoreParamNames != null && ignoreParamNames.size() > 0) {  
                for (String ignoreParamName : ignoreParamNames) {  
                    paramNames.remove(ignoreParamName);  
                }  
            }  
            Collections.sort(paramNames);  
            sb.append(secret);  
            for (String paramName : paramNames) {  
                sb.append(paramName).append(paramValues.get(paramName));  
            }  
            sb.append(secret);  
            byte[] md5Digest = getMD5Digest(sb.toString());  
            String sign = byte2hex(md5Digest);  
            signMap.put("appParam", sb.toString());  
            signMap.put("appSign", sign);  
            return signMap;  
        } catch (IOException e) {  
            throw new RuntimeException("加密签名计算错误", e);  
        }  
          
    }  
  
    public static String utf8Encoding(String value, String sourceCharsetName) {  
        try {  
            return new String(value.getBytes(sourceCharsetName), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            throw new IllegalArgumentException(e);  
        }  
    }  
  
  
  
    private static byte[] getMD5Digest(String data) throws IOException {  
        byte[] bytes = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            bytes = md.digest(data.getBytes("UTF-8"));  
        } catch (GeneralSecurityException gse) {  
            throw new IOException(gse);  
        }  
        return bytes;  
    }  
  
  
    private static String byte2hex(byte[] bytes) {  
        StringBuilder sign = new StringBuilder();  
        for (int i = 0; i < bytes.length; i++) {  
            String hex = Integer.toHexString(bytes[i] & 0xFF);  
            if (hex.length() == 1) {  
                sign.append("0");  
            }  
            //sign.append(hex.toUpperCase());  
            sign.append(hex.toLowerCase());  
        }  
        return sign.toString();  
    }  
    public static String getSign(Map<String, String> params,String secret)  
    {  
        String ret="";  
        StringBuilder sb = new StringBuilder();  
        Set<String> keyset=params.keySet();  
        TreeSet<String> sortSet=new TreeSet<String>();  
        sortSet.addAll(keyset);  
        Iterator<String> it=sortSet.iterator();  
        sb.append(secret);  
        while(it.hasNext())  
        {  
            String key=it.next();  
            String value=params.get(key);  
            sb.append(key).append(value);  
        }  
        sb.append(secret);  
        byte[] md5Digest;  
        try {  
            md5Digest = getMD5Digest(sb.toString());  
            ret = byte2hex(md5Digest);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return ret;  
    }

	
      
  
}  
