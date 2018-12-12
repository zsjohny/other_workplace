package com.finace.miscroservice.activity.controller;


import com.finace.miscroservice.activity.service.CreditService;
import com.finace.miscroservice.activity.service.SginService;
import com.finace.miscroservice.activity.service.ShopService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 金豆商城接口:签到,获取签到的日期,用户分享送金豆
 */
@RestController
public class AppShopController extends BaseController {
    private Log logger = Log.getInstance(AppShopController.class);

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private SginService sginService;

    @Autowired
    private ShopService shopService;

    /**
     * 签到
     * <p>
     * {
     * "msg": "签到成功",
     * "code": 200,
     * "data": 240
     * }
     *
     * @return 200 签到成功
     * 400 签到失败
     * 500 今日已签到
     */
//    @RequestMapping("doSign")
    @RequestMapping("doSign/auth")

    public Response doSign() {
        String userId = getUserId();
//        String userId = "60109";
        logger.info("用户{}开始签到", userId);

        return sginService.doSign(userId);
    }


    /**
     * 获取签到的日期
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "beanVal": 240,
     * "sginNum": 1,
     * "days": [
     * "03"
     * ]
     * }
     * }
     *
     * @return
     */
//    @RequestMapping("getSginDay")
    @RequestMapping("getSginDay/auth")
    public Response getSginDay() {
        String userId = getUserId();
//        String userId = "60109";

        logger.info("用户{}获取签到的日期", userId);

        return sginService.getSginDay(userId);
    }

    /**
     * 用户分享送金豆
     * <p>
     * {
     * "msg": "分享送金豆成功",
     * "code": 200,
     * "data": {}
     * }
     *
     * @return
     */
//    @RequestMapping("shareGrantGlodBean")
    @RequestMapping("shareGrantGlodBean/auth")
    public Response shareGrantGlodBean() {
        String userId = getUserId();
//        String userId = "60109";

        logger.info("用户{}分享,开始送金豆", userId);

        String isGrant = userStrHashRedisTemplate.get(Constant.VERY_DAY_SHARE + userId + DateUtils.getNowDateStr2());
        //判断今日分享是否已近送金豆
        if (isGrant == null) {
            userStrHashRedisTemplate.set(Constant.VERY_DAY_SHARE + userId + DateUtils.getNowDateStr2(), "isSend", 1, TimeUnit.DAYS);
            return creditService.shareGrantGlodBean(userId);
        }

        return Response.errorMsg("今日分享已送金豆");
    }


