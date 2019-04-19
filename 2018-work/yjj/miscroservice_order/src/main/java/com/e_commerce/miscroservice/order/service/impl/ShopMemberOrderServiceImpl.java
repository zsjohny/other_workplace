package com.e_commerce.miscroservice.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.DistributionGradeEnum;
import com.e_commerce.miscroservice.commons.enums.order.OrderStatusEnums;
import com.e_commerce.miscroservice.commons.enums.order.ShopOrderOperEnum;
import com.e_commerce.miscroservice.commons.enums.pay.WxPayTradeTypeEnum;
import com.e_commerce.miscroservice.commons.enums.user.MemberCanalEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.commons.utils.IdGenerator;
import com.e_commerce.miscroservice.commons.utils.pay.HttpUtil;
import com.e_commerce.miscroservice.commons.utils.wx.PaymentUtils;
import com.e_commerce.miscroservice.order.dao.ShopMemberOrderDao;
import com.e_commerce.miscroservice.order.dao.StoreBizOrderDao;
import com.e_commerce.miscroservice.order.dao.StoreBizOrderMemberDetailDao;
import com.e_commerce.miscroservice.order.entity.*;
import com.e_commerce.miscroservice.order.mapper.ShopMemberOrderItemMapper;
import com.e_commerce.miscroservice.order.mapper.ShopMemberOrderMapper;
import com.e_commerce.miscroservice.order.mapper.StoreBizOrderMapper;
import com.e_commerce.miscroservice.order.rpc.user.MemberRpcService;
import com.e_commerce.miscroservice.order.rpc.user.StoreBusinessRpcService;
import com.e_commerce.miscroservice.order.rpc.user.WxaPayConfigRpcService;
import com.e_commerce.miscroservice.order.service.system.DataDictionaryService;
import com.e_commerce.miscroservice.order.service.wx.ShopMemberOrderService;
import com.e_commerce.miscroservice.order.utils.OrderNotifyHelper;
import com.e_commerce.miscroservice.order.utils.PayConstantPool;
import com.e_commerce.miscroservice.order.vo.StoreBizOrderQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum.STORE_ORDER_PRE_PAY;
import static com.e_commerce.miscroservice.commons.enums.order.ShopOrderOperEnum.PAY_SUCCESS;
import static com.e_commerce.miscroservice.commons.enums.user.MemberCanalEnum.IN_SHOP;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:12
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberOrderServiceImpl implements ShopMemberOrderService{

    private static final String PREPAY_ID = "prepay_id";
    private Log logger = Log.getInstance (ShopMemberOrderServiceImpl.class);

    private static final String BUY_MEMBER_GROUP_CODE = "memberPackageType";
    private static final String BUY_MEMBER_TYPE = "\"memberType\":\"%s\"";
    private static final String PAY_BODY = "%s支付订单";


    @Autowired
    private PayConstantPool payConstantPool;

    @Autowired
    private ShopMemberOrderItemMapper shopMemberOrderItemMapper;
    @Autowired
    private StoreBizOrderMapper storeBizOrderMapper;

    @Autowired
    private MemberRpcService memberRpcService;

    @Autowired
    private WxaPayConfigRpcService wxaPayConfigRpcService;
    @Resource(name = "userStrHashRedisTemplate")
    private ValueOperations<String, String> redisStrService;
    @Autowired
    private StoreBusinessRpcService storeBusinessRpcService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;
    @Resource
    private ShopMemberOrderDao shopMemberOrderDao;
    @Autowired
    private StoreBizOrderMemberDetailDao storeBizOrderMemberDetailDao;
    @Autowired
    private StoreBizOrderDao storeBizOrderDao;

    public static final String WEIXIN_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    /**
     * 根据订单号更新订单信息(订单号必填)
     *
     * @param shopMemberOrder shopMemberOrder
     * @return int
     * @author Charlie
     * @date 2018/10/11 17:43
     */
    @Override
    public int updateByOrderNoSelective(ShopMemberOrder shopMemberOrder) {
        logger.info ("根据订单号更新订单信息 shopMemberOrder={}", shopMemberOrder);
        ErrorHelper.declareNull (shopMemberOrder.getOrderNumber (), "订单号必填");

        ShopMemberOrder query = new ShopMemberOrder ();
        query.setOrderNumber (shopMemberOrder.getOrderNumber ());
        ShopMemberOrder history = shopMemberOrderMapper.selectOne (query);
        ErrorHelper.declareNull (history, "订单不存在 orderNumber=" + shopMemberOrder.getOrderNumber ());

        shopMemberOrder.setId (history.getId ());
        int rec = shopMemberOrderMapper.updateByPrimaryKeySelective (shopMemberOrder);
        logger.info ("更新订单 rec={}", rec);
        return rec;
    }

    /**
     * 当月 粉丝 团队 自购总金额
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Double theMoneyTotalMoney(Long userId, Integer type) {
        if (type.equals (DistributionGradeEnum.STORE.getCode ())) {
            logger.info ("店长当月总购金额");
            Double money = shopMemberOrderDao.storeTheMoneyTotalMoney (userId, type);
            return money;
        }
        if (type.equals (DistributionGradeEnum.DISTRIBUTOR.getCode ())) {
            logger.info ("分销商当月总购金额");
            Double money = shopMemberOrderDao.findCountMoneyShopMemberOrderByUser (userId, type);
            return money;
        }
        return 0d;
    }

    /**
     * 查找 订单列表
     *
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<ShopMemberOrder> findOrderList(Long userId, Integer page) {
        List<ShopMemberOrder> list = shopMemberOrderDao.findOrderList (userId, page);
        return list;
    }

    /**
     * 查找 团队订单列表
     *
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    @Override
    public List<TeamOrder> findTeamOrderList(Long userId, Integer page, String orderNo) {
        if ("-1".equals (orderNo)) {
            orderNo = null;
        }
        List<TeamOrder> list = shopMemberOrderDao.findTeamOrderList (userId, page, orderNo);
        return list;
    }

    /**
     * 查找 团队今日新增
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findTodayTeamOrderSize(Long userId) {
        return shopMemberOrderDao.findTodayTeamOrderSize (userId);
    }

    /**
     * 查找 团队订单信息
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public OrderAccountDetailsResponse findTeamOrder(Long userId, String orderNo) {
        return shopMemberOrderDao.findTeamOrder (userId, orderNo);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public ShopMemberOrder findOrderByOrderNo(String orderNo) {
        ShopMemberOrder shopMemberOrder = shopMemberOrderDao.findOrderByOrderNo (orderNo);
        logger.info("根据订单号查询订单={}", shopMemberOrder);
        return shopMemberOrder;
    }

    /**
     * 团队总数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findCountOrderSize(Long userId) {
        return shopMemberOrderDao.findCountOrderSize (userId);
    }

    /**
     * 团队总金币，现金
     *
     * @param userId
     * @return
     */
    @Override
    public CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(Long userId) {
        return shopMemberOrderDao.findCountTeamMoneyAndCoin (userId);
    }

    /**
     * 团队订单 商品信息
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public List<OrderItemSkuVo> findTeamOrderItemSku(Long userId, String orderNo) {
        return shopMemberOrderDao.findTeamOrderItemSku (userId, orderNo);
    }


    /**
     * 用户已成功的订单数
     *
     * @param memberId       memberId
     * @param excludeOrderNo 排除在外的订单号
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/27 15:18
     */
    @Override
    public Long countUserSuccessOrder(Long memberId, String excludeOrderNo) {
        MybatisSqlWhereBuild where = new MybatisSqlWhereBuild (ShopMemberOrder.class)
                .eq (ShopMemberOrder::getMemberId, memberId)
                .eq (ShopMemberOrder::getOrderStatus, 4);

        if (StringUtils.isNotBlank (excludeOrderNo)) {
            where.neq (ShopMemberOrder::getOrderNumber, excludeOrderNo);
        }

        long count = MybatisOperaterUtil.getInstance ().count (where);
        logger.info ("统计用户已成功的订单数 memberId={},excludeOrderNo={},count={}", memberId, excludeOrderNo, count);
        return count;
    }


    /**
     * 生成订单
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/11 15:05
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map<String, Object> createOrder(StoreBizOrderQuery query) {
        logger.info ("下单");
        Integer operType = query.getOperType ();
        ErrorHelper.declareNull (operType, "未知的购买类型");

        //1购买店中店会员,2购买APP会员,10购买APP商品
        Map<String, Object> res = EmptyEnum.map ();
        switch (operType) {
            //购买店中店会员
            case 1:
                res = createMemberOrderFromInShop (query);
                break;
            //购买APP会员
            case 2:
                ErrorHelper.declare (false, "暂时不支持的订单类型");
                break;
            //10购买APP商品
            case 10:
                ErrorHelper.declare (false, "暂时不支持的订单类型");
                break;
            default:
                throw ErrorHelper.me ("未知的购买类型");
        }

        return res;
    }




    /**
     * 预支付
     *
     * @param query request
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/11 20:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String prePayOrder(StoreBizOrderQuery query) {

        //校验必填
        String orderNo = query.getOrderNo ();
        Integer payWay = query.getPayWay ();
        Integer operType = query.getOperType ();
        logger.info ("订单进行预支付... orderNo={},payWay={}", orderNo, payWay);

        ErrorHelper.declareNull (orderNo, "没有订单号");
        ErrorHelper.declareNull (payWay, "没有支付方式");
        ErrorHelper.declareNull (operType, "未知的请求类型");

        StoreBusinessVo store = storeBusinessRpcService.findStoreIdByInShopMemberId (query.getInShopMemberId ());
        ErrorHelper.declareNull (store, "没有用户信息");
        Long storeId = store.getId ();
        String inShopOpenId = store.getInShopOpenId ();

        //这一步以后放到redis做
        StoreBizOrder order = storeBizOrderDao.findByOrderNo (orderNo, storeId);
        ErrorHelper.declareNull (order, "未找到订单");

        //订单是否可用, 统一做订单是否可以支付的判断
        isOrderCanUse (order);

        //获取预支付信息
        String cacheKey = STORE_ORDER_PRE_PAY.append(orderNo);
        String prePayJson = redisStrService.get (cacheKey);
        if (StringUtils.isNotBlank (prePayJson)) {
            JSONObject prePayObj = JSONObject.parseObject (prePayJson);
            BigDecimal totalFee = prePayObj.getBigDecimal ("totalFee");
            if (! order.getRealPrice ().equals (totalFee)) {
                //改价,重新生成prePayJson
                logger.info ("改价了");
                prePayJson = null;
            }
        }

        if (StringUtils.isBlank (prePayJson)) {
            logger.info ("重新生成支付签名");
            //调用微信获取prePayId
            Map<String, String> prePayMap = doGetWxPrePayInfo (orderNo, operType, order, inShopOpenId);
            prePayJson = JSONObject.toJSONString (prePayMap);
            //同步到数据库
            StoreBizOrder updInfo = new StoreBizOrder ();
            updInfo.setId (order.getId ());
            updInfo.setPrepayId (prePayMap.get (PREPAY_ID));
            int rec = storeBizOrderDao.updateById (updInfo);
            ErrorHelper.declare (rec == 1, "同步预支付信息失败");

            //放入缓存,prePayId有效期120分钟
            redisStrService.set (cacheKey, prePayJson, 110, TimeUnit.MINUTES);
        }
        logger.info ("返回前端支付参数={}", prePayJson);
        return prePayJson;
    }




    /**
     * 支付成功
     *
     * @param query query
     * @author Charlie
     * @date 2018/12/12 21:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(StoreBizOrderQuery query) {
        String orderNo = query.getOrderNo ();
        logger.error ("订单{}支付成功!!!!!", orderNo);

        JSONObject orderJson = query.getAttach ();
        Long orderId = orderJson.getLong ("orderId");

        StoreBizOrder order = MybatisOperaterUtil.getInstance ().findOne (
                new StoreBizOrder (),
                new MybatisSqlWhereBuild (StoreBizOrder.class)
                        .eq (StoreBizOrder::getOrderNo, orderNo)
                        .eq (StoreBizOrder::getId, orderId)
        );

        //订单成功
        unifyOperOrder (order, query, PAY_SUCCESS);
    }



    /**
     * 操作订单的统一函数
     *
     * @param history history
     * @param query query
     * @author Charlie
     * @date 2018/12/12 21:35
     */
    private void unifyOperOrder(StoreBizOrder history, StoreBizOrderQuery query, ShopOrderOperEnum oper) {
        ErrorHelper.declareNull (history, "没有历史订单");

        //校验
        Integer delStatus = history.getDelStatus ();
        ErrorHelper.declare (delStatus == StateEnum.NORMAL, "订单失效");

        switch (oper) {
            case PAY_SUCCESS:
                doPaySuccess (history, query);
                break;


            default:
                throw ErrorHelper.me ("未知的的操作类型");
        }
    }



    /**
     * 支付成功
     *
     * @param history history
     * @param query query
     * @author Charlie
     * @date 2018/12/12 21:40
     */
    private void doPaySuccess(StoreBizOrder history, StoreBizOrderQuery query) {
        Integer orderStatus = history.getOrderStatus ();
        if (! OrderStatusEnums.COMMON_WAY_PAY.isThis (orderStatus)) {
            logger.info ("订单支付状态不是代付款 orderStatus={}", orderStatus);
            return;
        }


        logger.info ("支付成功,修改订单状态");


        Integer type = history.getType ();
        switch (type) {
            case 1:
                //供应商平台商品
                throw ErrorHelper.me ("暂时不支持的订单类型");
            case 2:
                //购买会员
                doPaySuccessOfByMember (history, query);
                break;
            default:throw ErrorHelper.me ("未知的订单类型");
        }

    }

    /**
     * 购买会员支付成功
     *
     * @param history history
     * @param query query
     * @return void
     * @author Charlie
     * @date 2018/12/13 8:56
     */
    private void doPaySuccessOfByMember(StoreBizOrder history, StoreBizOrderQuery query) {
        Long orderId = history.getId ();
        Long payTime = query.getPayTime ();

        StoreBizOrder updInfo = new StoreBizOrder ();
        updInfo.setId (orderId);
        updInfo.setOrderStatus (OrderStatusEnums.COMMON_BUSINESS_SUCCESS.getCode ());
        updInfo.setPayTime (payTime);
        updInfo.setFinishTime (payTime);
        updInfo.setThirdPayNo (query.getThirdPayNo ());
        int upd = storeBizOrderDao.updateById (updInfo);
        ErrorHelper.declare (upd == 1, "购买会员成功,修改订单代付款-->成功状态失败");

        StoreBizOrderMemberDetail orderDetail = storeBizOrderMemberDetailDao.findByOrderId (orderId);

        Integer task = orderDetail.getTaskAfterSuccess ();
        Integer canal = orderDetail.getCanal ();
        Integer memberType = orderDetail.getMemberType ();
        Integer timeUnit = orderDetail.getTimeUnit ();
        Integer timeValue = orderDetail.getTimeValue ();

        MemberOperatorRequest request = new MemberOperatorRequest ();
        request.setType (memberType);
        request.setCanal (MemberCanalEnum.instance (canal).getDescription ());
        request.setTask (task);
        request.setTimeUnit (timeUnit);
        request.setTimeValue (timeValue);
        request.setTotalMoney (history.getRealPrice ().doubleValue ());
        request.setId (history.getBuyerId ());
        boolean isSuccess = memberRpcService.buyMemberSuccess (request);
        ErrorHelper.declare (isSuccess, "购买会员失败");
    }


    /**
     * 向微信请求获取支付签名
     *
     * @param orderNo orderNo
     * @param operType operType
     * @param order order
     * @param inShopOpenId inShopOpenId
     * @return java.util.Map<java.lang.String   ,   java.lang.String>
     * @author Charlie
     * @date 2018/12/12 20:36
     */
    private Map<String, String> doGetWxPrePayInfo(String orderNo, Integer operType, StoreBizOrder order, String inShopOpenId) {

        BigDecimal realPrice = order.getRealPrice ();
        String totalFee = realPrice.multiply (new BigDecimal ("100")).intValue () + "";


        /*
         * 根据操作类型{@code operType},生成不同的签名,因为appId,支付回调attach,支付渠道,都可能不同,以此扩展
         * 但是强烈建议,attach放入json字段 {orderId:订单id,notifyType:支付回调类型},在支付成功回调时可以用的到,并且attach可以扩展
         */
        String returnXml;
        if (operType == 1) {
            //开通店中店
            Long inShopStoreId = payConstantPool.getInShopStoreId();
            logger.info("店中店中端storeId={}", inShopStoreId);
            StoreWxaVo payConfig = wxaPayConfigRpcService.findByStoreId (inShopStoreId);
            ErrorHelper.declareNull (payConfig, "没有支付信息");
            String appId = payConfig.getAppId ();
            String mchId = payConfig.getMchId ();
            String payKey = payConfig.getPayKey ();
            WxPayTradeTypeEnum tradeType = WxPayTradeTypeEnum.XIAO_CHENG_XU;
            //调用微信预支付
            String attach = String.format ("{orderId:%s,notifyType:%s}", order.getId (), OrderNotifyHelper.NotifyType.BUY_FROM_IN_SHOP.getCode ());
            returnXml = httpGetThirdPayNo (
                    appId, mchId, payConstantPool.getInShopWxPayNotifyUrl (), orderNo, totalFee, payKey, tradeType, inShopOpenId,
                    null, attach
            );

            logger.info ("微信预支付 returnXml={}",returnXml);
            Map<String, String> xmlMap = PaymentUtils.xmlToMap (returnXml);
            ErrorHelper.declareNull (xmlMap, "微信预支付失败");

            ErrorHelper.declare ("SUCCESS".equals (xmlMap.get ("return_code").toUpperCase ()), "微信预支付失败");

            String prePayId = xmlMap.get (PREPAY_ID);
            ErrorHelper.declareNull (prePayId, "微信预支付失败");

            Map<String, String> returnVal = new HashMap<> (16);
            returnVal.put("appId", xmlMap.get ("appid"));
            returnVal.put("nonceStr", xmlMap.get ("nonce_str"));
            returnVal.put ("package", "prepay_id=" + prePayId);
            returnVal.put("signType", "MD5");
            returnVal.put("timeStamp", String.valueOf (System.currentTimeMillis ()/1000));
            //再次签名
            String sign = PaymentUtils.createSign (returnVal, payKey);
            returnVal.put("paySign", sign);
            returnVal.put("errorMsg", xmlMap.get ("err_code_des"));
            //这个是多给的,存在缓存中,用户prePayId的验证是否有效,如果价格变了(暂时没有改价功能),prePayId失效
            returnVal.put("totalFee", realPrice.toString ());
            return returnVal;
        }
        else {
            //extend
            throw ErrorHelper.me ("暂时不支持的请求类型");
        }

    }


    private String httpGetThirdPayNo(
            @NotNull String appId,
            @NotNull String mchId,
            @NotNull String notifyUrl,
            @NotNull String orderNo,
            @NotNull String totalFee,
            @NotNull String payKey,
            @NotNull WxPayTradeTypeEnum tradeType,
            @NotNull String openId,

             String body,
             String attach
    ) {
        Map<String, String> param = new HashMap<> (10);
        param.put ("appid", appId);
        param.put ("body", StringUtils.isBlank (body)?String.format (PAY_BODY, "俞姐姐门店宝"):body);
        param.put ("mch_id", mchId);
        param.put ("openid", openId);
        param.put ("nonce_str", BeanKit.uuid ());
        param.put ("notify_url", notifyUrl);
        param.put ("out_trade_no", orderNo);
        param.put ("spbill_create_ip", Iptools.gainRealIp ());
        param.put ("total_fee", totalFee);
        //JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付，
        param.put ("trade_type", tradeType.toString ());
        if (StringUtils.isNotBlank (attach)) {
            param.put ("attach", attach);
        }
        logger.info ("加密前参数={}", param);
        String sign = PaymentUtils.createSign (param, payKey);
        param.put ("sign", sign);

        //请求参数
        String xmlRequest = PaymentUtils.toXml (param);
        logger.info("调用微信支付请求原始报文={}, url={}", xmlRequest, WEIXIN_PAY_URL);
        String result = HttpUtil.post (WEIXIN_PAY_URL, xmlRequest);
        logger.info ("调用微信支付请求返回值={}",result);
        return result;
    }


    /**
     * 订单是否可用
     *
     * @param order 用户订单
     * @throws ErrorHelper
     * @author Charlie
     * @date 2018/12/11 20:40
     */
    private void isOrderCanUse(StoreBizOrder order) {
        //扩展

    }


    /**
     * 生成购买会员的订单
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/11 15:08
     */
    private Map<String, Object> createMemberOrderFromInShop(StoreBizOrderQuery query) {

        Integer memberType = query.getMemberType ();
        ErrorHelper.declareNull (memberType, "未知的会员类型");
        StoreBusinessVo store = storeBusinessRpcService.findStoreIdByInShopMemberId(query.getInShopMemberId ());
//        ErrorHelper.declareNull (store, "没有店家信息");
        Long storeId = store.getId ();


        //查询购买的会员信息
        String memberTypeFilter = String.format (BUY_MEMBER_TYPE, memberType);
        List<DataDictionary> dictList = dataDictionaryService.getByGroupAndLikeComment (BUY_MEMBER_GROUP_CODE, memberTypeFilter);
        int size = dictList.size ();
        logger.info ("查询商品会员套餐 size={},memberTypeFilter={}", size, memberTypeFilter);
        ErrorHelper.declare (size == 1, "未找到会员套餐的支付信息");
        DataDictionary dict = dictList.get (0);

        BigDecimal totalFee = new BigDecimal (dict.getVal ());

        DebugUtils.todo("测试前 0.01元, 已关");
//        totalFee = new BigDecimal("0.01");
        logger.info("改价....!!!!!!!!!!");

        logger.info ("开通店中店会员,支付金额={}", totalFee);
        if (totalFee.compareTo (BigDecimal.ZERO) <= 0) {
            throw ErrorHelper.me ("金额错误");
        }
        String memberConfig = dict.getComment ();
        logger.info ("购买的会员信息 {}", memberConfig);
        ErrorHelper.declareNull (memberConfig, "未找到会员信息");
        //{"memberType":"2","timeUnit":"1","value":"1"}
        JSONObject memberJson = JSONObject.parseObject (memberConfig);
        Integer timeUnit = memberJson.getInteger ("timeUnit");
        Integer timeValue = memberJson.getInteger ("value");


        Map<String, Object> result = new HashMap<> (4);

        //小程序未到期,就不能购买
        boolean isCanOpenInShop = storeBusinessRpcService.isCanOpenInShop (storeId);
        if (! isCanOpenInShop) {
            result.put ("orderNo", "");
            result.put ("totalPay", "");
            result.put ("isCanOrder", false);
            result.put ("isPaySuccess", false);
            return result;
        }

        //套餐价格,价格是0的只能购买一次
        StoreBizOrder noPayOrder = storeBizOrderDao.findNoPayMemberOrder (storeId, memberType, totalFee, IN_SHOP.getCode ());
        //查询已有未支付订单,如果有则直接返回
        if (noPayOrder != null) {
            String noPayOrderNo = noPayOrder.getOrderNo ();
            logger.info ("会员服务下单,返回用户未支付的订单 orderNo={}", noPayOrderNo);
            result.put ("orderNo", noPayOrderNo);
            result.put ("totalPay", totalFee);
            result.put ("isCanOrder", true);
            return result;
        }

        //创建订单
        StoreBizOrder order = new StoreBizOrder ();
        order.setBuyerId (storeId);
        String orderNo = IdGenerator.getCurrentTimeId ("", String.format ("%07d", storeId));
        order.setOrderNo (orderNo);
        order.setOrderStatus (OrderStatusEnums.COMMON_WAY_PAY.getCode ());
        order.setType (2);

        order.setTotalPrice (totalFee);
        order.setRealPrice (totalFee);
        int save = storeBizOrderDao.save (order);
        ErrorHelper.declare (save == 1, "创建订单失败");


        //创建订单详情
        StoreBizOrderMemberDetail orderDetail = new StoreBizOrderMemberDetail ();
        orderDetail.setCanal (IN_SHOP.getCode ());
        orderDetail.setTaskAfterSuccess (1);
        orderDetail.setMemberType (memberType);
        orderDetail.setOrderNo (orderNo);
        orderDetail.setOrderId (order.getId ());
        orderDetail.setTimeUnit (timeUnit);
        orderDetail.setTimeValue (timeValue);
        save = storeBizOrderMemberDetailDao.save (orderDetail);
        ErrorHelper.declare (save == 1, "保存订单详情失败");

        result.put ("orderNo", orderNo);
        result.put ("totalPay", totalFee);
        result.put ("isCanOrder", true);
        return result;
    }

    @Transactional( rollbackFor = Exception.class)
    @Override
    public Response defaultAddress(Long memberId,Long id) {
        storeBizOrderMapper.updateAdress(memberId);
        int i = storeBizOrderMapper.chooseDefault(id);
        if (i!=1){
            logger.info("添加默认收货地址失败");
        }
        return Response.success("添加成功");
    }

    @Override
    public Response getAddress(int type,Long storeId, Long memberId) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map=null;
        if (type==0){//到店取货
            map=new HashMap<>();
            StoreBusiness storeBusiness = storeBizOrderMapper.selectBusiness(storeId);
            if (storeBusiness==null){
                map.put("address","");
                map.put("businessName","");
                list.add(map);
            }else {
                map.put("address",storeBusiness.getBusinessAddress());
                map.put("businessName",storeBusiness.getBusinessName());

            }

        }else {

            List<ShopMemberDeliveryAddress> shopMemberDeliveryAddresses = storeBizOrderMapper.selectMemberAddress(memberId);

            for (ShopMemberDeliveryAddress shopMemberDeliveryAddress : shopMemberDeliveryAddresses) {
                    map=new HashMap<>();
                    map.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
                    map.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());
                    map.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
                    String address = shopMemberDeliveryAddress.getLocation().replace("-", "") + shopMemberDeliveryAddress.getAddress();
                    map.put("address", address);
                    map.put("defaultStatus",shopMemberDeliveryAddress.getDefaultStatus());
                    list.add(map);
            }
        }
        return Response.success(list);
    }

    @Override
    public Response addAddress(RequestAddress requestAddress) {
        long timeMillis = System.currentTimeMillis();
        ShopMemberDeliveryAddress shopMemberDeliveryAddress=new ShopMemberDeliveryAddress();
        shopMemberDeliveryAddress.setAddress(requestAddress.getAddress());
        shopMemberDeliveryAddress.setCreateTime(timeMillis);
        if (requestAddress.getDefaultStatus()!=null){
            shopMemberDeliveryAddress.setDefaultStatus(requestAddress.getDefaultStatus());
            storeBizOrderMapper.updateAdress(requestAddress.getMemberId());//设为默认收货地址清除之前得默认地址
        }else {
            shopMemberDeliveryAddress.setDefaultStatus(0);
        }
        shopMemberDeliveryAddress.setLinkmanName(requestAddress.getLinkmanName());
        shopMemberDeliveryAddress.setLocation(requestAddress.getLocation());
        shopMemberDeliveryAddress.setLastUsedTime(timeMillis);
        shopMemberDeliveryAddress.setPhoneNumber(requestAddress.getPhoneNumber());
        shopMemberDeliveryAddress.setShopMemberId(requestAddress.getMemberId());
        shopMemberDeliveryAddress.setUpdateTime(timeMillis);
        Long lastId = storeBizOrderMapper.selectCount();
        int save = MybatisOperaterUtil.getInstance().save(shopMemberDeliveryAddress);
        if (save!=1){
            logger.info("新增地址失败");
        }

        Long shopMemberDeliveryAddressId = lastId;
        HashMap map=new HashMap();
        map.put("shopMemberDeliveryAddressId",shopMemberDeliveryAddressId);
        logger.info("2019.02.18发布成功测试shopMemberDeliveryAddressId="+shopMemberDeliveryAddressId);
        return Response.success(map);
    }

    @Override
    public Response selectAddress(Long memberId, Long id) {
        ShopMemberDeliveryAddress shopMemberDeliveryAddress = storeBizOrderMapper.selectAddressById(memberId, id);
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryAddressId", id);
        map.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());
        map.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
        map.put("location", shopMemberDeliveryAddress.getLocation());
        map.put("address", shopMemberDeliveryAddress.getAddress());
        map.put("defaultStatus",shopMemberDeliveryAddress.getDefaultStatus());
        return Response.success(map);
    }

    @Override
    public Response updateAddress(RequestAddress requestAddress) {
        ShopMemberDeliveryAddress shopMemberDeliveryAddress = storeBizOrderMapper.selectAddressById(requestAddress.getMemberId(), requestAddress.getDeliveryAddressId());
        shopMemberDeliveryAddress.setPhoneNumber(requestAddress.getPhoneNumber());
        shopMemberDeliveryAddress.setLocation(requestAddress.getLocation());
        shopMemberDeliveryAddress.setLinkmanName(requestAddress.getLinkmanName());
        shopMemberDeliveryAddress.setAddress(requestAddress.getAddress());
       if (requestAddress.getDefaultStatus()==1){
           storeBizOrderMapper.updateAdress(requestAddress.getMemberId());
           shopMemberDeliveryAddress.setDefaultStatus(requestAddress.getDefaultStatus());
       }else {
           shopMemberDeliveryAddress.setDefaultStatus(0);
       }
        int update = MybatisOperaterUtil.getInstance().update(shopMemberDeliveryAddress, new MybatisSqlWhereBuild(ShopMemberDeliveryAddress.class)
                .eq(ShopMemberDeliveryAddress::getId, requestAddress.getDeliveryAddressId()));
       if (update!=1){
           logger.info("修改地址失败");
       }
        return Response.success("修改地址成功");
    }

    @Override
    public ShopMemberOrderItem findBySql(Long orderItemId) {
        return shopMemberOrderItemMapper.findBySql(orderItemId);
    }

}
