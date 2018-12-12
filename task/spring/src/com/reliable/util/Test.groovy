package com.reliable.util

import net.sf.json.JSONObject
import org.apache.commons.lang.StringUtils

import java.text.SimpleDateFormat

/**
 * Created by nessary on 16-5-9.
 */
class Test {

    public static void main(String[] args) {

        String partTimeStr = "2010-10-20"

        String operateSys = "Ios"

        String phone = "15924179757"

        Integer regCount = 0

        String regScore = ""
        StringBuilder sb = new StringBuilder()

        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date partTime = sFormat.parse(partTimeStr);

        Properties productOnlineTimePro = new Properties();
        productOnlineTimePro.load(new InputStreamReader(QueryOutside.class.getResourceAsStream("/productOnlineTimePro.properties"), "utf-8"));

        QueryPhoneSetAttr queryPhoneSetAttr = new QueryPhoneSetAttr();
        Map map = null;
        JSONObject jsonObject = null;
        String string = null;
        Properties properties = new Properties();
        StringBuilder sbtotal = new StringBuilder();
        StringBuilder sbExcep = new StringBuilder();
        properties.load(new InputStreamReader(QueryOutside.class.getResourceAsStream("/queryphoneController.properties"), "utf-8"));


        try {
            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder().append("tantan").append(operateSys).append("Time").toString()))) {
                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder().append("tantan").append(operateSys).append("Time").toString())))) {
                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("tantanUrl"), new StringBuilder().append(properties.getProperty("tantanParam")).append(phone).append(properties.getProperty("tantanOtherParam")).toString(), queryPhoneSetAttr, properties.getProperty("tantanAttr"));

                    jsonObject = JSONObject.fromObject(map.get("content"));
                    if (jsonObject.get("message").equals(properties.getProperty("tantanRegMsg"))) {
                        sb.append(properties.getProperty("tantanTips"));

                        regCount++;
                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("tantanScore"), productOnlineTimePro).doubleValue();
                    }
                    sbtotal.append(properties.getProperty("tantanTips"));
                }
            }
        } catch (Exception e1) {
            sbExcep.append(properties.getProperty("tantanTips"));
            e1.printStackTrace();
        }
        println jsonObject

    }
}
