package com.reliable.util;

import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

class QueryOutside {

    public static void main(String[] args) {
        System.out.println(QueryOutside.outsideFinde("15924179757", "2010-10-20", "Ios"));
    }

    public static String outsideFinde(String phones, String partTimeStr, String operateSys) {
        JSONObject json = new JSONObject();

        String[] arr = null;

        if (phones.indexOf(",") > -1)
            arr = phones.split(",");
        else if (phones.indexOf("") > -1)
            arr = phones.split("，");
        else {
            arr = new String[] { phones };
        }

        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date partTime = sFormat.parse(partTimeStr);

            Properties productOnlineTimePro = new Properties();
            productOnlineTimePro.load(new InputStreamReader(
                    QueryOutside.class.getResourceAsStream("/productOnlineTimePro.properties"), "utf-8"));

            QueryPhoneSetAttr queryPhoneSetAttr = new QueryPhoneSetAttr();
            Map map = null;
            JSONObject jsonObject = null;
            String string = null;
            Properties properties = new Properties();

            properties.load(new InputStreamReader(
                    QueryOutside.class.getResourceAsStream("/queryphoneController.properties"), "utf-8"));

            StringBuilder sb = new StringBuilder();

            StringBuilder sbtotal = new StringBuilder();
            StringBuilder sbExcep = new StringBuilder();
            List list = new ArrayList();
            double regScore = 0.0D;
            for (String phone : arr) {
                int regCount = 0;

                if (!StringUtils.isEmpty(phone)) {
                    if (phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("tantan").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("tantan").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("tantanUrl"),
                                            new StringBuilder().append(properties.getProperty("tantanParam"))
                                                    .append(phone).append(properties.getProperty("tantanOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("tantanAttr"));

                                    jsonObject = JSONObject.fromObject(map.get("content"));
                                    if (jsonObject.get("message").equals(properties.getProperty("tantanRegMsg"))) {
                                        sb.append(properties.getProperty("tantanTips"));

                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("tantanScore"),
                                                productOnlineTimePro).doubleValue();
                                    }

                                }
                            }

                            sbtotal.append(properties.getProperty("tantanTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("tantanTips"));
                            e1.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jiqingyuehui").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("jiqingyuehui").append(operateSys).append("Time").toString())))) {
                                    HttpPost httpPost = new HttpPost(properties.getProperty("jiqingyuehuiUrl"));

                                    List params = new ArrayList();
                                    params.add(
                                            new BasicNameValuePair(properties.getProperty("jiqingyuehuiParam"), phone));
                                    httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

                                    HttpResponse response = new DefaultHttpClient().execute(httpPost);
                                    if (response.getStatusLine().getStatusCode() == 200) {
                                        String result = EntityUtils.toString(response.getEntity());
                                        if (result.equals(properties.getProperty("jiqingyuehuiRegMsg"))) {
                                            sb.append(properties.getProperty("jiqingyuehuiTips"));
                                            regCount++;
                                            regScore += CaluRatio
                                                    .calScore(productOnlineTimePro.getProperty("jiqingyuehuiScore"),
                                                            productOnlineTimePro)
                                                    .doubleValue();
                                        }
                                    }
                                }

                            }
                            sbtotal.append(properties.getProperty("jiqingyuehuiTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("jiqingyuehuiTips"));
                            e1.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("danshenjiaoyou").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("danshenjiaoyou").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("danshenjiaoyouUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("danshenjiaoyouParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("danshenjiaoyouOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("danshenjiaoyouAttr"));

                                    if (!((String) map.get("content")).equals("")) {
                                        sb.append(properties.getProperty("danshenjiaoyouTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("danshenjiaoyouScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("danshenjiaoyouTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("danshenjiaoyouTips"));
                            e1.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("xiangxiang").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("xiangxiang")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("xiangxiangUrl"),
                                        new StringBuilder().append(properties.getProperty("xiangxiangParam"))
                                                .append(phone).append(properties.getProperty("xiangxiangOtherParam"))
                                                .toString(),
                                        queryPhoneSetAttr, properties.getProperty("xiangxiangAttr"));

                                if (JSONObject.fromObject(map.get("content")).toString()
                                        .indexOf(properties.getProperty("xiangxiangRegMsg")) > 0) {
                                    sb.append(properties.getProperty("xiangxiangTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("xiangxiangScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("xiangxiangTips"));

                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("xiangxiangTips"));

                            e1.printStackTrace();
                        }
                        // try {
                        // if
                        // ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                        // new
                        // StringBuilder().append("miyue").append(operateSys).append("Time").toString())))
                        // && (partTime
                        // .before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("miyue").append(operateSys).append("Time").toString())))))
                        // {
                        // map =
                        // QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("miyueUrl"),
                        // new
                        // StringBuilder().append(properties.getProperty("miyueParam")).append(phone)
                        // .append(properties.getProperty("miyueOtherParam")).toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("miyueAttr"));
                        //
                        // if
                        // (!JSONObject.fromObject(map.get("content")).get("message")
                        // .equals(properties.getProperty("miyueRegMsg"))) {
                        // sb.append(properties.getProperty("miyueTips"));
                        // regCount++;
                        // regScore +=
                        // CaluRatio.calScore(productOnlineTimePro.getProperty("miyueScore"),
                        // productOnlineTimePro).doubleValue();
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("miyueTips"));
                        //
                        // } catch (Exception e1) {
                        // sbExcep.append(properties.getProperty("miyueTips"));
                        //
                        // e1.printStackTrace();
                        // }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("metoo").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("metoo").append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("metooUrl"),
                                        new StringBuilder().append(properties.getProperty("metooParam")).append(phone)
                                                .append(properties.getProperty("metooOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("metooAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("error")
                                        .equals(properties.getProperty("metooRegMsg"))) {
                                    sb.append(properties.getProperty("metooTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("metooScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("metooTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("metooTips"));
                            e1.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jiuxiumeinvzhibojian").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro
                                        .getProperty(new StringBuilder().append("jiuxiumeinvzhibojian")
                                                .append(operateSys).append("Time").toString())))) {
                                    String msg = QyeryPhoneByProxyIp
                                            .httpURLConnectionGET(
                                                    properties
                                                            .getProperty("jiuxiumeinvzhibojianUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("jiuxiumeinvzhibojianParam"))
                                                            .append(phone)
                                                            .append(properties
                                                                    .getProperty("jiuxiumeinvzhibojianOtherParam"))
                                                            .toString());

                                    int code = Integer.parseInt(properties.getProperty("jiuxiumeinvzhibojianRegMsg"));
                                    if (!JSONObject.fromObject(msg).get("code").equals(Integer.valueOf(code))) {
                                        sb.append(properties.getProperty("jiuxiumeinvzhibojianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("jiuxiumeinvzhibojianScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("jiuxiumeinvzhibojianTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("jiuxiumeinvzhibojianTips"));
                            e1.printStackTrace();
                        }
                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("leyuanjiaoyou").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("leyuanjiaoyou").append(operateSys).append("Time").toString()))))
                        // {
                        // map = QyeryPhoneByProxyIp
                        // .httpURLConnectionPOST(
                        // properties
                        // .getProperty("leyuanjiaoyouUrl"),
                        // new StringBuilder()
                        // .append(properties.getProperty("leyuanjiaoyouParam"))
                        // .append(phone)
                        // .append(properties.getProperty("leyuanjiaoyouOtherParam"))
                        // .toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("leyuanjiaoyouAttr"));
                        //
                        // if
                        // (!JSONObject.fromObject(map.get("content")).get("message")
                        // .equals(properties.getProperty("leyuanjiaoyouRegMsg")))
                        // {
                        // sb.append(properties.getProperty("leyuanjiaoyouTips"));
                        // regCount++;
                        // regScore += CaluRatio
                        // .calScore(productOnlineTimePro.getProperty("leyuanjiaoyouScore"),
                        // productOnlineTimePro)
                        // .doubleValue();
                        // }
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("leyuanjiaoyouTips"));
                        //
                        // } catch (Exception e1) {
                        // sbExcep.append(properties.getProperty("leyuanjiaoyouTips"));
                        // e1.printStackTrace();
                        // }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yingyue").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("yingyue").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("yingyueUrl"),
                                            new StringBuilder().append(properties.getProperty("yingyueParam"))
                                                    .append(phone).append(properties.getProperty("yingyueOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("yingyueAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("msg")
                                            .equals(properties.getProperty("yingyueRegMsg"))) {
                                        sb.append(properties.getProperty("yingyueTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("yingyueScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("yingyueTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("yingyueTips"));
                            e1.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jinriyouyue").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("jinriyouyue").append(operateSys).append("Time").toString())))) {

                                    String msg = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                            properties.getProperty("jinriyouyueUrl"),
                                            new StringBuilder().append(properties.getProperty("jinriyouyueParam"))
                                                    .append(phone)
                                                    .append(properties.getProperty("jinriyouyueOtherParam"))
                                                    .toString());

                                    if (!JSONObject.fromObject(msg).get("error_msg")
                                            .equals(properties.getProperty("jinriyouyueRegMsg"))) {
                                        sb.append(properties.getProperty("jinriyouyueTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("jinriyouyueScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }

                                }
                            }

                            sbtotal.append(properties.getProperty("jinriyouyueTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("jinriyouyueTips"));
                            e1.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jimomeinvyuehui").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("jimomeinvyuehui").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("jimomeinvyuehuiUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("jimomeinvyuehuiParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("jimomeinvyuehuiOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("jimomeinvyuehuiAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("event")
                                            .equals(properties.getProperty("jimomeinvyuehuiRegMsg"))) {
                                        sb.append(properties.getProperty("jimomeinvyuehuiTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("jimomeinvyuehuiScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("jimomeinvyuehuiTips"));
                        } catch (Exception e1) {
                            sbExcep.append(properties.getProperty("jimomeinvyuehuiTips"));
                            e1.printStackTrace();
                        }
                        //
                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder().append("youwo").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder().append("youwo").append(operateSys).append("Time").toString()))))
                        // {
                        // org.apache.commons.httpclient.HttpClient client = new
                        // org.apache.commons.httpclient.HttpClient();
                        // HttpMethod method = new GetMethod(new
                        // StringBuilder().append(properties.getProperty("youwoUrl")).append("?").append(properties.getProperty("youwoParam")).append(phone).append(properties.getProperty("youwoOtherParam")).toString());
                        //
                        // OutputStream os = new FileOutputStream(new File(new
                        // StringBuilder().append(System.getProperty("user.dir")).append("\\WebRoot\\properties\\appLoading").toString(),
                        // properties.getProperty("youwoAttr")));
                        //
                        // client.executeMethod(method);
                        // if (method.getStatusCode() == 200) {
                        // InputStream is = method.getResponseBodyAsStream();
                        // byte[] b = new byte[1024];
                        // int len = 0;
                        // while ((len = is.read(b)) != -1) {
                        // os.write(b, 0, len);
                        //
                        // if (len !=
                        // Integer.parseInt(properties.getProperty("youwoRegMsg")))
                        // {
                        // sb.append(properties.getProperty("youwoTips"));
                        // regCount++;
                        // regScore +=
                        // CaluRatio.calScore(productOnlineTimePro.getProperty("youwoScore"),
                        // productOnlineTimePro).doubleValue();
                        // }
                        // }
                        // }
                        // }
                        // }
                        // } catch (Exception e) {
                        // }

                        // -----加密过程太
                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("chengrenzhimei").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("chengrenzhimei").append(operateSys).append("Time").toString()))))
                        // {
                        // map = QyeryPhoneByProxyIp
                        // .httpURLConnectionPOST(
                        // properties
                        // .getProperty("chengrenzhimeiUrl"),
                        // new StringBuilder()
                        // .append(properties.getProperty("chengrenzhimeiParam"))
                        // .append(phone)
                        // .append(properties.getProperty("chengrenzhimeiOtherParam"))
                        // .toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("chengrenzhimeiAttr"));
                        //
                        // if
                        // (!JSONObject.fromObject(map.get("content")).get("errorInfo")
                        // .equals(properties.getProperty("chengrenzhimeiRegMsg")))
                        // {
                        // sb.append(properties.getProperty("chengrenzhimeiTips"));
                        // regCount++;
                        // regScore += CaluRatio
                        // .calScore(productOnlineTimePro.getProperty("chengrenzhimeiScore"),
                        // productOnlineTimePro)
                        // .doubleValue();
                        // }
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("chengrenzhimeiTips"));
                        //
                        // } catch (Exception e) {
                        // sbExcep.append(properties.getProperty("chengrenzhimeiTips"));
                        //
                        // e.printStackTrace();
                        // }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("baobao").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("baobao").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("baobaoUrl"),
                                            new StringBuilder().append(properties.getProperty("baobaoParam"))
                                                    .append(phone).append(properties.getProperty("baobaoOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("baobaoAttr"));

                                    if (JSONObject.fromObject(map.get("content")).toString()
                                            .indexOf(properties.getProperty("baobaoRegMsg")) > -1) {
                                        sb.append(properties.getProperty("baobaoTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("baobaoScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("baobaoTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("baobaoTips"));
                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("xingzhe").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("xingzhe").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("xingzheUrl"),
                                            new StringBuilder().append(properties.getProperty("xingzheParam"))
                                                    .append(phone).append(properties.getProperty("xingzheOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("xingzheAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("errmsg")
                                            .equals(properties.getProperty("xingzheRegMsg"))) {
                                        sb.append(properties.getProperty("xingzheTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("xingzheScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("xingzheTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("xingzheTips"));
                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("ganliao").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("ganliao").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("ganliaoUrl"),
                                            new StringBuilder().append(properties.getProperty("ganliaoParam"))
                                                    .append(phone).append(properties.getProperty("ganliaoOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("ganliaoAttr"));

                                    if ((JSONObject.fromObject(map.get("content")).get("code")
                                            .equals(Integer.valueOf(Integer
                                                    .parseInt(properties.getProperty("ganliaoRegMsg").split("_")[0]))))
                                            || (JSONObject.fromObject(map.get("content")).get("code")
                                            .equals(Integer.valueOf(Integer.parseInt(
                                                    properties.getProperty("ganliaoRegMsg").split("_")[1]))))) {
                                        sb.append(properties.getProperty("ganliaoTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("ganliaoScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("ganliaoTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("ganliaoTips"));
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("zhenqiao").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("zhenqiao").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("zhenqiaoUrl"),
                                            new StringBuilder().append(properties.getProperty("zhenqiaoParam"))
                                                    .append(phone).append(properties.getProperty("zhenqiaoOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("zhenqiaoAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("msg")
                                            .equals(properties.getProperty("zhenqiaoRegMsg"))) {
                                        sb.append(properties.getProperty("zhenqiaoTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("zhenqiaoScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("zhenqiaoTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zhenqiaoTips"));
                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("aizhenxin").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("aizhenxin").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("aizhenxinUrl"),
                                            new StringBuilder().append(properties.getProperty("aizhenxinParam"))
                                                    .append(phone).append(properties.getProperty("aizhenxinOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("aizhenxinAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("retmean")
                                            .equals(properties.getProperty("aizhenxinRegMsg"))) {
                                        sb.append(properties.getProperty("aizhenxinTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("aizhenxinScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("aizhenxinTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("aizhenxinTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro
                                    .getProperty(new StringBuilder().append("tongchengjiaoyoujianjian")
                                            .append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro
                                        .getProperty(new StringBuilder().append("tongchengjiaoyoujianjian")
                                                .append(operateSys).append("Time").toString())))) {
                                    string = QyeryPhoneByProxyIp
                                            .httpURLConnectionGET(properties.getProperty("tongchengjiaoyoujianjianUrl"),
                                                    new StringBuilder()
                                                            .append(properties
                                                                    .getProperty("tongchengjiaoyoujianjianParam"))
                                                            .append(phone)
                                                            .append(properties
                                                                    .getProperty("tongchengjiaoyoujianjianOtherParam"))
                                                            .toString());

                                    if ((!JSONObject.fromObject(string).get("message")
                                            .equals(properties.getProperty("tongchengjiaoyoujianjianRegMsg")))
                                            && (!JSONObject.fromObject(string).get("message").equals(
                                            properties.getProperty("tongchengjiaoyoujianjianRegMsg1")))) {
                                        sb.append(properties.getProperty("tongchengjiaoyoujianjianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty(
                                                        "tongchengjiaoyoujianjianScore"), productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("tongchengjiaoyoujianjianTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("tongchengjiaoyoujianjianTips"));

                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("liujianfangxiuchang").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro
                                        .getProperty(new StringBuilder().append("liujianfangxiuchang")
                                                .append(operateSys).append("Time").toString())))) {
                                    string = QyeryPhoneByProxyIp
                                            .httpURLConnectionGET(
                                                    properties
                                                            .getProperty("liujianfangxiuchangUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("liujianfangxiuchangParam"))
                                                            .append(phone)
                                                            .append(properties
                                                                    .getProperty("liujianfangxiuchangOtherParam"))
                                                            .toString());

                                    if (string.indexOf(properties.getProperty("liujianfangxiuchangRegMsg")) == -1) {
                                        sb.append(properties.getProperty("liujianfangxiuchangTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("liujianfangxiuchangScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("liujianfangxiuchangTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("liujianfangxiuchangTips"));

                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("chuyeyue").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("chuyeyue").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("chuyeyueUrl"),
                                            new StringBuilder().append(properties.getProperty("chuyeyueParam"))
                                                    .append(phone).append(properties.getProperty("chuyeyueOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("chuyeyueAttr"));

                                    if (JSONObject.fromObject(map.get("content")).get("error").toString()
                                            .indexOf(properties.getProperty("chuyeyueRegMsg")) == -1) {
                                        sb.append(properties.getProperty("chuyeyueTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("chuyeyueScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("chuyeyueTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("chuyeyueTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yuanlaihunlian").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("yuanlaihunlian").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("yuanlaihunlianUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("yuanlaihunlianParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("yuanlaihunlianOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("yuanlaihunlianAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("msg")
                                            .equals(properties.getProperty("yuanlaihunlianRegMsg"))) {
                                        sb.append(properties.getProperty("yuanlaihunlianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("yuanlaihunlianScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("yuanlaihunlianTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yuanlaihunlianTips"));

                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("baihe").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("baihe").append(operateSys).append("Time").toString())))) {
                                    String str = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                            properties.getProperty("baiheUrl"),
                                            new StringBuilder()
                                                    .append("jsonCallBack=jQuery18307090277567040175_1451103749446&l1451103760230&email=")
                                                    .append(phone).append("&_=1451103760233").toString());

                                    if (JSONObject.fromObject(str.substring(str.indexOf("{"), str.lastIndexOf(")")))
                                            .get("data").equals(properties.getProperty("baiheRegMsg"))) {
                                        sb.append(properties.getProperty("baiheTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("baiheScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("baiheTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("baiheTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("58jiaoyou").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("58jiaoyou").append(operateSys).append("Time").toString())))) {
                                    String str = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                            properties.getProperty("58jiaoyouUrl"),
                                            new StringBuilder().append(properties.getProperty("58jiaoyouParam"))
                                                    .append(phone).toString());

                                    if (str.indexOf(properties.getProperty("58jiaoyouRegMsg")) == -1) {
                                        sb.append(properties.getProperty("58jiaoyouTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("58jiaoyouScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("58jiaoyouTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("58jiaoyouTips"));

                            e.printStackTrace();
                        }
                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("hongniang").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("hongniang").append(operateSys).append("Time").toString()))))
                        // {
                        // map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                        // properties.getProperty("hongniangUrl"),
                        // new
                        // StringBuilder().append(properties.getProperty("hongniangParam"))
                        // .append(phone).append(properties.getProperty("hongniangOtherParam"))
                        // .toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("hongniangAttr"));
                        //
                        // if (!JSONObject.fromObject(map).get("content")
                        // .equals(properties.getProperty("hongniangRegMsg"))) {
                        // sb.append(properties.getProperty("hongniangTips"));
                        // regCount++;
                        // regScore += CaluRatio
                        // .calScore(productOnlineTimePro.getProperty("hongniangScore"),
                        // productOnlineTimePro)
                        // .doubleValue();
                        // }
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("hongniangTips"));
                        //
                        // } catch (Exception e) {
                        // sbExcep.append(properties.getProperty("hongniangTips"));
                        //
                        // e.printStackTrace();
                        // }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("shaige").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("shaige").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("shaigeUrl"),
                                            new StringBuilder().append(properties.getProperty("shaigeParam"))
                                                    .append(phone).append(properties.getProperty("shaigeOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("shaigeAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                    map.toString().indexOf("\"}") + 2))
                                            .get("desc").equals(properties.getProperty("shaigeRegMsg"))) {
                                        sb.append(properties.getProperty("shaigeTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("shaigeScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("shaigeTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("shaigeTips"));
                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("wozaizhaoni").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("wozaizhaoni").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("wozaizhaoniUrl"),
                                            new StringBuilder().append(properties.getProperty("wozaizhaoniParam"))
                                                    .append(phone)
                                                    .append(properties.getProperty("wozaizhaoniOtherParam")).toString(),
                                            queryPhoneSetAttr, properties.getProperty("wozaizhaoniAttr"));

                                    if (!JSONObject.fromObject(map).get("content")
                                            .equals(properties.getProperty("wozaizhaoniRegMsg"))) {
                                        sb.append(properties.getProperty("wozaizhaoniTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("wozaizhaoniScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("wozaizhaoniTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("wozaizhaoniTips"));
                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("zhongguohongniangwang").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro
                                        .getProperty(new StringBuilder().append("zhongguohongniangwang")
                                                .append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties
                                                    .getProperty("zhongguohongniangwangUrl"),
                                            new StringBuilder()
                                                    .append(properties.getProperty("zhongguohongniangwangParam"))
                                                    .append(phone)
                                                    .append(properties.getProperty("zhongguohongniangwangOtherParam"))
                                                    .append(phone).toString(),
                                            queryPhoneSetAttr, properties.getProperty("zhongguohongniangwangAttr"));

                                    if (!JSONObject.fromObject(map).get("content")
                                            .equals(properties.getProperty("zhongguohongniangwangRegMsg"))) {
                                        sb.append(properties.getProperty("zhongguohongniangwangTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(
                                                productOnlineTimePro.getProperty("zhongguohongniangwangScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("zhongguohongniangwangTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zhongguohongniangwangTips"));

                            e.printStackTrace();
                        }

                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("caime").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("caime").append(operateSys).append("Time").toString()))))
                        // {
                        // map =
                        // QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("caimeUrl"),
                        // new
                        // StringBuilder().append(properties.getProperty("caimeParam"))
                        // .append(phone).append(properties.getProperty("caimeOtherParam"))
                        // .toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("caimeAttr"));
                        //
                        // if (!JSONObject
                        // .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                        // map.toString().lastIndexOf(",")))
                        // .get("msg").equals(properties.getProperty("caimeRegMsg")))
                        // {
                        // sb.append(properties.getProperty("caimeTips"));
                        // regCount++;
                        // regScore +=
                        // CaluRatio.calScore(productOnlineTimePro.getProperty("caimeScore"),
                        // productOnlineTimePro).doubleValue();
                        // }
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("caimeTips"));
                        //
                        // } catch (Exception e) {
                        // sbExcep.append(properties.getProperty("caimeTips"));
                        //
                        // e.printStackTrace();
                        // }
                        // try {
                        // if
                        // (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("yuehuishuo").append(operateSys).append("Time").toString())))
                        // {
                        // if
                        // (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new
                        // StringBuilder()
                        // .append("yuehuishuo").append(operateSys).append("Time").toString()))))
                        // {
                        // map = QyeryPhoneByProxyIp
                        // .httpURLConnectionPOST(
                        // properties
                        // .getProperty(
                        // "yuehuishuoUrl"),
                        // new StringBuilder()
                        // .append(properties.getProperty("yuehuishuoParam"))
                        // .append(phone)
                        // .append(properties.getProperty("yuehuishuoOtherParam"))
                        // .toString(),
                        // queryPhoneSetAttr,
                        // properties.getProperty("yuehuishuoAttr"));
                        //
                        // String st =
                        // UnicodeConverChinese.decodeUnicode(((String)
                        // map.get("content"))
                        // .substring(16, ((String)
                        // map.get("content")).lastIndexOf("\"}")));
                        //
                        // if (!st.equals("用户名不存在！")) {
                        // sb.append(properties.getProperty("yuehuishuoTips"));
                        // regCount++;
                        // regScore += CaluRatio
                        // .calScore(productOnlineTimePro.getProperty("yuehuishuoScore"),
                        // productOnlineTimePro)
                        // .doubleValue();
                        // }
                        // }
                        // }
                        // sbtotal.append(properties.getProperty("yuehuishuoTips"));
                        // } catch (Exception e) {
                        // sbExcep.append(properties.getProperty("yuehuishuoTips"));
                        // e.printStackTrace();
                        // }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yuihuiba").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("yuihuiba").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("yuihuibaUrl"),
                                            new StringBuilder().append(properties.getProperty("yuihuibaParam"))
                                                    .append(phone).append(properties.getProperty("yuihuibaOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("yuihuibaAttr"));

                                    if (!JSONObject.fromObject(map).get("content")
                                            .equals(properties.getProperty("yuihuibaRegMsg"))) {
                                        sb.append(properties.getProperty("yuihuibaTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("yuihuibaScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("yuihuibaTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yuihuibaTips"));
                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("douxiuhaipi").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("douxiuhaipi").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("douxiuhaipiUrl"),
                                            new StringBuilder().append(properties.getProperty("douxiuhaipiParam"))
                                                    .append(phone)
                                                    .append(properties.getProperty("douxiuhaipiOtherParam")).toString(),
                                            queryPhoneSetAttr, properties.getProperty("douxiuhaipiAttr"));

                                    if (!JSONObject.fromObject(map.get("content")).get("msg")
                                            .equals(properties.getProperty("douxiuhaipiRegMsg"))) {
                                        sb.append(properties.getProperty("douxiuhaipiTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("douxiuhaipiScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("douxiuhaipiTips"));
                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("douxiuhaipiTips"));
                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("youxunjiaoyou").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("youxunjiaoyou").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("youxunjiaoyouUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("youxunjiaoyouParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("youxunjiaoyouOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("youxunjiaoyouAttr"));

                                    if (((Integer) JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf(":{\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("status")).intValue() != Integer
                                            .parseInt(properties.getProperty("youxunjiaoyouRegMsg"))) {
                                        sb.append(properties.getProperty("youxunjiaoyouTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("youxunjiaoyouScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("youxunjiaoyouTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("youxunjiaoyouTips"));

                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yuepengtongchengjiaoyou").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro
                                        .getProperty(new StringBuilder().append("yuepengtongchengjiaoyou")
                                                .append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties
                                                    .getProperty("yuepengtongchengjiaoyouUrl"),
                                            new StringBuilder()
                                                    .append(properties.getProperty("yuepengtongchengjiaoyouParam"))
                                                    .append(phone)
                                                    .append(properties.getProperty("yuepengtongchengjiaoyouOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("yuepengtongchengjiaoyouAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("msg")
                                            .equals(properties.getProperty("yuepengtongchengjiaoyouRegMsg"))) {
                                        sb.append(properties.getProperty("yuepengtongchengjiaoyouTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty(
                                                        "yuepengtongchengjiaoyouScore"), productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("yuepengtongchengjiaoyouTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yuepengtongchengjiaoyouTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jimodanshenyuehui").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("jimodanshenyuehui").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("jimodanshenyuehuiUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("jimodanshenyuehuiParam"))
                                                            .append(phone)
                                                            .append(properties
                                                                    .getProperty("jimodanshenyuehuiOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("jimodanshenyuehuiAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("event").equals(properties.getProperty("jimodanshenyuehuiRegMsg"))) {
                                        sb.append(properties.getProperty("jimodanshenyuehuiTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("jimodanshenyuehuiScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("jimodanshenyuehuiTips"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            sbExcep.append(properties.getProperty("jimodanshenyuehuiTips"));

                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("youyue").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("youyue").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("youyueUrl"),
                                            new StringBuilder().append(properties.getProperty("youyueParam"))
                                                    .append(phone).append(properties.getProperty("youyueOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("youyueAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("error_info").equals(properties.getProperty("youyueRegMsg"))) {
                                        sb.append(properties.getProperty("youyueTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("youyueScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("youyueTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("youyueTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("lianlian").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("lianlian").append(operateSys).append("Time").toString())))) {

                                    HttpClient httpClient = HttpClients.createDefault();

                                    HttpGet get = new HttpGet(
                                            properties.getProperty("lianlianUrl") + "?"
                                                    + new StringBuilder()
                                                    .append(properties.getProperty("lianlianParam"))
                                                    .append(phone).toString());

                                    HttpResponse execute = httpClient.execute(get);
                                    String error = EntityUtils.toString(execute.getEntity());

                                    if (!JSONObject.fromObject(error).get("error")
                                            .equals(properties.getProperty("lianlianRegMsg"))) {
                                        sb.append(properties.getProperty("lianlianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("lianlianScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("lianlianTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("lianlianTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("liangmian").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("liangmian").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("liangmianUrl"),
                                            new StringBuilder().append(properties.getProperty("liangmianParam"))
                                                    .append(phone).append(properties.getProperty("liangmianOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("liangmianAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("code_msg").equals(properties.getProperty("liangmianRegMsg"))) {
                                        sb.append(properties.getProperty("liangmianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("liangmianScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("liangmianTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("liangmianTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("tutu").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("tutu").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("tutuUrl"),
                                            new StringBuilder().append(properties.getProperty("tutuParam"))
                                                    .append(phone).append(properties.getProperty("tutuOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("tutuAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("desc").equals(properties.getProperty("tutuRegMsg"))) {
                                        sb.append(properties.getProperty("tutuTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("tutuScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("tutuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("tutuTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("zhaodaota").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("zhaodaota").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("zhaodaotaUrl"),
                                            new StringBuilder().append(properties.getProperty("zhaodaotaParam"))
                                                    .append(phone).append(properties.getProperty("zhaodaotaOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("zhaodaotaAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("msg").equals(properties.getProperty("zhaodaotaRegMsg"))) {
                                        sb.append(properties.getProperty("zhaodaotaTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("zhaodaotaScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("zhaodaotaTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zhaodaotaTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("jianjian").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("jianjian").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("jianjianUrl"),
                                            new StringBuilder().append(properties.getProperty("jianjianParam"))
                                                    .append(phone).append(properties.getProperty("jianjianOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("jianjianAttr"));

                                    if (JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("ret").equals(properties.getProperty("jianjianRegMsg"))) {
                                        sb.append(properties.getProperty("jianjianTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("jianjianScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("jianjianTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("jianjianTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("qinqin").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("qinqin").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("qinqinUrl"),
                                            new StringBuilder().append(properties.getProperty("qinqinParam"))
                                                    .append(phone).append(properties.getProperty("qinqinOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("qinqinAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                    map.toString().lastIndexOf(",")))
                                            .get("errorMsg").equals(properties.getProperty("qinqinRegMsg"))) {
                                        sb.append(properties.getProperty("qinqinTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("qinqinScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("qinqinTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("qinqinTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("moyou").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("moyou").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("moyouUrl"),
                                            new StringBuilder().append(properties.getProperty("moyouParam"))
                                                    .append(phone).append(properties.getProperty("moyouOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("moyouAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                    map.toString().lastIndexOf(",")))
                                            .get("entity").equals(properties.getProperty("moyouRegMsg"))) {
                                        sb.append(properties.getProperty("moyouTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("moyouScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("moyouTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("moyouTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("highing").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("highing").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("HighingUrl"),
                                            new StringBuilder().append(properties.getProperty("HighingParam"))
                                                    .append(phone).append(properties.getProperty("HighingOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("HighingAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                    map.toString().lastIndexOf(",")))
                                            .get("state").equals(Integer.valueOf(
                                                    Integer.parseInt(properties.getProperty("HighingRegMsg"))))) {
                                        sb.append(properties.getProperty("HighingTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("highingScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("HighingTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("HighingTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("mouling").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("mouling").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                            properties.getProperty("moulingUrl"),
                                            new StringBuilder().append(properties.getProperty("moulingParam"))
                                                    .append(phone).append(properties.getProperty("moulingOtherParam"))
                                                    .toString(),
                                            queryPhoneSetAttr, properties.getProperty("moulingAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                    map.toString().lastIndexOf(",")))
                                            .get("message").equals(properties.getProperty("moulingRegMsg"))) {
                                        sb.append(properties.getProperty("moulingTips"));
                                        regCount++;
                                        regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("moulingScore"),
                                                productOnlineTimePro).doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("moulingTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("moulingTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("yishuo").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("yishuo")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                        properties.getProperty("yishuoUrl"), new StringBuilder()
                                                .append(properties.getProperty("yishuoParam")).append(phone).toString(),
                                        queryPhoneSetAttr, properties.getProperty("yishuoAttr"));

                                if (!JSONObject
                                        .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                map.toString().lastIndexOf(",")))
                                        .get("msg").equals(properties.getProperty("yishuoRegMsg"))) {
                                    sb.append(properties.getProperty("yishuoTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("yishuoScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("yishuoTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yishuoTips"));

                            e.printStackTrace();
                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("meiyuanwang").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("meiyuanwang")
                                            .append(operateSys).append("Time").toString()))))) {
                                String str = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("meiyuanwangUrl"),
                                        new StringBuilder().append("username=").append(phone)
                                                .append("&password=879227577&fromsys=7").toString());

                                if (!JSONObject.fromObject(str).get("return_content")
                                        .equals(properties.getProperty("meiyuanwangRegMsg"))) {
                                    sb.append(properties.getProperty("meiyuanwangTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("meiyuanwangScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("meiyuanwangTips"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            sbExcep.append(properties.getProperty("meiyuanwangTips"));
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("pengyouyingxiang").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("pengyouyingxiang").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("pengyouyingxiangUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("pengyouyingxiangParam"))
                                                            .append(phone)
                                                            .append(properties
                                                                    .getProperty("pengyouyingxiangOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("pengyouyingxiangAttr"));

                                    if (!JSONObject
                                            .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                    map.toString().lastIndexOf(",")))
                                            .get("message").equals(properties.getProperty("pengyouyingxiangRegMsg"))) {
                                        sb.append(properties.getProperty("pengyouyingxiangTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("pengyouyingxiangScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("pengyouyingxiangTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("pengyouyingxiangTips"));

                            e.printStackTrace();
                        }
                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("shimingxiangqin").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("shimingxiangqin").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("shimingxiangqinUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("shimingxiangqinParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("shimingxiangqinOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("shimingxiangqinAttr"));

                                    if (map.toString().indexOf(properties.getProperty("shimingxiangqinRegMsg")) == -1) {
                                        sb.append(properties.getProperty("shimingxiangqinTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("shimingxiangqinScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("shimingxiangqinTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("shimingxiangqinTips"));

                            e.printStackTrace();
                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("heibaixiaoyuan").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("heibaixiaoyuan").append(operateSys).append("Time").toString())))) {
                                    map = QyeryPhoneByProxyIp
                                            .httpURLConnectionPOST(
                                                    properties
                                                            .getProperty("heibaixiaoyuanUrl"),
                                                    new StringBuilder()
                                                            .append(properties.getProperty("heibaixiaoyuanParam"))
                                                            .append(phone)
                                                            .append(properties.getProperty("heibaixiaoyuanOtherParam"))
                                                            .toString(),
                                                    queryPhoneSetAttr, properties.getProperty("heibaixiaoyuanAttr"));

                                    String string2 = (String) JSONObject.fromObject(map.toString()
                                            .substring(map.toString().indexOf("{\""), map.toString().lastIndexOf(",")))
                                            .get("msg");

                                    if ((!string2.equals(properties.getProperty("heibaixiaoyuanRegMsg")))
                                            && (!string2.equals(properties.getProperty("heibaixiaoyuanRegMsg1")))) {
                                        sb.append(properties.getProperty("heibaixiaoyuanTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("heibaixiaoyuanScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }
                            }
                            sbtotal.append(properties.getProperty("heibaixiaoyuanTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("heibaixiaoyuanTips"));

                            e.printStackTrace();
                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("zeze").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("zeze").append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("zezeUrl"),
                                        new StringBuilder().append(properties.getProperty("zezeParam")).append(phone)
                                                .append(properties.getProperty("zezeOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("zezeAttr"));

                                if (!JSONObject
                                        .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                map.toString().lastIndexOf(",")))
                                        .get("code").equals(Integer
                                                .valueOf(Integer.parseInt(properties.getProperty("zezeRegMsg"))))) {
                                    sb.append(properties.getProperty("zezeTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("zezeScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("zezeTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zezeTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("meihu").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("meihu").append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("meihuUrl"),
                                        new StringBuilder().append(properties.getProperty("meihuParam")).append(phone)
                                                .append(properties.getProperty("meihuOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("meihuAttr"));

                                if (!JSONObject
                                        .fromObject(map.toString().substring(map.toString().indexOf("{\""),
                                                map.toString().lastIndexOf(",")))
                                        .get("event").equals(properties.getProperty("meihuRegMsg"))) {
                                    sb.append(properties.getProperty("meihuTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("meihuScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("meihuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("meihuTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("yazhai").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("yazhai")
                                            .append(operateSys).append("Time").toString()))))) {
                                String str = QyeryPhoneByProxyIp
                                        .httpURLConnectionGET(properties.getProperty("yazhaiUrl"),
                                                new StringBuilder().append(properties.getProperty("yazhaiParam"))
                                                        .append(phone)
                                                        .append(properties.getProperty("yazhaiOtherParam")).toString());

                                if (str.equals(properties.getProperty("yazhaiRegMsg"))) {
                                    sb.append(properties.getProperty("yazhaiTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("yazhaiScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("yazhaiTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yazhaiTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("huoliao").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("huoliao")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("huoliaoUrl"),
                                        new StringBuilder().append(properties.getProperty("huoliaoParam")).append(phone)
                                                .append(properties.getProperty("huoliaoOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("huoliaoAttr"));

                                if (!JSONObject
                                        .fromObject(map.toString().substring(map.toString().indexOf("={\"") + 1,
                                                map.toString().lastIndexOf(",")))
                                        .get("errorMsg").equals(properties.getProperty("huoliaoRegMsg"))) {
                                    sb.append(properties.getProperty("huoliaoTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("huoliaoScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("huoliaoTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("huoliaoTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yingying").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("yingying")
                                            .append(operateSys).append("Time").toString()))))) {
                                String str = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("yingyingUrl"),
                                        new StringBuilder().append(properties.getProperty("yingyingParam"))
                                                .append(phone).append(properties.getProperty("yingyingOtherParam"))
                                                .toString());

                                if (!JSONObject.fromObject(str).get("code")
                                        .equals(properties.getProperty("yingyingRegMsg"))) {
                                    sb.append(properties.getProperty("yingyingTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("yingyingScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("yingyingTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yingyingTips"));

                            e.printStackTrace();
                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("miaotu").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("miaotu")
                                            .append(operateSys).append("Time").toString()))))) {
                                string = QyeryPhoneByProxyIp.httpURLConnectionGET(properties.getProperty("miaotuUrl"),
                                        new StringBuilder().append(properties.getProperty("miaotuParam")).append(phone)
                                                .toString());

                                if (!JSONObject.fromObject(string).get("Msg")
                                        .equals(properties.getProperty("miaotuRegMsg"))) {
                                    sb.append(properties.getProperty("miaotuTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("miaotuScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("miaotuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("miaotuTips"));

                            e.printStackTrace();
                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("bolatu").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("bolatu")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("bolatuUrl"),
                                        new StringBuilder().append(properties.getProperty("bolatuParam")).append(phone)
                                                .append(properties.getProperty("bolatuOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("bolatuAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("bolatuRegMsg"))) {
                                    sb.append(properties.getProperty("bolatuTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("bolatuScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("bolatuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("bolatuTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("memezhibo").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("memezhibo")
                                            .append(operateSys).append("Time").toString()))))) {
                                string = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("memezhiboUrl"),
                                        new StringBuilder().append(properties.getProperty("memezhiboParam"))
                                                .append(phone).toString());

                                if (JSONObject.fromObject(string).get("code").toString()
                                        .equals(properties.getProperty("memezhiboRegMsg"))) {
                                    sb.append(properties.getProperty("memezhiboTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("memezhiboScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("bolatuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("bolatuTips"));

                            e.printStackTrace();
                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("youqu").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("youqu").append(operateSys).append("Time").toString()))))) {
                                org.apache.http.client.HttpClient httpClient = HttpClients.createDefault();
                                HttpPost httpPost = new HttpPost(properties.getProperty("youquUrl"));
                                JSONObject jso = new JSONObject();
                                jso.put("UniqueIdentifier", phone);
                                jso.put("PassWord", "nzksms");
                                jso.put("APPMark", "com.wenweikj.www");
                                jso.put("DeviceCode", properties.getProperty("youquParam"));

                                StringEntity entity = new StringEntity(jso.toString(), "utf-8");
                                entity.setContentEncoding("UTF-8");
                                entity.setContentType("application/json");
                                httpPost.setEntity(entity);
                                httpPost.setHeader("Authorization", properties.getProperty("youquOtherParam"));
                                HttpResponse response = httpClient.execute(httpPost);
                                HttpEntity httpEntity = response.getEntity();
                                jso = JSONObject.fromObject(EntityUtils.toString(httpEntity, "utf-8"));
                                if (!jso.get("Remark").equals(properties.getProperty("youquRegMsg"))) {
                                    sb.append(properties.getProperty("youquTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("youquScore"),
                                            productOnlineTimePro).doubleValue();
                                }
                            }
                            sbtotal.append(properties.getProperty("youquTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("youquTips"));

                        }

                        try {
                            if (StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("fanxingzhibo").append(operateSys).append("Time").toString()))) {
                                if (partTime.before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                        .append("fanxingzhibo").append(operateSys).append("Time").toString())))) {
                                    org.apache.http.client.HttpClient httpClient = HttpClients.createDefault();
                                    HttpPost httpPost = new HttpPost(properties.getProperty("fanxingzhiboUrl"));
                                    JSONObject jso = new JSONObject();
                                    jso.put("clienttime", properties.getProperty("fanxingzhiboTime"));
                                    jso.put("p", properties.getProperty("fanxingzhiboParam"));
                                    jso.put("mid", "b997523eada19eb9d02d84b859b0b00f8cb104f8");
                                    jso.put("clientver", "2808");
                                    jso.put("username", phone);
                                    jso.put("key", properties.getProperty("fanxingzhiboOtherParam"));
                                    jso.put("appid", "1131");
                                    StringEntity entity = new StringEntity(jso.toString(), "utf-8");
                                    entity.setContentEncoding("UTF-8");
                                    entity.setContentType("application/json");

                                    httpPost.setEntity(entity);
                                    HttpResponse response = httpClient.execute(httpPost);
                                    HttpEntity httpEntity = response.getEntity();
                                    jso = JSONObject.fromObject(EntityUtils.toString(httpEntity, "utf-8"));

                                    if (jso.get("error_code").toString()
                                            .equals(properties.getProperty("fanxingzhiboRegMsg"))) {
                                        sb.append(properties.getProperty("fanxingzhiboTips"));
                                        regCount++;
                                        regScore += CaluRatio
                                                .calScore(productOnlineTimePro.getProperty("fanxingzhiboScore"),
                                                        productOnlineTimePro)
                                                .doubleValue();
                                    }
                                }

                            }
                            sbtotal.append(properties.getProperty("fanxingzhiboTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("fanxingzhiboTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("juweimi").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("juweimi")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("juweimiUrl"),
                                        new StringBuilder().append(properties.getProperty("juweimiParam")).append(phone)
                                                .append(properties.getProperty("juweimiOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("juweimiAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("juweimiRegMsg"))) {
                                    sb.append(properties.getProperty("juweimiTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("juweimiScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("juweimiTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("juweimiTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("huainanhai").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("huainanhai")
                                            .append(operateSys).append("Time").toString()))))) {
                                String msg = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("huainanhaiUrl"),
                                        new StringBuilder().append(properties.getProperty("huainanhaiParam"))
                                                .append(phone).append(properties.getProperty("huainanhaiOtherParam"))
                                                .toString());

                                if (!String.valueOf(JSONObject.fromObject(msg).get("rtn"))
                                        .equals(properties.getProperty("huainanhaiRegMsg"))) {
                                    sb.append(properties.getProperty("huainanhaiTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("huainanhaiScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("huainanhaiTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("huainanhaiTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("haixiu").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("haixiu")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("haixiuUrl"),
                                        new StringBuilder().append(properties.getProperty("haixiuParam")).append(phone)
                                                .append(properties.getProperty("haixiuOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("haixiuAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("message")
                                        .equals(properties.getProperty("haixiuRegMsg"))) {
                                    sb.append(properties.getProperty("haixiuTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("haixiuScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("haixiuTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("haixiuTips"));

                        }
                        // 短信--
                        // try {
                        // if
                        // ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                        // new
                        // StringBuilder().append("fuliao").append(operateSys).append("Time").toString())))
                        // && (partTime.before(sFormat
                        // .parse(productOnlineTimePro.getProperty(new
                        // StringBuilder().append("fuliao")
                        // .append(operateSys).append("Time").toString()))))) {
                        // String msg = QyeryPhoneByProxyIp
                        // .httpURLConnectionGET(properties.getProperty("fuliaoUrl"),
                        // new
                        // StringBuilder().append(properties.getProperty("fuliaoParam"))
                        // .append(phone)
                        // .append(properties.getProperty("fuliaoOtherParam")).toString());
                        //
                        // if
                        // (!String.valueOf(JSONObject.fromObject(msg).get("result"))
                        // .equals(properties.getProperty("fuliaoRegMsg"))) {
                        // sb.append(properties.getProperty("fuliaoTips"));
                        // regCount++;
                        // regScore +=
                        // CaluRatio.calScore(productOnlineTimePro.getProperty("fuliaoScore"),
                        // productOnlineTimePro).doubleValue();
                        // }
                        //
                        // }
                        // sbtotal.append(properties.getProperty("fuliaoTips"));
                        //
                        // } catch (Exception e) {
                        // sbExcep.append(properties.getProperty("fuliaoTips"));
                        //
                        // }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("zhenaiwang").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("zhenaiwang")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("zhenaiwangUrl"),
                                        new StringBuilder().append(properties.getProperty("zhenaiwangParam"))
                                                .append(phone).append(properties.getProperty("zhenaiwangOtherParam"))
                                                .toString(),
                                        queryPhoneSetAttr, properties.getProperty("zhenaiwangAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("zhenaiwangRegMsg"))) {
                                    sb.append(properties.getProperty("zhenaiwangTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("zhenaiwangScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("zhenaiwangTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zhenaiwangTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("lianaijun").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("lianaijun")
                                            .append(operateSys).append("Time").toString()))))) {

                                HttpClient client = HttpClients.createDefault();

                                HttpPost post = new HttpPost(properties.getProperty("lianaijunUrl"));
                                JSONObject jso = new JSONObject();
                                JSONObject js = new JSONObject();
                                js.put("@pwd", "fguuiii");
                                js.put("@phone", phone);
                                js.put("@version", "1.2");
                                js.put("@os", "IOS");
                                jso.put("params", js.toString());

                                StringEntity stringEntity = new StringEntity(jso.toString());
                                stringEntity.setContentEncoding("utf-8");
                                stringEntity.setContentType("application/json");

                                post.setEntity(stringEntity);
                                HttpResponse execute = client.execute(post);

                                String msg = EntityUtils.toString(execute.getEntity());

                                if (!JSONObject.fromObject(JSONArray.fromObject(msg).get(0)).get("msg")
                                        .equals(properties.getProperty("lianaijunRegMsg"))) {
                                    sb.append(properties.getProperty("lianaijunTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("lianaijunScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("lianaijunTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("lianaijunTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("xiaowo").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("xiaowo")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("xiaowoUrl"),
                                        new StringBuilder().append(properties.getProperty("xiaowoParam")).append(phone)
                                                .append(properties.getProperty("xiaowoOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("xiaowoAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("xiaowoRegMsg"))) {
                                    sb.append(properties.getProperty("xiaowoTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("xiaowoScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("xiaowoTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("xiaowoTips"));

                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("feichengwurao").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("feichengwurao")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp
                                        .httpURLConnectionPOST(
                                                properties
                                                        .getProperty(
                                                                "feichengwuraoUrl"),
                                                new StringBuilder().append(properties.getProperty("feichengwuraoParam"))
                                                        .append(phone)
                                                        .append(properties.getProperty("feichengwuraoOtherParam"))
                                                        .append(System.currentTimeMillis()).toString(),
                                                queryPhoneSetAttr, properties.getProperty("feichengwuraoAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("return_msg")
                                        .equals(properties.getProperty("feichengwuraoRegMsg"))) {
                                    sb.append(properties.getProperty("feichengwuraoTips"));
                                    regCount++;
                                    regScore += CaluRatio
                                            .calScore(productOnlineTimePro.getProperty("feichengwuraoScore"),
                                                    productOnlineTimePro)
                                            .doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("feichengwuraoTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("feichengwuraoTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("zhaodaota").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("zhaodaota")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp
                                        .httpURLConnectionPOST(
                                                properties
                                                        .getProperty(
                                                                "zhaodaotaUrl"),
                                                new StringBuilder().append(properties.getProperty("zhaodaotaParam"))
                                                        .append(phone)
                                                        .append(properties.getProperty("zhaodaotaOtherParam"))
                                                        .append(System.currentTimeMillis()).toString(),
                                                queryPhoneSetAttr, properties.getProperty("zhaodaotaAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("zhaodaotaRegMsg"))) {
                                    sb.append(properties.getProperty("zhaodaotaTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("zhaodaotaScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("zhaodaotaTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("zhaodaotaTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("yangmeizi").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("yangmeizi")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp
                                        .httpURLConnectionPOST(
                                                properties
                                                        .getProperty(
                                                                "yangmeiziUrl"),
                                                new StringBuilder().append(properties.getProperty("yangmeiziParam"))
                                                        .append(phone)
                                                        .append(properties.getProperty("yangmeiziOtherParam"))
                                                        .append(System.currentTimeMillis()).toString(),
                                                queryPhoneSetAttr, properties.getProperty("yangmeiziAttr"));

                                if (!String.valueOf(JSONObject.fromObject(map.get("content")).get("status"))
                                        .equals(properties.getProperty("yangmeiziRegMsg"))) {
                                    sb.append(properties.getProperty("yangmeiziTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("yangmeiziScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("yangmeiziTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("yangmeiziTips"));

                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("liangliang").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("liangliang")
                                            .append(operateSys).append("Time").toString()))))) {

                                HttpClient client = HttpClients.createDefault();

                                HttpPost post = new HttpPost(properties.getProperty("liangliangUrl"));
                                JSONObject jso = new JSONObject();

                                jso.put("mobile", phone);
                                jso.put("hashedPassword", "a90a8c7f9db3db61bcd07cb80a45953fec7e2d12");

                                StringEntity stringEntity = new StringEntity(jso.toString());
                                stringEntity.setContentEncoding("utf-8");
                                stringEntity.setContentType("application/json");

                                post.setEntity(stringEntity);
                                post.setHeader("AppSign", "82ff106c5a7b64931c82693b09a448705af4cfc0");
                                post.setHeader("AppVersion", "YopperPro-2.5.3-ios-9.3.2-App Store");
                                post.setHeader("ClientId", "292ae0aedbe848e0a7f7cc43853c36ba");
                                post.setHeader("RequestId", "0f7418c1b1a5459583c2b05f9de0ef2b");
                                post.setHeader("Timestamp", "1464532659.993694");
                                HttpResponse execute = client.execute(post);

                                String msg = EntityUtils.toString(execute.getEntity());

                                if (!JSONObject.fromObject(msg).get("msg")
                                        .equals(properties.getProperty("liangliangRegMsg"))) {
                                    sb.append(properties.getProperty("liangliangTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("liangliangScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("liangliangTips"));

                        } catch (Exception e) {

                            sbExcep.append(properties.getProperty("liangliangTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("youyou").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat
                                    .parse(productOnlineTimePro.getProperty(new StringBuilder().append("youyou")
                                            .append(operateSys).append("Time").toString()))))) {
                                String msg = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("youyouUrl"),
                                        new StringBuilder().append(properties.getProperty("youyouParam")).append(phone)
                                                .toString());

                                if (!JSONObject
                                        .fromObject(
                                                JSONArray.fromObject(JSONObject.fromObject(msg).get("errors")).get(0))
                                        .get("msg").equals(properties.getProperty("youyouRegMsg"))) {
                                    sb.append(properties.getProperty("youyouTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("youyouScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("youyouTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("youyouTips"));

                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("laile").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("laile").append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("laileUrl"),
                                        new StringBuilder().append(properties.getProperty("laileParam")).append(phone)
                                                .append(properties.getProperty("laileOtherParam")).toString(),
                                        queryPhoneSetAttr, properties.getProperty("laileAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("errorInfo")
                                        .equals(properties.getProperty("laileRegMsg"))) {
                                    sb.append(properties.getProperty("laileTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("laileScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("laileTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("laileTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("qingchifan").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("qingchifan")
                                            .append(operateSys).append("Time").toString()))))) {

                                HttpClient client = HttpClients.custom()
                                        .setUserAgent("QingChiFan/2.8.6 CFNetwork/758.4.3Darwin/15.5.0").build();

                                HttpPost post = new HttpPost(properties.getProperty("qingchifanUrl"));

                                List<BasicNameValuePair> listName = new ArrayList<>();

                                listName.add(new BasicNameValuePair("oauth_signature", "x8+CCvWqj4/wIJI2yj4mgRJ9kiY="));
                                listName.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
                                listName.add(
                                        new BasicNameValuePair("oauth_nonce", "F07875FA-9CC6-47EA-9F3E-FD7C5D6B265C"));
                                listName.add(new BasicNameValuePair("x_auth_password", "dmjsns"));
                                listName.add(new BasicNameValuePair("x_auth_model", "client_auth"));
                                listName.add(new BasicNameValuePair("apiVersion", "2.8.0"));
                                listName.add(new BasicNameValuePair("oauth_timestamp", "1464535023"));
                                listName.add(new BasicNameValuePair("oauth_version", "1.0"));
                                listName.add(new BasicNameValuePair("source", "028fa5cddf7e5130dfd35344299ffbba"));
                                listName.add(new BasicNameValuePair("oauth_consumer_key",
                                        "028fa5cddf7e5130dfd35344299ffbba"));

                                listName.add(new BasicNameValuePair("x_auth_username", "+86" + phone));

                                post.setEntity(new UrlEncodedFormEntity(listName));
                                HttpResponse execute = client.execute(post);

                                post.setHeader("Accept-Encoding", "gzip, deflate");
                                String msg = EntityUtils.toString(execute.getEntity(), "utf-8");

                                if (!JSONObject.fromObject(msg).get("info")
                                        .equals(properties.getProperty("qingchifanRegMsg"))) {
                                    sb.append(properties.getProperty("qingchifanTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("qingchifanScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("qingchifanTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("qingchifanTips"));

                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(
                                    new StringBuilder().append("kaomi").append(operateSys).append("Time").toString())))
                                    && (partTime
                                    .before(sFormat.parse(productOnlineTimePro.getProperty(new StringBuilder()
                                            .append("kaomi").append(operateSys).append("Time").toString()))))) {
                                String msg = QyeryPhoneByProxyIp
                                        .httpURLConnectionGET(properties.getProperty("kaomiUrl"),
                                                new StringBuilder().append(properties.getProperty("kaomiParam"))
                                                        .append(phone).append(properties.getProperty("kaomiOtherParam"))
                                                        .toString());

                                if (!JSONObject.fromObject(msg).get("event")
                                        .equals(properties.getProperty("kaomiRegMsg"))) {
                                    sb.append(properties.getProperty("kaomiTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("kaomiScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("kaomiTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("kaomiTips"));

                        }
                        // ---参数签名有些问题
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("chongwoba").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("chongwoba")
                                            .append(operateSys).append("Time").toString()))))) {
                                String msg = QyeryPhoneByProxyIp.httpURLConnectionGET(
                                        properties.getProperty("chongwobaUrl"),
                                        new StringBuilder().append(properties.getProperty("chongwobaParam"))
                                                .append(phone).append(properties.getProperty("chongwobaOtherParam"))
                                                .toString());

                                if (String.valueOf(JSONObject.fromObject(msg).get("Code"))
                                        .equals(properties.getProperty("chongwobaRegMsg"))) {
                                    sb.append(properties.getProperty("chongwobaTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("chongwobaScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("chongwobaTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("chongwobaTips"));

                        }
                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("sinianti").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(
                                    productOnlineTimePro.getProperty(new StringBuilder().append("sinianti")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(properties.getProperty("siniantiUrl"),
                                        new StringBuilder().append(properties.getProperty("siniantiParam"))
                                                .append(properties.getProperty("siniantiOtherParam")).append(phone)
                                                .toString(),
                                        queryPhoneSetAttr, properties.getProperty("siniantiAttr"));

                                if (!JSONObject.fromObject(map.get("content")).get("msg")
                                        .equals(properties.getProperty("siniantiRegMsg"))) {
                                    sb.append(properties.getProperty("siniantiTips"));
                                    regCount++;
                                    regScore += CaluRatio.calScore(productOnlineTimePro.getProperty("siniantiScore"),
                                            productOnlineTimePro).doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("siniantiTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("siniantiTips"));

                        }

                        try {
                            if ((StringUtils.isNotEmpty(productOnlineTimePro.getProperty(new StringBuilder()
                                    .append("peinikandianying").append(operateSys).append("Time").toString())))
                                    && (partTime.before(sFormat.parse(productOnlineTimePro
                                    .getProperty(new StringBuilder().append("peinikandianying")
                                            .append(operateSys).append("Time").toString()))))) {
                                map = QyeryPhoneByProxyIp.httpURLConnectionPOST(
                                        properties.getProperty("peinikandianyingUrl"),
                                        new StringBuilder().append(properties.getProperty("peinikandianyingParam"))
                                                .append(properties.getProperty("peinikandianyingOtherParam"))
                                                .append(phone).toString(),
                                        queryPhoneSetAttr, properties.getProperty("peinikandianyingAttr"));

                                if (JSONObject.fromObject(map.get("content")).get("status")
                                        .equals(properties.getProperty("peinikandianyingRegMsg"))) {
                                    sb.append(properties.getProperty("peinikandianyingTips"));
                                    regCount++;
                                    regScore += CaluRatio
                                            .calScore(productOnlineTimePro.getProperty("peinikandianyingScore"),
                                                    productOnlineTimePro)
                                            .doubleValue();
                                }

                            }
                            sbtotal.append(properties.getProperty("peinikandianyingTips"));

                        } catch (Exception e) {
                            sbExcep.append(properties.getProperty("peinikandianyingTips"));

                        }
                        list.add(Integer.valueOf(regCount));

                    }
                }

            }
            //
            // if (list.size() == 0) {
            // regTotal = 0.00
            // }

            System.out.println(sbtotal);

            String result = new StringBuilder().append(productOnlineTimePro.getProperty("appTips"))
                    .append(new DecimalFormat("#0.00").format(regScore)).toString();

            json.put("status", result);

            return result;
        } catch (Exception e) {
        }

        return "";
    }
}