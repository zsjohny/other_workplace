package com.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Package com.util
 * @Description:
 * @author: Aison
 * @date: 2018/5/16 15:43
 * @Copyright: 玖远网络
 */
public enum LoginWay {


    SMS(1,"手机验证码","如果这不是你的操作，你的手机验证码已经泄漏。请重新获取验证码登录门店宝，勿将验证码告诉他人"),
    WXA(2,"微信帐号","如果这不是你的操作，你的微信密码已经泄漏。请及时修改你的微信密码，并重新登录门店宝。");

    private Integer code;

    private String name;

    private String comment;

    LoginWay(Integer code,String name,String comment){
        this.code = code;
        this.name = name;
        this.comment = comment;
    }

    private static Map<Integer,LoginWay> loginWayMap = new HashMap<>();
    static {
        for(LoginWay loginWay :LoginWay.values()){
            loginWayMap.put(loginWay.getCode(),loginWay);
        }
    }

    public static LoginWay getWayEnum(Integer code) {
        LoginWay loginWay = loginWayMap.get(code);
        return loginWay;
    }

    public static String getWay(Integer code) {
        LoginWay loginWay = loginWayMap.get(code);
        return loginWay == null ? "" :loginWay.getName();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
