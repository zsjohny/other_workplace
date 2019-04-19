package com.e_commerce.miscroservice.order.controller.shop;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.order.entity.*;
import com.e_commerce.miscroservice.order.method.WxPayResultData;
import com.e_commerce.miscroservice.order.rpc.user.StoreBusinessAccountRpcService;
import com.e_commerce.miscroservice.order.service.wx.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;

@RestController
@RequestMapping("/wxMember/order")
public class MemberController {
    private Log logger = Log.getInstance(MemberController.class);

    @Value("${skipShop.storeId}")
    private String shopInId;

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;


    @RequestMapping("/wxaPay")
    public Response wxaPay(Long orderId,
                           @RequestParam( "memberId" ) Long memberId,
                           @RequestParam( "storeId" ) Long storeId
    ) {
        logger.info("shopInId"+shopInId);
        String ip = Iptools.gainRealIp();
        try {
            logger.info("orderId="+orderId,"memberId="+memberId,"storeId="+storeId);
            if (orderId==null||memberId==null||storeId==null){
                logger.warn("参数为空orderId={},memberId={},storeId={}", orderId,memberId,storeId);
                return Response.errorMsg("参数为空orderId="+orderId+",memberId="+memberId+",storeId="+storeId+"");
            }
            ShopMember member = memberService.selectShopMember(memberId);
            logger.info("查询用户 member={}",member);
            StoreBusiness storeBusiness = memberService.selectStoreBusiness(storeId);
            Integer wxaBusinessType = storeBusiness.getWxaBusinessType();

            String bindWeixin = member.getBindWeixin();
            Long storeIdNew=memberService.selectOppenId(bindWeixin,storeId);
            /*
             * 当前小程序的店铺状态
             * 小程序店铺类型 0从未开通,1共享版(过期不设为0),2专项版(过期不设为0)
             */
            Integer type=null;
            if (wxaBusinessType == 1) {
                logger.info("获取id=shopInId={}", shopInId);
                storeBusiness.setId(Long.valueOf(shopInId));
                storeBusiness.setId(storeIdNew);
//                Long inShopMemberId = member.getInShopMemberId();
//                ShopMember shopMember = memberService.selectShopMember(inShopMemberId);
//                bindWeixin = shopMember.getBindWeixin();
            }
            ShopMemberOrder shopMemberOrder = memberService.getMemberById(orderId);
            if (shopMemberOrder==null){
                return Response.errorMsg("订单号不存在");
            }
            logger.info("订单信息={}", shopMemberOrder);
            //根据购买方式进行一系列判断
            if (shopMemberOrder.getBuyWay() == 2) {
                //秒杀活动
                SecondBuyActivity secondBuyActivity = memberService.selectById(shopMemberOrder.getSecondId());
                //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
                int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
                //秒杀活动手动结束或者订单超时
                if ((haveActivityStatusInt == 3 && secondBuyActivity.getActivityHandEndTime() != 0) || isOverTime(shopMemberOrder)) {
                    logger.info(haveActivityStatusInt + "" + isOverTime(shopMemberOrder));
                    return Response.success("活动已过期，无法完成付款。欢迎下次参与！");
                }

            }

            if (shopMemberOrder.getBuyWay() == 1) {
                //团购活动
                TeamBuyActivity teamBuyActivity = memberService.selectTeamId(shopMemberOrder.getTeamId());
                //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
                int haveActivityStatusInt = teamBuyActivity.haveActivityStatusInt();
                //判断活动是否结束
                if (haveActivityStatusInt == 3) {
                    return Response.success("活动已过期，无法完成付款。欢迎下次参与！");
                }

                //下单减库存,表明有订单一定有库存,这里只判断拼团是否成功
                boolean isTeamSuccess = isTeamSuccess(teamBuyActivity);
                if (isTeamSuccess) {
                    logger.info("已达到成团条件{activityId:" + teamBuyActivity.getId() +
                            "," + teamBuyActivity.getConditionType() +
                            "" + teamBuyActivity.getUserCount() +
                            "" + teamBuyActivity.getActivityMemberCount() +
                            "" + teamBuyActivity.getMeetProductCount() +
                            "" + teamBuyActivity.getOrderedProductCount()
                    );

                } else {
                    StringBuilder builder = new StringBuilder("当前活动参团数量不足，无法完成付款！还差");
                    if (2 == teamBuyActivity.getConditionType()) {
                        builder.append(teamBuyActivity.getMeetProductCount() - teamBuyActivity.getOrderedProductCount()).append("件就可成团啦!");
                    } else {
                        builder.append(teamBuyActivity.getUserCount() - teamBuyActivity.getActivityMemberCount()).append("人就可成团啦!");
                    }
                    String tip = builder.toString();
                    return Response.errorMsg(tip);
                }
            }

            SortedMap<Object, Object> signMap = unifiedorder(storeBusiness, orderId, bindWeixin, ip,type);
            return Response.success(signMap);
        } catch (Exception e) {
            logger.error("微信支付的接口:" + e.getMessage());
            e.printStackTrace();
            return Response.errorMsg(e.getMessage());
        }
    }

