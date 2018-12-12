/**
 * onway.com Inc.
 * Copyright (c) 2013-2015 All Rights Reserved.
 */
package com.onway.web.controller.util;

/**
 * 正则表达式工具类
 * 
 * @author hong.li
 * @version $Id: RegexUtils.java, v 0.1 2015年1月15日 下午6:00:55 hong.li Exp $
 */
public class RegexUtils {
    
    /**
     * 判断是否为电话号码  只判断11位的电话号码 前面不包含+86
     * 
     * @param cell
     * @return
     */
    public static boolean isCellNumber(String cell){
        if(cell==null){
            return false;
        }
        return cell.matches("^[1][2-9]\\d{9}$");
    }

}
