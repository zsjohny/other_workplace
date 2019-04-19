package com.jiuy.rb.service.impl.payment;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.enums.GlobalsEnums;
import com.store.entity.YjjStoreBusinessAccountLog;
import com.util.CallBackUtil;
import com.util.MapTrunPojo;
import com.util.ServerPathUtil;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ConstMy;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.order.ShopMemberOrderItemRbMapper;
import com.jiuy.rb.mapper.order.ShopMemberOrderLogRbMapper;
import com.jiuy.rb.mapper.order.ShopMemberOrderRbMapper;
import com.jiuy.rb.model.account.CoinsVo;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.coupon.WxaShare;
import com.jiuy.rb.model.coupon.WxaShareQuery;
import com.jiuy.rb.model.order.ShopMemberOrderItemRb;
import com.jiuy.rb.model.order.ShopMemberOrderItemRbQuery;
import com.jiuy.rb.model.order.ShopMemberOrderLogRb;
import com.jiuy.rb.model.order.ShopMemberOrderRb;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.service.account.ICoinsAccountService;
import com.jiuy.rb.service.account.IShareService;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.payment.IPaymentService;
import com.jiuy.rb.service.product.IBrandService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpClientUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 支付相关的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/17 11:22
 * @Copyright 玖远网络
 */
@Service("paymentService")
public class PaymentServiceImpl implements IPaymentService {
    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private ShopMemberOrderRbMapper shopMemberOrderRbMapper;

    @Autowired
    private ShopMemberOrderLogRbMapper shopMemberOrderLogRbMapper;

    @Resource(name = "coinsAccountService")
    private ICoinsAccountService coinsAccountService;

    @Resource(name = "shareService")
    private IShareService shareService;

    @Resource(name = "shopMemberOrderItemRbMapper")
    private ShopMemberOrderItemRbMapper shopMemberOrderItemRbMapper;

    @Resource(name = "shopProductServiceRb")
    private IShopProductService shopProductService;

    @Resource(name = "dataDictionaryServiceRb")
    private IDataDictionaryService dataDictionaryService;

    @Resource(name = "couponServerNew")
    private ICouponServerNew couponServerNew;
    @Autowired
    private IBrandService brandService;


