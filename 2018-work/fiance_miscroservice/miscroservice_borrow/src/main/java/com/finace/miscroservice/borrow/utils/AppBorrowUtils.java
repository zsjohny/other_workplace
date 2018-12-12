package com.finace.miscroservice.borrow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AppBorrowController中移植过来的 工具类方法
 */
public class AppBorrowUtils {
    //获取html里的图片地址
    public static List<String> searchHtmlImage(String contentStr) {
        String regexImage = "<img.+?src=\"(.+?)\".+?/?>";
        Pattern p = Pattern.compile(regexImage, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(contentStr);
        List<String> imagestrlist = new ArrayList<String>();
        while (m.find()) {
            imagestrlist.add(m.group(1));
        }
        return imagestrlist;
    }

    //清除HTML里的IMG标签内容
    public static String clsHtmlImage(String contentStr) {
        String regexImage = "<img.+?src=\"(.+?)\".+?/?>";
        Pattern p = Pattern.compile(regexImage, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(contentStr);
        while (m.find()) {
            contentStr = contentStr.replace(m.group(0), "");
        }
        return contentStr;
    }
}
