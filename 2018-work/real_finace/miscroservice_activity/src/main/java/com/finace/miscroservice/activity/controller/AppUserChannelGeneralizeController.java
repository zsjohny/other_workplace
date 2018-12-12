package com.finace.miscroservice.activity.controller;

import com.finace.miscroservice.activity.service.UserChannelGeneralizeService;
import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 渠道推广接口
 */
@InnerRestController
public class AppUserChannelGeneralizeController {
    Log logger = Log.getInstance(AppUserChannelGeneralizeController.class);
    @Autowired
    private UserChannelGeneralizeService userChannelGeneralizeService;

    /**
     * 渠道推广保存信息接口
     * Seconds to earn
     * @param appid 广告主推广app的Appstore ID
     * @param idfa 用户的IDFA
     * @param keywords 关键词
     * @param channel 渠道名称 miaozhuan
     * @param timestamp 时间戳 精确到秒
     * @param callback 回调
     * @param phone 区分是ios还是android：ios android
     * @return
     */
    @RequestMapping(value = "click", method = RequestMethod.GET)
    public Map<String,String> click(String appid, String idfa, String keywords, String channel, String timestamp, String callback, String phone) {
        logger.info("渠道推广点击保存idfa={},channel={},keywords={},callback={},phone={}",idfa,channel,keywords,callback,phone);
        return userChannelGeneralizeService.insertUserChannelGeneralize(appid,idfa,keywords,channel,timestamp,callback,phone);

    }


    /**
     * 根据 phone：android|ios 和 idfa（android为imei） 查询 渠道推广
     * @param idfa （android为imei） 查询 渠道推广 格 式 ：CA944F0B-3876-4F23-8817-9890E1E30035,9A98C598+39A3+4AFB+818 3+180ACB6569D2,...(英文逗号分隔)
     * @param phone android|ios
     * @return
     */
    @RequestMapping(value = "distinct", method = RequestMethod.GET)
    public Map<String,String> distinct(String idfa, String phone) {
        logger.info("排重接口 idfa={},phone",idfa,phone);
        return userChannelGeneralizeService.findUserChannelGeneralize(idfa,phone);
    }

//    @RequestMapping("test")
//    public void test(Integer pageNum){
//        userChannelGeneralizeService.test(pageNum);
//    }



}
