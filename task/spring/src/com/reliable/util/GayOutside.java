package com.reliable.util;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GayOutside {
    public static void main(String[] args) {
        GayOutside.GayOutside("15924179757");
    }

    public static void GayOutside(String phone) {

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
            String string = QyeryPhoneByProxyIp.httpURLConnectionGET(properties.getProperty("ZANKUrl"),
                    new StringBuilder().append(properties.getProperty("ZANKParam")).append(phone)
                            .append(properties.getProperty("ZANKOtherParam")).toString());

            if (!JSONObject.fromObject(string).get("error").equals(properties.getProperty("ZANKRegMsg"))) {
                sb.append(properties.getProperty("ZANKTips"));
                regCount++;
            }

            sbtotal.append(properties.getProperty("ZANKTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("ZANKTips"));
            e.printStackTrace();
        }

        try {
            map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("glowUrl"),
                    new StringBuilder().append(properties.getProperty("glowParam")).append(phone)
                            .append(properties.getProperty("glowOtherParam")).toString(),
                    queryPhoneSetAttr, properties.getProperty("glowAttr"));

            if (!JSONObject.fromObject(map.get("content")).get("message")
                    .equals(properties.getProperty("glowRegMsg"))) {
                sb.append(properties.getProperty("glowTips"));
                regCount++;
            }

            sbtotal.append(properties.getProperty("glowTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("glowTips"));
            e.printStackTrace();
        }

        try {
            map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("tiantianquanUrl"),
                    new StringBuilder().append(properties.getProperty("tiantianquanParam")).append(phone)
                            .append(properties.getProperty("tiantianquanOtherParam")).toString(),
                    queryPhoneSetAttr, properties.getProperty("tiantianquanAttr"));

            if (JSONObject.fromObject(map.get("content")).get("auth").toString()
                    .equals(properties.getProperty("tiantianquanRegMsg"))) {
                sb.append(properties.getProperty("tiantianquanTips"));

                regCount++;
            }
            sbtotal.append(properties.getProperty("tiantianquanTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("tiantianquanTips"));
            e.printStackTrace();
        }


        try {
            String string = QyeryPhoneByProxyIp.httpURLConnectionGET(properties.getProperty("gShowUrl"),
                    new StringBuilder().append(properties.getProperty("gShowParam")).append(phone).toString());


            if (JSONObject.fromObject(string).get("errorStr").equals(properties.getProperty("gShowRegMsg"))) {
                sb.append(properties.getProperty("gShowTips"));
                regCount++;
            }

            sbtotal.append(properties.getProperty("gShowTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("gShowTips"));
            e.printStackTrace();
        }


        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(properties.getProperty("zuozuoUrl"));
            JSONObject jso = new JSONObject();
            jso.put("v", "2.2.3");
            jso.put("username", phone);
            jso.put("password", "comuuuw");

            StringEntity entity = new StringEntity(jso.toString(), "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            String msg = EntityUtils.toString(httpEntity, "utf-8");

            if (JSONObject.fromObject(msg).get("msg").equals(properties.getProperty("zuozuoRegMsg"))) {
                sb.append(properties.getProperty("zuozuoTips"));
                regCount++;
            }

            sbtotal.append(properties.getProperty("zuozuoTips"));
        } catch (Exception e) {
            sbExcep.append(properties.getProperty("zuozuoTips"));
            e.printStackTrace();
        }


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


    }
}