    /**
     * 统一支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public SortedMap<Object, Object> unifiedorder(StoreBusiness storeBusiness, long orderId, String bindWeixin, String ip,Integer type) {
        logger.info("调用微信预支付 orderId={}", orderId);
        StoreWxa storeWxa = memberService.getStoreWxaByStoreId(String.valueOf(storeBusiness.getId()));
        if (storeWxa == null) {
            throw new RuntimeException("该商家没有绑定小程序");
        }
        String appId = storeWxa.getAppId();
        String mchId = storeWxa.getMchId();

        if (mchId == null) {
            throw new RuntimeException("该商家没有填写商户号");
        }
        String payKey = storeWxa.getPayKey();
        if (payKey == null) {
            throw new RuntimeException("该商家没有填写商家秘钥");
        }

        ShopMemberOrder shopMemberOrder = memberService.getMemberById(orderId);
        if (shopMemberOrder == null) {
            throw new RuntimeException("订单不存在");
        }

        // TODO: 2018/12/18  暂时不确定是否需要 
        //如果是店中店用户
//        if (type==1){
//            appId="";
//            mchId="";
//            payKey="";
//            bindWeixin="";
//        }

        String orderNo = String.valueOf(shopMemberOrder.getOrderNumber());

        List<ShopMemberOrderItem> memberOrderItemList = memberService.getMemberOrderItemList(orderId);

        ShopMemberOrderItem shopMemberOrderItem = memberOrderItemList.get(0);
        //1、准备数据
        String productName = shopMemberOrderItem.getName();

        BigDecimal payMoney = shopMemberOrder.getPayMoney();
        String string = payMoney.toString();
        double v = Double.parseDouble(string);
        int total_fee = (int) (v * 100);
        String businessName = storeBusiness.getBusinessName();

        //2、订单统一支付
        String result = memberService.unifiedorder(orderNo, productName, total_fee, bindWeixin, businessName, appId, mchId, payKey, ip,type);

        //3、解析微信下单接口返回的XML信息
        WxPayResultData resultData = memberService.buildWxPayResultData(result);
        System.out.println("微信支付下单返回,resultData:" + JSON.toJSONString(resultData));
        SortedMap<Object, Object> signMap;
        if ("SUCCESS".equals(resultData.getResult_code()) && "SUCCESS".equals(resultData.getReturn_code())) {
            //生成sign
            signMap = memberService.buildSignData(resultData, payKey);
            String jsonStr = JSON.toJSONString(signMap);
            logger.info("返回页面参数json：" + jsonStr);

            //修改订单支付标识码
            String prepay_id = resultData.getPrepay_id();
            logger.info("开始修改订单支付标识prepay_id：" + prepay_id + ",orderId:" + orderId);
            int ret = memberService.updatePrepayId(orderId, prepay_id);
            /**
            YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setUserId(storeBusiness.getId());
//				收入
            yjjStoreBusinessAccountLog.setInOutType(0);
            yjjStoreBusinessAccountLog.setOperMoney(Double.valueOf(total_fee));
            yjjStoreBusinessAccountLog.setRemarks(shopMemberOrder.getUserNickname()+shopMemberOrderItem.getName());

            storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
             */
            logger.info("修改订单支付标识完成ret:" + ret + ",prepay_id：" + prepay_id + ",orderId:" + orderId);

        } else {
            logger.info("微信支付下单错误,resultData:" + JSON.toJSONString(resultData));
            throw new RuntimeException("调用微信出现错误");
        }
        return signMap;
    }


    /**
     * 是否拼团成功
     *
     * @param teamAct 活动
     * @return boolean
     * @author Charlie
     * @date 2018/7/31 18:00
     */
    public static boolean isTeamSuccess(TeamBuyActivity teamAct) {
        if (teamAct.getConditionType() == 2) {
            if (teamAct.getMeetProductCount() <= teamAct.getOrderedProductCount()) {
                return true;
            }
        } else {
            if (teamAct.getUserCount() <= teamAct.getActivityMemberCount()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断秒杀订单保留时间是否过期
     *
     * @param shopMemberOrder
     * @return
     */
    public boolean isOverTime(ShopMemberOrder shopMemberOrder) {
        long overdueTime = shopMemberOrder.getCreateTime() + 2 * 60 * 60 * 1000;
        long nowTime = System.currentTimeMillis();
        long time = overdueTime - nowTime;
        if (time < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检验门店是否为空
     */
    private void checkStore(ShopDetail shopDetail) {
        if (shopDetail == null || shopDetail.getId() == 0) {
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
            throw new RuntimeException("商家信息不能为空");
        }
    }

    /**
     * 检验会员是否为空
     */
    private void checkMember(MemberDetail memberDetail) {
        if (memberDetail == null || memberDetail.getId() == 0) {
            logger.info("会员信息不能为空，该接口需要登陆，请排除问题");
            throw new RuntimeException("会员信息不能为空");
        }
    }

}
