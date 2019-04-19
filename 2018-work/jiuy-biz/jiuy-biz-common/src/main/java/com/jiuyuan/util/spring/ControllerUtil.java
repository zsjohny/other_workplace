package com.jiuyuan.util.spring;


public class ControllerUtil {

    public static String redirect(String url) {
        return "redirect:" + url;
    }

    public static String forward(String url) {
        return "forward:" + url;
    }
}
