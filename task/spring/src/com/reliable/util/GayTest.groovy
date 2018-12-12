package com.reliable.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nessary on 16-5-30.
 */
public class GayTest {

    public static void main(String[] args) {

        String partTimeStr = "2010-10-20"

        String operateSys = "Ios"

        String phone = "15924179757"


        int regCount = 0;
        Properties properties = new Properties();
        try {
            properties.load(
                    new InputStreamReader(QueryOutside.class.getResourceAsStream("/GayOutside.properties"), "utf-8"));
        } catch (Exception e) {
        }
        QueryPhoneSetAttr queryPhoneSetAttr = new QueryPhoneSetAttr();

        StringBuilder sb = new StringBuilder();

        StringBuilder sbtotal = new StringBuilder();
        StringBuilder sbExcep = new StringBuilder();
        Map map = null;

        try {
            String string = QyeryPhoneByProxyIp.httpURLConnectionGET(properties.getProperty("tongxinUrl"),
                    new StringBuilder().append(properties.getProperty("tongxinParam")).append(phone).toString());




            if (!string.contains(properties.getProperty("tongxinRegMsg"))) {
                sb.append(properties.getProperty("tongxinTips"));
                regCount++;
            }

            sbtotal.append(properties.getProperty("tongxinTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("tongxinTips"));
            e.printStackTrace();
        }

        println sb.toString()

    }

}