    /**
     * 回调
     *
     * @param shopMemberOrder shopMemberOrder
     * @param paymentNo paymentNo
     * @param paymentType paymentType
     * @author Aison
     * @date 2018/7/17 11:29
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateOrderAlreadyPayStatus(ShopMemberOrder shopMemberOrder, String paymentNo, PaymentType paymentType) {

        if(shopMemberOrder.getOrderStatus() != ShopMemberOrder.order_status_pending_payment) {
            logger.info ("订单状态={}", shopMemberOrder.getOrderStatus ());
            return -1;
        }
        ShopMemberOrderRb shopMemberOrderUpd = new ShopMemberOrderRb();
        shopMemberOrderUpd.setId(shopMemberOrder.getId());
        if (ShopMemberOrder.order_type_delivery == shopMemberOrder.getOrderType()) {
            //待发货
            shopMemberOrderUpd.setOrderStatus(ShopMemberOrder.order_status_pending_delivery);
        }
        if (ShopMemberOrder.order_type_get_product_at_store == shopMemberOrder.getOrderType()) {
            //待提货
            shopMemberOrderUpd.setOrderStatus(ShopMemberOrder.order_status_paid);
        }
        shopMemberOrderUpd.setPaymentNo(paymentNo);
        shopMemberOrderUpd.setPaymentType(ShopMemberOrder.paymentType_wxa);
        long time = System.currentTimeMillis();
        //付款时间
        shopMemberOrderUpd.setPayTime(time);
        shopMemberOrderUpd.setUpdateTime(time);
        int record = shopMemberOrderRbMapper.updateByPrimaryKeySelective(shopMemberOrderUpd);
        if(record!=1){
            throw new RuntimeException("修改订单为已支付状态shopMemberOrderId:"+shopMemberOrderUpd.getId());
        }

        addShopMemberOrderLog(shopMemberOrder.getMemberId(), shopMemberOrder.getOrderStatus(), shopMemberOrderUpd);
//        addCoins(shopMemberOrder);

        sendCoupon(shopMemberOrder.getOrderNumber(),shopMemberOrder.getMemberId());

        Long storeId = shopMemberOrder.getStoreId();
        Long memberId = shopMemberOrder.getMemberId();
        Long orderId = shopMemberOrder.getId();

        String url = "/distribution/distribution/cashOutIn/addDstbFromOrder";
        JSONObject map = new JSONObject();
        map.put ("orderNumber", shopMemberOrder.getOrderNumber ());
        map.put ("memberId", memberId);
        map.put ("storeId", storeId);
        map.put ("realPay", shopMemberOrder.getPayMoney ());
        map.put ("payTime", time);
        map.put ("wx2DstbSign", new Md5Hash (shopMemberOrder.getOrderNumber () + "xiaochengxu2dstb").toString ());
        logger.info ("下单计算分销返利, 请求记录分销 url={},map={}", url,map);
        CallBackUtil.send(map.toJSONString(),url,"get");

        //渠道商
        logger.info("所属小程序 storeId={}", storeId);
        String channelFansOrderParam = new JSONObject() {{ put("orderId", orderId);put("shopMemberId", memberId);put("storeId", storeId); }}.toJSONString();
        logger.info ("渠道商粉丝下单, map={}", channelFansOrderParam);
        CallBackUtil.send(channelFansOrderParam, "/activity/activity/channel/fansPaySuccess", "get");

        String url2 = "/user/account/update";
        //店中店购买商品
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
        yjjStoreBusinessAccountLog.setUserId(storeId);
        //收入
        yjjStoreBusinessAccountLog.setInOutType(0);
        yjjStoreBusinessAccountLog.setOperMoney(shopMemberOrder.getPayMoney());
        yjjStoreBusinessAccountLog.setRemarks("货品订单-"+shopMemberOrder.getUserNickname()+"的订单");
        yjjStoreBusinessAccountLog.setAboutOrderNo(shopMemberOrder.getOrderNumber());
        yjjStoreBusinessAccountLog.setType(1801);
       // yjjStoreBusinessAccountLog.setAboutOrderNo(orderId.toString());
//        Map pojo= MapTrunPojo.object2Map(yjjStoreBusinessAccountLog);
//        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmm="+pojo);
//        CallBackUtil.send(JSONObject.toJSONString(yjjStoreBusinessAccountLog),"/user/account/update","get");
        logger.info ("店中店购买商品回调 url={},yjjStoreBusinessAccountLog={}", url,yjjStoreBusinessAccountLog);
        System.out.println("店中店购买商品回调="+System.currentTimeMillis());

        CallBackUtil.send(JSONObject.toJSONString(yjjStoreBusinessAccountLog),url2,"get");
        return record;
    }


    /**
     *  发送优惠券
     *
     * @param orderNo orderNo
     * @param memberId memberId
     * @author Aison
     * @date 2018/8/3 18:47
     */
    private void sendCoupon(String orderNo,Long memberId) {
        try{
            CouponAcceptVo accept = new CouponAcceptVo(memberId,null,orderNo,CouponSysEnum.WXA,CouponSendEnum.ORDER,CouponStateEnum.NOT_USE);
            couponServerNew.grant(accept);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addCoins(ShopMemberOrder shopMemberOrderRb) {
        logger.info("=========================添加玖币============================");
        // 判断是否存在关系
        Long memberId = shopMemberOrderRb.getMemberId();
        WxaShareQuery query = new WxaShareQuery();
        query.setTargetUser(memberId);
        List<WxaShare> wxaShareList =  shareService.getShare(query);
        if(wxaShareList.size()==0 || memberId==null) {
            return ;
        }
        WxaShare share = wxaShareList.get(0);
        ShopMemberOrderItemRbQuery itemRbQuery = new ShopMemberOrderItemRbQuery();
        itemRbQuery.setOrderNumber(shopMemberOrderRb.getOrderNumber());
        List<ShopMemberOrderItemRb> items = shopMemberOrderItemRbMapper.selectList(itemRbQuery);

        //获取商品的价格
        BigDecimal account = BigDecimal.ZERO;
        StringBuilder productNames = new StringBuilder();
        // 计算可以获得的玖币
        DataDictionaryRb dd = dataDictionaryService.getByCode(ConstMy.SHARE_POINT_CODE,ConstMy.SHARE_POINT_GROUP);
        DataDictionaryRb shareStoreId = dataDictionaryService.getByCode(ConstMy.YJJ_SHARE_STORE_ID,ConstMy.YJJ_SHARE_STORE_ID);
        //若为俞姐姐品牌则 获取俞姐姐品牌的 分享比率
        if (shopMemberOrderRb.getStoreId()==Long.parseLong(shareStoreId.getVal())){
            dd = dataDictionaryService.getByCode(ConstMy.YJJ_SHARE_POINT_CODE,ConstMy.YJJ_SHARE_POINT_CODE);
        }

        logger.info("=========================添加玖币============================入账的 比率={}",dd.getVal());
        logger.info("=========================添加玖币============================getStoreId={}",shopMemberOrderRb.getStoreId());
        logger.info("=========================添加玖币============================getVal={}",shareStoreId.getVal());
        DataDictionaryRb radio = dataDictionaryService.getByCode(ConstMy.COINS_RMB_RADIO_CODE,ConstMy.COINS_RMB_RADIO_GROUP);
        //要进账的人名币
        BigDecimal inRMB = BigDecimal.ZERO;
        if(dd==null || radio==null) {
            throw BizException.instance(GlobalsEnums.BUY_POINT_IS_NULL);
        }
        for (ShopMemberOrderItemRb item : items) {
            ShopProductRb shopProductRb = shopProductService.getById(item.getShopProductId());
            //根据商品id 获取品牌信息
//            BrandRb brandRb = brandService.findBrandByProductId(item.getProductId());

            if(shopProductRb!=null) {
                BigDecimal price = shopProductRb.getPrice();
                price = price==null ? BigDecimal.ZERO:price;
                account = account.add(price.multiply(new BigDecimal(item.getCount())));
                productNames.append(shopProductRb.getName()).append(" ");

                //单类产品的价格
//                BigDecimal money = price.multiply(new BigDecimal(item.getCount()));
                //要进账的人名币(临时采用这种形式 区别俞姐姐优选与其他品牌的 分佣比例)
//                if (brandRb!=null&&brandRb.getBrandName().equals("俞姐姐优选")){
//                    money =  money.multiply(new BigDecimal(0.2)).setScale(2,BigDecimal.ROUND_HALF_UP);
//                }else {
//                    money =  money.multiply(new BigDecimal(dd.getVal())).setScale(2,BigDecimal.ROUND_HALF_UP);
//
//                }
                //价格添加
//                account = account.add(money);
            }
        }


        //要进账的人名币
        inRMB =  account.multiply(new BigDecimal(dd.getVal())).setScale(2,BigDecimal.ROUND_HALF_UP);
//        inRMB =  account;
        //兑换成玖币
        Long inCoins =  inRMB.multiply(new BigDecimal(radio.getVal())).setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
        if(inCoins<=0) {
            return;
        }

        Long inviter = share.getSourceUser();
        Date deadline = Biz.addDate(new Date(),24*365);
        String note = "被邀请者购买商品邀请者入账玖币 当前商品比率:"+dd.getVal()+" 当前玖币人民币兑换比率："+radio.getVal();

        // 添加玖币
        CoinsVo coinsVo = CoinsVo.instance(CoinsDetailTypeEnum.INVITE_BUY_IN,shopMemberOrderRb.getOrderNumber(),inCoins,deadline,null,
                "支付成功:购买商品 "+productNames.toString(),note
                ,
                null,
                inviter,
                AccountTypeEnum.WXA_USER);
        coinsVo.setCreateAccount(true);
        coinsAccountService.acceptCoins(coinsVo);
    }

    /**
     * 添加日志
     *
     * @param memberId memberId
     * @param oldStatus oldStatus
     * @param shopMemberOrder shopMemberOrder
     * @author Aison
     * @date 2018/7/17 11:28
     */
    private void addShopMemberOrderLog(long memberId, int oldStatus, ShopMemberOrderRb shopMemberOrder) {

        ShopMemberOrderLogRb shopMemberOrderLog = new ShopMemberOrderLogRb();
        shopMemberOrderLog.setOldStatus(oldStatus);
        shopMemberOrderLog.setMemberId(memberId);
        shopMemberOrderLog.setNewStatus(shopMemberOrder.getOrderStatus());
        shopMemberOrderLog.setOrderId(shopMemberOrder.getId());
        shopMemberOrderLog.setCreateTime(shopMemberOrder.getUpdateTime());
        int record = shopMemberOrderLogRbMapper.insertSelective(shopMemberOrderLog);
        if(record!=1){
            throw new RuntimeException("添加log失败shopMemberOrderId:"+shopMemberOrderLog.getOrderId());
        }
    }

}
