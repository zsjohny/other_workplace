package com.onway.baib.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.onway.platform.common.utils.VelocityHelper;

/**
 * 通讯报文辅助类
 * 
 * @author guangdong.li
 * @version $Id: ParamsUtil.java, v 0.1 2014年10月20日 下午3:35:25 guangdong.li Exp $
 */
public class ParamsUtil {

    /** logger */
    private static final Logger logger = Logger.getLogger(ParamsUtil.class);

    /**
     * 构造公共报文体
     * 
     * @param sign
     * @param cer
     * @param pay
     * @param params
     * @param commTemplate
     * @return
     */
    public static String getCommonXml(String sign, String cer, String pay,
                                      Map<String, Object> params, String commTemplate) {
        params.put("sign", sign);
        params.put("cer", cer);
        params.put("requestPrams", pay);
        String xmlStr = VelocityHelper.getInstance().evaluate(params, commTemplate);
        logger.info("请求交易报文：" + xmlStr.replaceAll("\r|\n", ""));
        return xmlStr.replaceAll("\r|\n", "");
    }

    /**
     * 构造请求头信息
     * 
     * @param websvrName
     * @param websvrCode
     * @param appForm
     * @param keep
     * @param requestTime
     * @return
     */
    public static String getCtrlInfo(String websvrName, String websvrCode, String appForm,
                                     String keep, String requestTime) {

        StringBuffer sb = new StringBuffer();
        sb.append("<CTRL-INFO WEBSVRNAME=\"");
        sb.append(websvrName);
        sb.append("\" WEBSVRCODE=\"");
        sb.append(websvrCode);
        sb.append("\" APPFROM=\"");
        sb.append(appForm);
        sb.append("\" KEEP=\"");
        sb.append(keep);
        sb.append("\" REQUESTTIME=\"");
        sb.append(requestTime);
        sb.append("\"/>");
        //logger.info(sb.toString());
        return sb.toString();
    }

    /**
     * 生成流水号：规则：终端号+当前时间+4位随机数字
     * 
     * @param tmnnum
     * @return
     */
    public static String getkeep(String tmnNum) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        Long random = (long) (Math.random() * Math.pow(10, 4));
        return tmnNum + time + random.toString();
    }

    /**
     * 获取当前时间字符形式:yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getRequestTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /**
     * 判断字符串是否为空
     * 
     * @param o
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取出一对标签之间的值
     * 
     * @param name
     *            标签名
     * @param xml目标字符串
     * @return
     */
    public String getValue(String name, String xml) {
        String start = "<" + name + ">";
        String end = "</" + name + ">";
        String value = xml.substring(xml.indexOf(start) + start.length(), xml.indexOf(end));
        // logger.info("标签名=" + name + " 标签值为=" + value);
        return value;
    }

    /**
     * 取出一对标签值(包括那对标签符)
     * 
     * @param name
     *            标签名
     * @param xml目标字符串
     * @return
     */
    public String getValue1(String name, String xml) {
        String start = "<" + name + ">";
        String end = "</" + name + ">";
        String value = xml.substring(xml.indexOf(start), xml.indexOf(end) + end.length());
        //logger.info("标签名=" + name + " 标签为=" + value);
        return value;
    }

}
