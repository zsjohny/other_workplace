package com.onway.web.controller.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.onway.common.lang.HttpUtils;

/**
 * 创蓝短信通道
 * 
 * @author li.hong
 * @version $Id: ChuanglanSmsSender.java, v 0.1 2016年10月11日 上午10:39:52 li.hong Exp $
 */
public class ChuanglanSmsSender{

    public static void main(String[] args) {
        try {
            //发送内容
            String phone="18010972281";
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("account", "N602811_N5629222");
            paramsMap.put("pswd", "SFIJ8tnUqB05b3");
            paramsMap.put("mobile",phone);
            paramsMap.put("msg", "尊敬的用户您好，您的短信验证码为：320569，（五分钟有效）");
            paramsMap.put("needstatus", "false");
            System.out.println("发送内容：" + paramsMap.toString());

            //转换返回值
            String returnStr = HttpUtils.executePostMethod(
                "http://sms.253.com/msg/HttpBatchSendSM?", "UTF-8", paramsMap);

            System.out.println(returnStr);
            String[] strs = returnStr.split("\n");

            String value = "";
            if (StringUtils.isNotBlank(strs[0])) {
                value = strs[0].split(",")[1];
            } else {

            }
            System.out.println(value);
            
            if (!StringUtils.equals(value, "0")) {
               System.out.println("短信发送失败，"+phone);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
