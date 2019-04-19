package com.e_commerce.miscroservice.operate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.activity.Coupon;
import com.e_commerce.miscroservice.commons.entity.application.activity.CouponTemplate;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.DecimalUtils;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.dao.CouponDao;
import com.e_commerce.miscroservice.operate.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.operate.dao.ShopMemberOrderDao;
import com.e_commerce.miscroservice.operate.dao.ShopMemberOrderItemDao;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberOrderMapper;
import com.e_commerce.miscroservice.operate.mapper.StoreBusinessMapper;
import com.e_commerce.miscroservice.operate.service.distributionSystem.DistributionSystemService;
import com.e_commerce.miscroservice.operate.service.order.ShopMemberOrderService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 18:54
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberOrderServiceImpl implements ShopMemberOrderService{

    private Log logger = Log.getInstance (ShopMemberOrderServiceImpl.class);

    @Autowired
    private DistributionSystemService distributionSystemService;
    @Autowired
    private ShopMemberAccountCashOutInDao shopMemberAccountCashOutInDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private ShopMemberOrderDao shopMemberOrderDao;
    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    @Autowired
    private ShopMemberOrderItemDao shopMemberOrderItemDao;



    /**
     * 小程序订单查询
     *
     * @param query query
     * @author Charlie
     * @date 2018/11/8 19:12
     */
    @Override
    public Map<String, Object> listOrder(ShopMemberOrderQuery query) {
        Map<String, Object> result = new HashMap<> (2);
        PageInfo<Map<String, Object>> page = shopMemberOrderDao.listOrderPage (query);
        result.put ("dataList", page);
        return result;
    }


    /**
     * 订单详情
     *
     * @param orderNo 订单编号
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 19:50
     */
    @Override
    public ShopMemberOrderQuery orderDetailByOrderNo(String orderNo) {
        if (StringUtils.isBlank (orderNo)) {
            return null;
        }

        //查询订单
        ShopMemberOrderQuery orderQuery = new ShopMemberOrderQuery ();
        orderQuery.setOrderNumber (orderNo);
        ShopMemberOrderQuery order = shopMemberOrderMapper.selectOne (orderQuery);
        logger.info ("查询订单详情 orderId={}, order={}", orderNo, order);
        if (BeanKit.hasNull (order)) {
            return null;
        }


        //门店信息
        StoreBusinessVo storeBusiness = storeBusinessMapper.selectByPrimaryKey (order.getStoreId ());
        ErrorHelper.declareNull (storeBusiness, "未找到门店信息");
        order.setStoreName (storeBusiness.getBusinessName ());

        //订单商品信息
        List<ShopMemberOrderItemQuery> orderItemList = shopMemberOrderItemDao.findProductInfoByOrderNo (order.getOrderNumber ());
        order.setShopMemberOrderItemList (orderItemList);


        //实付金额,应付金额,实付金币,金币现金汇率 (当前没有使用金币的, 数据写死,留个口子,到时候再加新字段!!!!)
        order.setShouldBePayMoney (order.getPayMoney ());
        order.setRealPayGoldCoin (BigDecimal.ZERO);
        order.setRealPayMoney (order.getPayMoney ());
        order.setCashGoldCoinRate ("");

        //格式化日期
        order.setCreateTimeStr (TimeUtils.longFormatString (order.getCreateTime ()));
        order.setUpdateTimeStr (TimeUtils.longFormatString (order.getUpdateTime ()));
        order.setDeliveryTimeStr (TimeUtils.longFormatString (order.getDeliveryTime ()));
        order.setOrderStopTimeStr (TimeUtils.longFormatString (order.getOrderStopTime ()));
        order.setOrderFinishTimeStr (TimeUtils.longFormatString (order.getOrderFinishTime ()));
        order.setTakeDeliveryTimeStr (TimeUtils.longFormatString (order.getTakeDeliveryTime ()));

        String expressInfo = order.getExpressInfo ();
        if (StringUtils.isNotBlank (expressInfo)) {
            JSONObject jsonObject = JSON.parseObject (expressInfo);

            JSONObject result = (JSONObject) jsonObject.get ("result");
            order.setExpressInfoCom (result.get ("com").toString ());
            order.setExpressInfoNo (result.get ("no").toString ());
            order.setExpressInfoCompany (result.get ("company").toString ());

            order.setExpressInfoList (parseJson (jsonObject));

            //就不需要物流信息字段的显示
            order.setExpressInfo ("");
        }


        //查询优惠券
        Coupon oneCoupon = couponDao.findCouponByOrderNo (order.getOrderNumber (), 1);
        logger.info ("查询优惠券 coupon={}", oneCoupon);
        if (oneCoupon != null) {
            CouponTemplate oneTemp = couponDao.findTemplateById (oneCoupon.getTemplateId ());
            if (oneTemp != null) {
                if (ObjectUtils.nullSafeEquals (oneTemp.getPlatformType (), 0)) {
                    order.setPlatformCoupon (order.getSaleMoney ());
                }
                else {
                    order.setBusinessCoupon (order.getSaleMoney ());
                }
            }

        }


        //查询收益
        Long earningCount = shopMemberAccountCashOutInDao.findEarningCount (order.getOrderNumber ());
        order.setEarningCount (earningCount);
        if (BeanKit.gt0 (earningCount)) {
            ShopMemberOrderDstbRecord orderRecord = distributionSystemService.findShopMemberOrderRecord (order.getOrderNumber ());
            ErrorHelper.declareNull (orderRecord, "未找到订单收益记录");
            order.setCashEarning (
                    DecimalUtils.add (orderRecord.getTotalCommissionCash (), orderRecord.getTotalManagerCash ())
            );

            order.setGoldCoinEarning (
                    DecimalUtils.add (orderRecord.getTotalManagerGoldCoin (), orderRecord.getTotalCommissionGoldCoin ())
            );
        }
        else {
            order.setCashEarning (BigDecimal.ZERO);
            order.setGoldCoinEarning (BigDecimal.ZERO);
        }


        //移除不需要
        order.setCreateTime (null);
        order.setTakeDeliveryTime (null);
        order.setPayTime (null);
        order.setUpdateTime (null);
        order.setOrderFinishTime (null);

        return order;
    }




    /**
     * 订单收益明细
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Charlie
     * @date 2018/11/9 15:33
     */
    @Override
    public Map<String, Object> orderEarningDetail(ShopMemAcctCashOutInQuery query) {
        Map<String, Object> result = new HashMap<> (4);

        String orderNo = query.getOrderNo ();
        logger.info ("订单收益明细 orderNo={}", orderNo);
        //sql查询
        List<Map<String, Object>> earningsList = shopMemberAccountCashOutInDao.listOrderEarnings (query);

        //列表格式化
        for (Map<String, Object> earnings : earningsList) {
            //日期格式化
            Long orderPayTime = Long.parseLong (earnings.get ("orderPayTime").toString ());
            earnings.put ("orderPayTime", TimeUtils.longFormatString (orderPayTime));

            Long orderSuccessTime = Long.parseLong (earnings.get ("orderSuccessTime").toString ());
            earnings.put ("orderSuccessTime", TimeUtils.longFormatString (orderSuccessTime));

            Long operTime = Long.parseLong (earnings.get ("operTime").toString ());
            earnings.put ("operTime", TimeUtils.longFormatString (operTime));

            //应收金币现金格式化
            BigDecimal shouldInCash = new BigDecimal (earnings.get ("shouldInCash").toString ());
            if (shouldInCash.compareTo (BigDecimal.ZERO) < 0) {
                //没有退单
                earnings.put ("shouldInCash", earnings.get ("earningsCash"));
            }
            BigDecimal shouldInGoldCoin = new BigDecimal (earnings.get ("shouldInGoldCoin").toString ());
            if (shouldInGoldCoin.compareTo (BigDecimal.ZERO) < 0) {
                //没有退单
                earnings.put ("shouldInGoldCoin", earnings.get ("earningsGoldCoin"));
            }
        }


        Map<String, Object> statistics = new HashMap<> (8);
        //汇总
        if (ObjectUtils.isEmpty (earningsList)) {
            statistics.put ("earningsCount", 0);
            //现金收益
            statistics.put ("cashEarnings", 0);
            //实收现金
            statistics.put ("realCashEarnings", 0);
            //待结算现金
            statistics.put ("waitCashEarnings", 0);
            //金币收益
            statistics.put ("goldCoinEarnings", 0);
            //实收金币
            statistics.put ("realGoldCoinEarnings", 0);
            //待结算金币
            statistics.put ("waitGoldCoinEarnings", 0);
        }
        else {

            //实收现金
            BigDecimal realCashEarnings = BigDecimal.ZERO;
            //待结算现金
            BigDecimal waitCashEarnings = BigDecimal.ZERO;
            //实收金币
            BigDecimal realGoldCoinEarnings = BigDecimal.ZERO;
            //待结算金币
            BigDecimal waitGoldCoinEarnings = BigDecimal.ZERO;
            for (Map<String, Object> earnings : earningsList) {
                Integer status = Integer.parseInt (earnings.get ("status").toString ());
                BigDecimal earningsCash = new BigDecimal (earnings.get ("earningsCash").toString ());
                BigDecimal earningsGoldCoin = new BigDecimal (earnings.get ("earningsGoldCoin").toString ());
                if (status == 1) {
                    //待结算
                    waitCashEarnings = waitCashEarnings.add (earningsCash);
                    waitGoldCoinEarnings = waitGoldCoinEarnings.add (earningsGoldCoin);
                }
                else if (status == 2) {
                    //已结算
                    realCashEarnings = realCashEarnings.add (earningsCash);
                    realGoldCoinEarnings = realGoldCoinEarnings.add (earningsGoldCoin);
                }
                else {
                    //ignore
                }
            }
            statistics.put ("realCashEarnings", realCashEarnings);
            statistics.put ("waitCashEarnings", waitCashEarnings);
            statistics.put ("realGoldCoinEarnings", realGoldCoinEarnings);
            statistics.put ("waitGoldCoinEarnings", waitGoldCoinEarnings);

            //收益人数
            Set<Object> userCount = new HashSet<> (earningsList.size ());
            for (Map<String, Object> earnings : earningsList) {
                Object userId = earnings.get ("userId");
                userCount.add (userId);
            }
            statistics.put ("earningsCount", userCount.size ());
            //现金收益
            statistics.put ("cashEarnings", realCashEarnings.add (waitCashEarnings));
            //金币收益
            statistics.put ("goldCoinEarnings", realGoldCoinEarnings.add (waitGoldCoinEarnings));
        }

        //订单通用信息
        ShopMemberOrderQuery ordQuery = new ShopMemberOrderQuery ();
        ordQuery.setOrderNumber (orderNo);
        ShopMemberOrderQuery orderDetail = shopMemberOrderMapper.selectOne (ordQuery);
        ErrorHelper.declareNull (orderDetail, "没有找到订单");
        orderDetail.setPayTimeStr (TimeUtils.longFormatString (orderDetail.getPayTime ()));

        StoreBusinessVo store = storeBusinessMapper.selectByPrimaryKey (orderDetail.getStoreId ());
        if (BeanKit.notNull (store)) {
            orderDetail.setStoreName (store.getBusinessName ());
        }


        result.put ("dataList", new PageInfo<> (earningsList));
        result.put ("statistics", statistics);
        result.put ("orderDetail", orderDetail);
        return result;
    }


    /**
     * 解析物流信息json
     */
    private static List<Map<String, Object>> parseJson(JSONObject jsonObject) {

        JSONObject result = (JSONObject) jsonObject.get ("result");

        JSONArray arrayList = (JSONArray) result.get ("data");

        List<Map<String, Object>> list = new ArrayList<> ();
        //Map<String, Object> data = new HashMap<>();

        if (arrayList != null) {
            DateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < arrayList.size (); i++) {
                JSONObject obj = (JSONObject) arrayList.get (i);
                Map<String, Object> map = new HashMap<> ();
                String context = obj.get ("context").toString ();
                Date time = null;
                try {
                    time = df.parse (obj.get ("time").toString ());
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                //String date = df.format(time);
                map.put ("context", context);
                map.put ("date", time);
                //map.put("date1", obj.get("time").toString());
                list.add (map);
            }
        }
        /*
         * int compare(Person p1, Person p2) 返回一个基本类型的整型，
         * 返回负数表示：p1 小于p2，
         * 返回0 表示：p1和p2相等，
         * 返回正数表示：p1大于p2
         */
        Collections.sort (list, (p1, p2) -> {
            //按照Person的年龄进行升序排列
            Date p1d = (Date) p1.get ("date");
            Date p2d = (Date) p2.get ("date");
            if (p1d.before (p2d)) {
                return 1;
            }
            return - 1;
        });
        return list;
    }
}