    /**
     * 金豆 获得记录
     *
     * @param page 页码
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "total": 4,
     *             "pages": 1,
     *             "list": [{
     *             "id": 95534,
     *             "user_id": 0,
     *             "type_id": 0,
     *             "op": 1,
     *             "value": 130,
     *             "remark": "用户每日首次分享,奖励100金豆",
     *             "op_user": 0,
     *             "addtime": 1524884979,
     *             "total": "230"
     *             }, {
     *             "id": 95533,
     *             "user_id": 0,
     *             "type_id": 0,
     *             "op": 1,
     *             "value": 110,
     *             "remark": "连续签到2天，送20金豆",
     *             "op_user": 0,
     *             "addtime": 1524884958,
     *             "total": "130"
     *             }, {
     *             "id": 95532,
     *             "user_id": 0,
     *             "type_id": 0,
     *             "op": 1,
     *             "value": 10,
     *             "remark": "用户每日首次分享,奖励100金豆",
     *             "op_user": 0,
     *             "addtime": 1524808966,
     *             "total": "110"
     *             }, {
     *             "id": 95531,
     *             "user_id": 0,
     *             "type_id": 0,
     *             "op": 1,
     *             "value": 0,
     *             "remark": "连续签到1天，送10金豆",
     *             "op_user": 0,
     *             "addtime": 1524808841,
     *             "total": "10"
     *             }]
     *             }
     *             }
     * @return
     */
//    @RequestMapping("gold/peas/log")
    @RequestMapping("gold/peas/log/auth")
    public Response getGoldPeasLog(Integer page) {
        String userId = getUserId();

        if (page == null || page < 1) {
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问金豆获得记录", userId, ip);
        return sginService.getGoldPeasLog(page, userId);
    }

    /**
     * 金豆商城 商品列表
     *
     * @param page 页码
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "total": 1,
     *             "pages": 1,
     *             "list": [{
     *             "id": 1,
     *             "name": "商品名称",
     *             "serviceConditions": "描述内容",
     *             "pic": "http://www.baidu.com",
     *             "imgs": "www.baidu.com",
     *             "addTime": "2018-05-02 16:17:48",
     *             "endTime": 7,
     *             "type": 0,
     *             "goldPeas": 200,
     *             "count": 10,
     *             "status": 0,
     *             "number": 0,
     *             "money": 100.0
     *             }]
     *             }
     *             }
     * @return
     */
//    @RequestMapping("show/commodities")
    @RequestMapping("show/commodities/auth")
    public Response showCommodities(Integer page) {
                String userId = getUserId();
//        String userId = "60109";
        if (page == null || page < 1) {
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问金豆商城 商品列表", userId, ip);
        return shopService.showCommodities(page);
    }

    /**
     * 金豆商城首页信息
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "imgs": [{
     * "pageNum": 0,
     * "pageSize": 0,
     * "id": 70,
     * "name": "30秒带你了解网贷基础知识",
     * "type": 5,
     * "status": 1,
     * "jumurl": "http://mp.weixin.qq.com/s/GR5l43EKu7KAeAj01HBfgQ",
     * "imgurl": "https://www.etongjin.com.cn/data/banner/20180403112546d2cc.jpg",
     * "scontent": "",
     * "surl": "http://mp.weixin.qq.com/s/GR5l43EKu7KAeAj01HBfgQ",
     * "stitle": "",
     * "torder": 10,
     * "stime": "2018-04-01 11:25:28",
     * "etime": "2019-04-03 11:25:31",
     * "addtime": "2018-04-04 15:03:47"
     * }],
     * "peas": 230,
     * "count": 4
     * }
     * }
     *
     * @return
     */
//    @RequestMapping("showShopHome")
    @RequestMapping("showShopHome/auth")
    public Response showShopHome() {
        String userId = getUserId();
//        String userId = "60109";
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问金豆商城首页信息", userId, ip);
        return shopService.showShopHome(userId);
    }

    /**
     * 金豆商城商品详情
     * @param id
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "name": "商品名称",
     * "serviceConditions": "描述内容",
     * "pic": "http://www.baidu.com",
     * "imgs": "www.baidu.com",
     * "addTime": "2018-05-02 16:17:48",
     * "id": 1,
     * "endTime": 7,
     * "goldPeas": 10,
     * "count": 1,
     * "status": 1,
     * "number": 0,
     * "nid": 392,
     * "money": 100
     * }
     * }
     *
     * @return
     */
//    @RequestMapping("showCommodityDetailed")
    @RequestMapping("showCommodityDetailed/auth")
    public Response showCommodityDetailed(Integer id) {
                String userId = getUserId();
//        String userId = "60109";
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问金豆商城商品详情", userId, ip);
        return shopService.showCommodityDetailed(userId, id);
    }

    /**
     * 购买商品--兑换商品
     *
     * @param id
     * <p>
     *           {
     *           "msg": "",
     *           "code": 200,
     *           "data": {}
     *           }
     * @return
     */
//    @RequestMapping("buy/commodity")
    @RequestMapping("buy/commodity/auth")
    public Response buyCommodity(Integer id) {
                String userId = getUserId();
//        String userId = "60109";
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问购买商品", userId, ip);
        return shopService.buyCommodity(id, userId);
    }

    /**
     * 消费记录
     *
     * @param page 页码
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "total": 3,
     *             "pages": 1,
     *             "list": [
     *             {
     *             "shopId": 1,
     *             "peas": 200,
     *             "userId": 60109,
     *             "orderId": "0201805031920029511272324612990",
     *             "addTime": "2018-05-03 19:20:02",
     *             "content": "商城商品兑换，消耗200金豆"
     *             },
     *             {
     *             "shopId": 1,
     *             "peas": 10,
     *             "userId": 60109,
     *             "orderId": "0201805031922260810770889419380",
     *             "addTime": "2018-05-03 19:22:26",
     *             "content": "商城商品兑换，消耗10金豆"
     *             },
     *             {
     *             "shopId": 1,
     *             "peas": 10,
     *             "userId": 60109,
     *             "orderId": "0201805031924085337546935275420",
     *             "addTime": "2018-05-03 19:24:08",
     *             "content": "商城商品兑换，消耗10金豆"
     *             }
     *             ]
     *             }
     *             }
     * @return
     */
//    @RequestMapping("commodity/record")
    @RequestMapping("commodity/record/auth")
    public Response commodityRecord(Integer page) {
                String userId = getUserId();
//        String userId = "60109";
        if (page == null || page < 1) {
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问消费记录", userId, ip);

        return shopService.commodityRecord(page, userId);
    }

    /**
     * 兑换记录
     *
     * @param page
     * @return
     */
//    @RequestMapping("buy/record")
    @RequestMapping("buy/record/auth")
    public Response buyRecord(Integer page) {
                String userId = getUserId();
//        String userId = "60109";
        if (page == null || page < 1) {
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String ip = Iptools.gainRealIp(request);
        logger.info("用户={}，ip={} 访问兑换记录", userId, ip);
        return shopService.buyRecord(page, userId);
    }
}










