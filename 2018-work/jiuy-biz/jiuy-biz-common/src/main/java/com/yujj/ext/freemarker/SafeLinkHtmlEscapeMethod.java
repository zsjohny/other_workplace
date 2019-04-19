package com.yujj.ext.freemarker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.utility.StringUtil;

/**
 * <pre>
 * 在转义HTML文本时，对其中的A标签做特殊处理，显示为链接，并且不会破坏页面结构。
 * 要求A标签必须具备良好的结构。否则它有可能被转义。
 * 注意，没有考虑url本身的安全问题。
 * </pre>
 * 
 */
public class SafeLinkHtmlEscapeMethod implements TemplateMethodModel {

    /** patten of A tag */
    private static Pattern linkPattern = Pattern.compile("<a\\s[^<>]*href\\s*=[\"']?([^\"'<> ]+)[^<>]*>([^<>]+)</a>",
        Pattern.CASE_INSENSITIVE);

    /**
     * 当前应用的域。结果url的域如果和当前应用相同，就在同一个窗口中打开，否则在新窗口中打开。
     */
    private String domain;

    /**
     * constructor
     * 
     * @param domain 当前应用的域。结果url的域如果和当前应用相同，就在同一个窗口中打开，否则在新窗口中打开。
     */
    public SafeLinkHtmlEscapeMethod(String domain) {
        this.domain = domain;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) {
        String html = (String) arguments.get(0);
        String attributes = "";
        if (arguments.size() > 1) {
            attributes = (String) arguments.get(1);
        }
        return realWork(html, this.domain, attributes);
    }

    /**
     * 转义一段html，把其中符合patten的A标签替换为一个简单的版本。
     * 
     * @param html html文本
     * @param domain 当前应用的域
     * @param attributes 这个字符串会被添加到A标签的起始标签中
     * @return 转义后的html
     */
    public static String realWork(String html, String domain, String attributes) {
        if (html == null) {
            return "";
        }
        if (StringUtils.isBlank(attributes)) {
            attributes = "";
        }
        StringBuilder result = new StringBuilder();
        Matcher matcher = linkPattern.matcher(html);
        int endIndexOfDone = 0;
        while (matcher.find()) {
            String url = matcher.group(1);
            String name = matcher.group(2);
            String target = url.contains(domain) ? "" : " target=\"_blank\"";
            if (matcher.start() > endIndexOfDone) {
                result.append(StringUtil.XMLEnc(html.substring(endIndexOfDone, matcher.start())));
            }
            int index = url.indexOf("http");
            if (index > 0) {
                url = url.substring(index);
            }
            result.append("<a href=\"" + url + "\"" + target + " " + attributes + ">" + name + "</a>");
            endIndexOfDone = matcher.end();
        }
        if (endIndexOfDone < html.length()) {
            result.append(StringUtil.XMLEnc(html.substring(endIndexOfDone)));
        }
        return result.toString();
    }
}
