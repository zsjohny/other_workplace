package com.e_commerce.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.activity.dao.CityPartnerDao;
import com.e_commerce.miscroservice.activity.service.CityPartnerService;
import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
@Service
public class CityPartnerServiceImpl implements CityPartnerService {
    private Log logger = Log.getInstance(CityPartnerServiceImpl.class);

    @Resource
    private CityPartnerDao cityPartnerDao;

    /**
     * 添加城市合伙人
     *
     * @param name     姓名
     * @param phone    手机
     * @param province 省
     * @param city     市
     * @param district 区
     * @return
     */
    @Override
    public Response insertCityPartner(String name, String phone, String province, String city, String district) {
        logger.info("添加城市合伙人：name={},phone={},province={},name={},name={},name={}", name, name, name, name, name, name);
        if (StringUtils.isAnyEmpty(name, phone, province, city, district)) {
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        CityPartner cityPartner = cityPartnerDao.findCityPartner(phone);
        if (cityPartner != null) {
            logger.warn("该用户已添加");
            return Response.errorMsg("无需重复添加");
        }
        cityPartnerDao.insertCityPartner(name, phone, province, city, district);
        rebortNotify(name, province + city + district);

        return Response.success();
    }

    /**
     * //TODO:wait
     * 钉钉提醒,后期弄成调度任务中
     *
     * @param name
     * @param area
     */
    private void rebortNotify(String name, String area) {
        try {

            String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=cd56d1ca5e457fd77fb3cf44897f710deced4b3dfc6b75f55d121039499e780d";

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

//
//        JSONObject json = new JSONObject();
//        json.put("msgtype", "text");
//        JSONObject text = new JSONObject();
//        text.put("content", "我就是我, 是不一样的烟火");
//        json.put("text", text);
//        JSONObject at = new JSONObject();
//        JSONArray array = new JSONArray();
//        array.add("13291859481");
//        at.put("atMobiles", array);
//        at.put("isAtAll", Boolean.FALSE);
//        json.put("at", at.toJSONString());


            JSONObject json = new JSONObject();
            json.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.put("title", "代理商报名提醒");
            markdown.put("text", String.format("#### 姓名:**%s** \n" +
                    "#### 地区:*%s*\n" +
                    "## 报名成功", name, area));
            json.put("markdown", markdown);
            JSONObject at = new JSONObject();
            JSONArray array = new JSONArray();
            array.add("15158934928");
            at.put("atMobiles", array);
            at.put("isAtAll", Boolean.FALSE);
            json.put("at", at.toJSONString());


            StringEntity se = new StringEntity(json.toJSONString(), "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
