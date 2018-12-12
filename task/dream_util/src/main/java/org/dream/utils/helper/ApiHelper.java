package org.dream.utils.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dream.model.order.VarietyPriceModel;
import org.dream.utils.http.HttpTools;
import org.dream.utils.prop.SpringProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.dream.model.order.VarietyPriceModel.VARIETY_PRICE;
import static org.dream.utils.date.SimpleDateFormatUtil.DATE_FORMAT2;

/**
 * Created by 12 on 2016/12/21.
 *
 * 通过http 调用其他项目的方法
 *
 *
 */
@Component
public class ApiHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ApiHelper.class);

    @Autowired
    SpringProperties springProperties;

    @Autowired
    RedisTemplate redisTemplate;


    public String getClosedTime(int exchangeId) {
        Date date = new Date();
        String now = DATE_FORMAT2.format(date);
        //判断当前开始状态
        Map<String, String> map = new HashMap<>();
        map.put("exchangeId", exchangeId + "");
        String result = HttpTools.doPost(springProperties.getProperty("sys.order.getExchangeStatus"), map);

        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = JSON.parseObject(result);

            if (json.get("newtradestartTime") != null) {
                now = DATE_FORMAT2.format(json.getDate("newtradestartTime"));
            }
        }

        LOG.info("获取该市场Id状态下的当天交易结束时间为{},访问得到的行情内容为{}", now, result);
        return now;
    }





    /***
     * 获取每个行情的价格
     *
     * @param varietyId
     * @return
     */
    public double getEachPriceByVarietyId(int varietyId) {
        double eachPrice = 0;

        try {

            //参数定义
            Map<String, String> map = new HashMap<>();


            map.put("varietyId", String.valueOf(varietyId));
            VarietyPriceModel varietyPriceModel;

            //缓存中获取
            varietyPriceModel = (VarietyPriceModel) redisTemplate.opsForHash().get(VARIETY_PRICE, varietyId);

            if (varietyPriceModel == null) {
                String message = HttpTools.doGet(springProperties.getProperty("sys.host") + springProperties.getProperty("sys.order.getEachPrice"), map);

                if (StringUtils.isEmpty(message)) {
                    return eachPrice;
                }

                varietyPriceModel = JSON.toJavaObject(JSON.parseObject(message).getJSONObject("data"), VarietyPriceModel.class);
            }

            if (varietyPriceModel == null) {
                return eachPrice;
            }

            eachPrice = varietyPriceModel.getEachPointMoney();

        } catch (Exception e) {

            LOG.warn("调用 api接口 读取varietyId,redis出现初始化异常 ", e);
        }
        return eachPrice;
    }

}
