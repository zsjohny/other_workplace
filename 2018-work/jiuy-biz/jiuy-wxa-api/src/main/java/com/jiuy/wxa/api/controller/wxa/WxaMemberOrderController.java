package com.jiuy.wxa.api.controller.wxa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.rb.model.order.ShopMemberOrderRbQuery;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.order.IMemberOrderService;
import com.jiuy.rb.service.user.IUserService;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.service.common.area.ActivityInfoCache;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonExtResponse;
import com.store.entity.member.ShopMember;
import com.store.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ShopMemberDeliveryAddress;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.order.OrderStateVO;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.QRCodeUtil;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.MemberMapper;
import com.store.dao.mapper.ShopMemberDeliveryAddressMapper;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.ShopMemberOrderFacade;
import com.store.service.ShopMemberOrderService;
import com.store.service.ShopMemberOrderStateFacade;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 会员订单表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@Controller
@RequestMapping("/mobile/memberOrder")
public class WxaMemberOrderController {
    private static final Log logger = LogFactory.get("小程序订单");

    @Resource(name = "memberOrderService")
    private IMemberOrderService memberOrderService;

    @Autowired
    private ShopMemberOrderService shopMemberOrderService;

    @Autowired
    private ShopMemberOrderFacade shopMemberOrderFacade;

    @Autowired
    private ShopMemberOrderStateFacade shopMemberOrderStateFacade;

    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private ShopMemberDeliveryAddressMapper shopMemberDeliveryAddressMapper;

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreBusinessNewService storeBusinessNewService;
    @Autowired
    private ShopGoodsCarService shopGoodsCarService;


    @Resource(name = "userService")
    private IUserService userService;
    @Autowired
    ICouponServerNew couponServerNew;

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderStateInfo")
    @ResponseBody
    public JsonResponse getOrderStateInfo(Long storeId, Long memberId, long orderId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            List<OrderStateVO> data = shopMemberOrderStateFacade.getOrderStateInfo(orderId);
            return jsonResponse.setSuccessful().setData(data);
        } catch (TipsMessageException e) {
            logger.info(e.getFriendlyMsg());
            return jsonResponse.setError(e.getFriendlyMsg());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 确认订单的接口
     *
     * @return
     */
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public JsonResponse confirmOrder(HttpServletRequest request,
                                     @RequestParam(value = "productIdSkuIdCount", required = false) String[] productIdSkuIdCounts,
                                     @RequestParam(value = "orderSkuJsonArr", required = false) String orderSkuJsonArr,
                                     @RequestParam(value = "otherPage", required = false, defaultValue = "0") int otherPage,
                                     Long storeId, @RequestParam(value = "activityId", required = false) Long activityId,
                                     @RequestParam(value = "targetType", required = false) Integer targetType,
                                     Long memberId) {
        checkLogin(storeId, memberId);
        ShopDetail shopDetail = shopDetailBack(storeId);
        MemberDetail memberDetail = memberDetailBack(memberId);

        String version = request.getHeader("version");
        //版本
        Integer wxVersion = HttpUtils.wxVersion(version, 140);

        logger.info("小程序会员确认订单WxaMemberOrderController:productIdSkuIdCounts-" + productIdSkuIdCounts + ";storeId-" + storeId
                + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //放入活动和version,做版本兼容
            ActivityInfoCache.createInstance(activityId, targetType, wxVersion);

//            List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
//            for (String productIdSkuIdCount : productIdSkuIdCounts) {
//                String[] split = productIdSkuIdCount.split("_");
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("productId", split[0]);
//                param.put("skuId", split[1]);
//                param.put("count", split[2]);
//                paramList.add(param);
//            }

            List<Map<String, String>> paySkuList = clearPaySkuList(orderSkuJsonArr, productIdSkuIdCounts);

            //获取商品详情和优惠券个数
            Map<String, Object> data = shopMemberOrderFacade.confirmOrder(paySkuList, storeId, memberId, version);
            //默认收货方式送货上门
            Map<String, Object> addressInfo = shopMemberOrderFacade.chooseDeliveryWay(otherPage, shopDetail, memberDetail);
            //总是带着商家地址
            addressInfo.put("dorderType", ShopMemberOrder.order_type_get_product_at_store);//默认到店取货
            //获取该用户的收货信息
            addressInfo.put("dbusinessName", shopDetail.getStoreBusiness().getBusinessName());//商家名称
            addressInfo.put("daddress", shopDetail.getStoreBusiness().getBusinessAddress());//到店取货地址
            data.put("storeBusiness", shopDetail.getStoreBusiness());
            data.put("addressInfo", addressInfo);
            return jsonResponse.setSuccessful().setData(data);
        } catch (TipsMessageException e) {
            logger.info(e.getFriendlyMsg());
            return jsonResponse.setError(e.getFriendlyMsg());
        } catch (Exception e) {
            logger.info("确认订单的接口:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        } finally {
            ActivityInfoCache.clear();
        }
    }

    /**
     * #######################################################################################################
     * 确认订单的接口
     * #######################################################################################################
     */
    @RequestMapping("/confirmOrderV362")
    @ResponseBody
    public JsonResponse confirmOrderV362(
            HttpServletRequest request,
            @RequestParam(value = "productIdSkuIdCount", required = false) String[] productIdSkuIdCounts,
            @RequestParam(value = "orderSkuJsonArr", required = false) String orderSkuJsonArr,
            @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType,
            @RequestParam(value = "shopMemberDeliveryAddressId", required = false, defaultValue = "0") Long shopMemberDeliveryAddressId,
            @RequestParam(value = "activityId", required = false) Long activityId,
            @RequestParam(value = "targetType", required = false) Integer targetType,
            Long storeId, Long memberId) {
        String version = request.getHeader("version");
        logger.info("小程序会员确认订单WxaMemberOrderController:productIdSkuIdCounts-" + productIdSkuIdCounts + ";storeId-" + storeId
                + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        //版本
        Integer wxVersion = HttpUtils.wxVersion(version, 140);

        try {
            checkLogin(storeId, memberId);
            ShopDetail shopDetail = shopDetailBack(storeId);
            MemberDetail memberDetail = memberDetailBack(memberId);

            //放入活动和version,做版本兼容
            ActivityInfoCache.createInstance(activityId, targetType, wxVersion);
//
//            List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
//            for (String productIdSkuIdCount : productIdSkuIdCounts) {
//                String[] split = productIdSkuIdCount.split("_");
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("productId", split[0]);
//                param.put("skuId", split[1]);
//                param.put("count", split[2]);
//                paramList.add(param);
//            }

            List<Map<String, String>> paySkuList = clearPaySkuList(orderSkuJsonArr, productIdSkuIdCounts);


            //获取商品详情和优惠券个数
            Map<String, Object> data = shopMemberOrderFacade.confirmOrder(paySkuList, shopDetail.getId(), memberDetail.getId(), version);
            //默认收货方式送货上门
            Map<String, Object> addressInfo = shopMemberOrderFacade.getAddressInfo(orderType, shopMemberDeliveryAddressId, shopDetail, memberDetail);
            //获取该用户的收货信息
            //默认到店取货
            addressInfo.put("dorderType", ShopMemberOrder.order_type_get_product_at_store);
            //商家名称
            addressInfo.put("dbusinessName", shopDetail.getStoreBusiness().getBusinessName());
            //到店取货地址
            addressInfo.put("daddress", shopDetail.getStoreBusiness().getBusinessAddress());
            data.put("storeBusiness", shopDetail.getStoreBusiness());
            data.put("addressInfo", addressInfo);
            couponServerNew.fillCoupon(data, shopDetail.getStoreBusiness().getId(), memberDetail.getMember().getId(), CouponSysEnum.WXA, false, 1, 100);
            return jsonResponse.setSuccessful().setData(data);
        } catch (TipsMessageException e) {
            logger.info(e.getFriendlyMsg());
            return jsonResponse.setError(e.getFriendlyMsg());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("确认订单的接口:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        } finally {
            ActivityInfoCache.clear();
        }

    }


    /**
     * 生成订单的接口
     *
     * @param
     * @param storeId
     * @param memberId http://wxalocal.yujiejie.com/mobile/memberOrder/addShopMemberOrder.json?
     *                     productIdSkuIdCount=6345_0_1
     *                     &orderType=1&
     *                     formId=d5d8f4e8ae1e4c632a85bf10a0fa9eb4&
     *                     deliveryAddressId=3
     *                     &buyWay=2
     *                     &activeId=120&
     *                     storeId=892&
     *                     memberId=6255
     * @return
     */
    @RequestMapping("/addShopMemberOrder")
    @ResponseBody
    public JsonResponse addShopMemberOrder(HttpServletRequest request, String formId,
                                           @RequestParam(value = "productIdSkuIdCount", required = false) String[] productIdSkuIdCount,
                                           @RequestParam(value = "orderSkuJsonArr", required = false) String orderSkuJsonArr,
                                           @RequestParam(value = "couponId", required = false, defaultValue = "0") long couponId,
                                           @RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType,
                                           @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
                                           @RequestParam(value = "deliveryAddressId", required = false, defaultValue = "0") long deliveryAddressId,
                                           @RequestParam(value = "activityId", required = false, defaultValue = "0") long activityId,
                                           @RequestParam(value = "buyWay", required = false, defaultValue = "0") int buyWay,//0 普通 1 团购 2 秒杀
                                          Long storeId,Long memberId, String carIds
    ) {
        // TODO: 2018/12/18 2018年12月18日10:55:52 添加了一个店中店标示参数type
        // 胡坤
        logger.info("生成订单接口参数productIdSkuIdCount:" + productIdSkuIdCount + ",couponId:" + couponId + ",orderType:" + orderType
                + ",remark:" + remark + ",deliveryAddressId:" + deliveryAddressId + ",buyWay：" + buyWay + ",activeId:" + activityId
                + ",storeId-" + storeId + ",memberId-" +memberId + ",orderSkuJsonArr="+orderSkuJsonArr);

        String version = request.getHeader("version");
        JsonResponse jsonResponse = new JsonResponse();

        try {
            checkLogin(storeId, memberId);
            MemberDetail member = memberDetailBack(memberId);


            //新版的下单,直播商品购买上线后,productIdSkuIdCount这个字段就不用了
            List<Map<String, String>> paySkuList = clearPaySkuList(orderSkuJsonArr, productIdSkuIdCount);

			if(buyWay==0){
                List<Map<String, Object>> maps = shopGoodsCarService.judgePaySNum(paySkuList);

                if (maps == null || maps.size() < 1) {

                } else {
                    Map<String, Object> stringObjectMap = maps.get(0);

                    Object isOk = stringObjectMap.get("isOk");

                    if (!(Boolean) isOk) {
                        return jsonResponse.setResultCode(-704, "无法结算！非常抱歉，您晚了一步，所选商品中有的库存不足了，请重新选择您要购买的商品").setData(maps);
                    }
                }
            }


//			if(carIds!=null && carIds.length()>0){
//                List<Map<String, Object>> maps = shopGoodsCarService.judgeCardsInventory(memberDetail.getId(), carIds.split(","));
//                if(maps==null || maps.size() <1){
//
//                }else {
//                    Map<String, Object> stringObjectMap = maps.get(0);
//
//                    Object isOk = stringObjectMap.get("isOk");
//
//                    if(!(Boolean) isOk){
//                        return jsonResponse.setResultCode(-704,"无法结算！非常抱歉，您晚了一步，所选商品中有的库存不足了，请重新选择您要购买的商品").setData(maps);
//                    }
//                }
//            }else {
//
//            }

            //版本
            Integer wxVersion = HttpUtils.wxVersion(request.getHeader("version"), 140);
            //放入活动和version,做版本兼容
            ActivityInfoCache.createInstance(activityId, buyWay, wxVersion);

            long orderId = shopMemberOrderFacade.addShopMemberOrder(buyWay, activityId, paySkuList, couponId, orderType, storeId,member.getMember(), formId, remark, deliveryAddressId, version, carIds);
            Map<String, Object> data = new HashMap<>();

            data.put("orderId", orderId);
            if (buyWay == ShopMemberOrder.buy_way_tuangou) {
                return jsonResponse.setResultCode(ResultCode.ACTIVE_JOIN_TEAM_SUCCESS).setData(data);
            }
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            if (e instanceof ResultCodeException) {
                ResultCodeException rce = (ResultCodeException) e;
                logger.info("生成订单的接口rce.getCode()" + rce.getCode() + ",rce.getMessage():" + rce.getMessage());
                return jsonResponse.setResultCode(rce.getCode(), rce.getMessage());
            } else {
                logger.info("生成订单的接口出现错误:" + e.getMessage());
                e.printStackTrace();
            }
            return jsonResponse.setError(e.getMessage());
        } finally {
            ActivityInfoCache.clear();
        }
    }

    private List<Map<String,String>> clearPaySkuList(String orderSkuJsonArr, String[] productIdSkuIdCount) {
        List<Map<String, String>> paySkuList = new ArrayList<>(8);
        boolean isNewOrder = StringUtils.isNotBlank(orderSkuJsonArr);
        if (isNewOrder) {
            JSONArray orderSkuJarr = JSONObject.parseArray(orderSkuJsonArr);
            if (! orderSkuJarr.isEmpty()) {
                for (int i = 0; i < orderSkuJarr.size(); i++) {
                    Map<String, String> skuMap = new HashMap<>(4);
                    JSONObject skuJson = orderSkuJarr.getJSONObject(i);
                    skuMap.put("productId", skuJson.getString("productId"));
                    skuMap.put("skuId", skuJson.getString("skuId"));
                    skuMap.put("count", skuJson.getString("count"));
                    //直播商品
                    Object liveProductId = skuJson.get("liveProductId");
                    skuMap.put("liveProductId", liveProductId == null ? "0" : String.valueOf(liveProductId));
                    paySkuList.add(skuMap);
                }
            }
        }
        else {
            for (String productIdSkuIdCountStr : productIdSkuIdCount) {
                String[] split = productIdSkuIdCountStr.split("_");
                Map<String, String> skuMap = new HashMap<>(4);
                skuMap.put("productId", split[0]);
                skuMap.put("skuId", split[1]);
                skuMap.put("count", split[2]);
                skuMap.put("liveProductId", "0");
                paySkuList.add(skuMap);
            }
        }
        return paySkuList;
    }


    /**
     * 获取订单列表
     *
     * @param orderStatus  订单状态：-1:全部;0:待付款;6:待提货 5：代发货 6 待收货 4:订单完成
     * @return
     */
    @RequestMapping("/getMemberOrderList")
    @ResponseBody
    public JsonResponse getMemberOrderList(@RequestParam("orderStatus") int orderStatus,
                                           @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                           Long storeId,Long memberId) {
        logger.info("获取订单列表WxaMemberOrderController:orderStatus-" + orderStatus + ";storeId-" +storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            Map<String, Object> data = new HashMap<String, Object>();
            SmallPage smallPage = shopMemberOrderService.getWxaMemberOrderList(orderStatus, storeId, memberId,
                    new Page<ShopMemberOrder>(current, size));
            data.put("smallPage", smallPage);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            logger.info("获取订单列表:" + e.getMessage() + "shopDetail.getId():" + storeId + ",memberDetail.getId()：" + memberId + ",current:" + current + ",size:" + size);
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 小程序取消订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/storeOrderByOrderId")
    @ResponseBody
    public JsonResponse storeOrderByOrderId(@RequestParam("orderId") long orderId,
                                            Long storeId,Long memberId) {
        logger.info("取消订单WxaMemberOrderController:orderId-" + orderId + ";storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            UserSession userSession = UserSession.getUserSession();
            userSession=new UserSession();
            userSession.setMemberId(memberId);
            userSession.setStoreId(storeId);
            //小程序会员取消
            int resonType = 1;
            String reason = "您已取消订单";
            memberOrderService.cancelOrder(userSession, orderId, resonType, reason);
//			int record = shopMemberOrderService.storeOrderByOrderId(orderId,shopDetail.getId(),memberDetail.getId(),resonType,reason);
//			if(record!=1){
//				return jsonResponse.setError("无法取消订单:orderId-"+orderId);
//			}
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取订单列表:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 小程序取消订单自动取消  hyq
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/cancelOrderAuto")
    @ResponseBody
    public JsonResponse cancelOrderAuto(@RequestParam("orderId") long orderId, int resonType, String reason,
                                        Long storeId,Long memberId) {
        logger.info("取消订单WxaMemberOrderController:orderId-" + orderId + ";storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId,memberId);
            UserSession userSession = UserSession.getUserSession();

            userSession=new UserSession();
            userSession.setMemberId(memberId);
            userSession.setStoreId(storeId);
            memberOrderService.cancelOrder(userSession, orderId, resonType, reason);
//			int record = shopMemberOrderService.storeOrderByOrderId(orderId,shopDetail.getId(),memberDetail.getId(),resonType,reason);
//			if(record!=1){
//				return jsonResponse.setError("无法取消订单:orderId-"+orderId);
//			}
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取订单列表:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 小程序订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderInfoByOrderId")
    @ResponseBody
    public JsonResponse getOrderInfoByOrderId(@RequestParam("orderId") long orderId,Long storeId,Long memberId) {
        logger.info("小程序订单详情WxaMemberOrderController:orderId-" + orderId + ";storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopDetail shopDetail = shopDetailBack(storeId);
            checkLogin(storeId, memberId);
            Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> shopMemberOrderInfo = shopMemberOrderService.getOrderInfoByOrderId(orderId,storeId,memberId);
            ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.findInformationById(orderId);
            data.put("shopMemberOrderInfo", shopMemberOrderInfo);
            if (shopMemberOrder.getOrderType() == ShopMemberOrder.order_type_delivery) {
                data.put("receiverName", shopMemberOrder.getReceiverName());
                data.put("address", shopMemberOrder.getReceiverAddress());
                data.put("phoneNumber", shopMemberOrder.getReceiverPhone());
            } else if (shopMemberOrder.getOrderType() == ShopMemberOrder.order_type_get_product_at_store) {
                data.put("storeBusiness", shopDetail.getStoreBusiness());
            }

            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            logger.error("获取订单列表:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 收藏订单商品
     *
     * @return
     */
    @RequestMapping("/addMemberOrderFavoriteProduct")
    @ResponseBody
    public JsonResponse addMemberOrderFavoriteProduct(Long storeId,Long memberId,
                                                      @RequestParam("orderId") long orderId) {
        logger.error("收藏订单商品:storeId" + storeId + "---memberId" + memberId + "---orderId" + orderId);
        JsonResponse jsonResponse = new JsonResponse();
        checkLogin(storeId, memberId);
        try {
            shopMemberOrderFacade.addMemberOrderFavoriteProduct(orderId, memberId, storeId);
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            logger.error("收藏订单商品:orderId" + orderId + "," + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 生成二维码接口http://dev.yujiejie.com:31080/miniapp/coupon/qrcode/src.do?storeId=1&memberId=1&orderId=1&width=400&height=400
     *
     * @param response
     * @param request
     * @param width
     * @param height
     */
    @RequestMapping("/qrcode/src")
    @ResponseBody
    public void qrcode(HttpServletResponse response, HttpServletRequest request,Long storeId,Long memberId,
                       @RequestParam("type") String type, @RequestParam("id") long id, @RequestParam("width") int width, @RequestParam("height") int height) {
        logger.info("生成二维码会员信息storeId:::::::::::::::" + storeId + ",memberId:" +memberId);
        String content = Constants.SERVER_URL + "/index.html" + "?type=" + type + "&id=" + id;
        content += "&memberId=" + memberId + "&storeId=" + storeId;
        Object obj = null;
        if ("dingdan".equals(type)) {
            obj = shopMemberOrderService.getMemberOrderById(id);
        }
        boolean rec = memcachedService.setCommon(MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME, type + "=" + id + "", 3 * DateConstants.SECONDS_PER_MINUTE, obj);
        logger.info("生成二维码接口缓存key[{}].放入缓存rec[{}]", MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME, rec);
        logger.info("生成二维码接口obj:" + obj);
        logger.info("生成二维码接口memcachedService:obj:" + memcachedService.getCommon(MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME,
                type + "=" + id + ""));
        logger.info("生成二维码，content+" + content);
        QRCodeUtil.getFile(request, response, content, "IMG" + memberId + "_" + type + "_" + id, width, height, null);
    }

    /**
     * 判断订单状态是否改变
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/checkMemberOrderStatus")
    @ResponseBody
    public JsonResponse checkMemberOrderStatus(@RequestParam("orderId") long orderId, @RequestParam("orderStatus") int orderStatus,
                                               Long storeId,Long memberId) {
        logger.info("判断订单状态是否改变WxaMemberOrderController:orderId-" + orderId + ";orderStatus-" + orderStatus + ";storeId-" +storeId + ";memberId-" +memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);

            Map<String, Object> data = new HashMap<String, Object>();
            boolean flag = shopMemberOrderService.checkMemberOrderStatus(orderId, orderStatus);
            data.put("flag", flag);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            logger.error("判断订单状态是否改变:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 获取提货二维码蒙版层的文案
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/getDeliveryOrderText")
    @ResponseBody
    public JsonResponse getDeliveryOrderText(@RequestParam("orderId") long orderId,
                                             Long storeId,Long memberId) {
        logger.info("获取提货二维码蒙版层的文案WxaMemberOrderController:orderId-" + orderId + ";storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        checkLogin(storeId, memberId);
        ShopDetail shopDetail = shopDetailBack(storeId);
        try {

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("text1", "提货二维码");
            data.put("text2", "请到门店，出示二维码取货");
            data.put("text3", "注意：提货前请先预约店家");
            data.put("text4", "联系电话");
            data.put("telePhoneNumber", shopDetail.getStoreBusiness().getPhoneNumber());
            data.put("text5", "二维码失效！");
            data.put("text6", "使用超时，请点击刷新再试");
            data.put("orderId", orderId);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            logger.error("获取提货二维码蒙版层的文案:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 选择收货方式的接口
     */
    @RequestMapping("/chooseDeliveryType")
    @ResponseBody
    public JsonResponse chooseDeliveryType(@RequestParam(value = "orderType", required = true) int orderType, Long storeId, Long memberId) {
        logger.info("小程序选择收货方式WxaMemberOrderController:" + "storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        checkLogin(storeId, memberId);
        ShopDetail shopDetail = shopDetailBack(storeId);
        try {
            List<Map<String, Object>> list = new ArrayList<>();
//			checkStore(shopDetail);
//   	 		checkMember(memberDetail);
            Map<String, Object> data = null;
            if (orderType == 0) {//到店取货
                data = new HashMap<String, Object>();
                data.put("address", shopDetail.getStoreBusiness().getBusinessAddress());
                data.put("businessName", shopDetail.getStoreBusiness().getBusinessName());
                list.add(data);
            }
            if (orderType == 1) {//送货上门
//                Wrapper<ShopMemberDeliveryAddress> wrapper = new EntityWrapper<ShopMemberDeliveryAddress>().
//                        eq("store_id", shopDetail.getId()).eq("shop_member_id", memberDetail.getId()).orderBy("last_used_time", false);
                List<ShopMemberDeliveryAddress> shopMemberDeliveryAddressList = shopMemberDeliveryAddressMapper.findDeliveryAddressByStoreMemberId(storeId,memberId);
                for (ShopMemberDeliveryAddress shopMemberDeliveryAddress : shopMemberDeliveryAddressList) {
                    data = new HashMap<String, Object>();
                    data.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
                    data.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());
                    data.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
                    String address = shopMemberDeliveryAddress.getLocation().replace("-", "") + shopMemberDeliveryAddress.getAddress();
                    data.put("address", address);
                    list.add(data);
                }
            }
            return jsonResponse.setSuccessful().setData(list);
        } catch (Exception e) {
            logger.error("选择收货方式:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 确认收货的接口
     */
    @RequestMapping("/confirmReceipt")
    @ResponseBody
    public JsonResponse confirmReceipt(@RequestParam(value = "shopMemberOrderId", required = true) long shopMemberOrderId,Long storeId,Long memberId) {
        logger.info("小程序确认收货WxaMemberOrderController:" + "storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            UserSession user = UserSession.getUserSession();
            user=new UserSession();
            user.setStoreId(storeId);
            user.setMemberId(memberId);
            memberOrderService.confirmReceipt(user, shopMemberOrderId);
//            同步订单状态
            memberOrderService.syncOrderType( shopMemberOrderId);

            return jsonResponse.setSuccessful().setData(new ArrayList<>());
        } catch (Exception e) {
            logger.error("小程序确认收货:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    //获取订单状态  图片   商品件数
    @RequestMapping("/getStatusAndImg")
    @ResponseBody
    public JsonResponse getStatusAndImg(@RequestParam(value = "shopMemberOrderId", required = true) long shopMemberOrderId, Long storeId,Long memberId) {
        logger.info("小程序获取物流上方的商品信息WxaMemberOrderController:" + "storeId-" + storeId + ";memberId-" + memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(shopMemberOrderId);
            if (shopMemberOrder == null) {
                return jsonResponse.setError("订单不存在：" + shopMemberOrderId);
            }
            String[] split = shopMemberOrder.getSummaryImages().split(",");
            Map<String, Object> map = new HashMap<>();
            map.put("count", shopMemberOrder.getCount());
            map.put("orderStatus", shopMemberOrder.getOrderStatus());
            map.put("image", split[0]);

            return jsonResponse.setSuccessful().setData(map);
        } catch (Exception e) {
            logger.error("小程序获取物流上方的商品信息:" + e.getMessage());
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 检验登陆状态
     */
    private void checkLogin(Long storeId, Long memberId) {
        if (storeId == null || memberId == null) {
            logger.warn("登陆参数为空storeId={}，memberId={}", storeId, memberId);
            throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
        }
    }

    /**
     * 查询store信息
     * @param storeId
     * @return
     */
    private ShopDetail shopDetailBack(Long storeId) {
        ShopDetail shopDetail = new ShopDetail();
        StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(storeId);
//        StoreBusiness storeBusiness = storeBusinessNewService.findStoreBusinessById(storeId);
        if (storeBusiness != null) {
            shopDetail.setStoreBusiness(storeBusiness);
            return shopDetail;
        }else {
            throw new RuntimeException("店铺信息不存在");
        }
    }
    /**
     * 查询member信息
     * @param memberId
     * @return
     */
    private MemberDetail memberDetailBack(Long memberId) {
        MemberDetail memberDetail = new MemberDetail();
//        ShopMember member = memberService.getMemberById(memberId);
        ShopMember member = memberService.findMemberById(memberId);
        if (member != null) {
            memberDetail.setMember(member);
            return memberDetail;
        }else {
            throw new RuntimeException("会员信息不存在");
        }
    }


    /**
     * 最新订单消息展示
     *
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie(唐静)
     * @date 2018/7/3 13:31
     */
    @RequestMapping("lastestOrderMessage")
    @ResponseBody
//    @Login
    public JsonResponse lastestOrderMessage(Long storeId) {
        return JsonResponse.getInstance().setSuccessful().setData(shopMemberOrderFacade.lastestOrderMessage(storeId));
    }


    /**
     * 用户即将成团的订单
     *
     * @param query query
     * @return ResponseResult
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    @ResponseBody
    @RequestMapping("/teamBuyActivityUnderwayList")
    public JsonResponse teamBuyActivityUnderwayList(ShopMemberOrderRbQuery query) {
        JsonExtResponse response = JsonExtResponse.me();
        try {
            MyPage<Map<String, Object>> result = memberOrderService.teamBuyActivityUnderwayList(query);
            Map<String, Object> extData = new HashMap<>(2);
            extData.put("tip", memberOrderService.tip("teamActivityUnderWay"));
            response.setExtData(extData);
            return response.setSuccessful().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return response.setError(e.getMessage());
        }
    }


    /**
     * 即将成团数量
     *
     * @param query query
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/8/10 8:33
     */
    @ResponseBody
    @RequestMapping("/teamBuyActivityUnderwayListCount")
    public JsonResponse teamBuyActivityUnderwayListCount(ShopMemberOrderRbQuery query) {
        JsonResponse response = JsonResponse.getInstance();
        try {
            return response.setSuccessful().setData(memberOrderService.teamBuyActivityUnderwayCount(query));
        } catch (Exception e) {
            e.printStackTrace();
            return response.setError(e.getMessage());
        }
    }


    /**
     * 用户已经成团的订单
     *
     * @param query query
     * @return ResponseResult
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    @ResponseBody
    @RequestMapping("/teamBuyActivityOKList")
    public JsonResponse teamBuyActivityOKList(ShopMemberOrderRbQuery query) {
        JsonExtResponse response = JsonExtResponse.me();
        try {
            //团购订单
            MyPage<Map<String, Object>> result = memberOrderService.teamBuyActivityOKList(query);
            response.setData(result);
            //用户手机号
            String phone = userService.getStoreBusinessPhoneById(query.getStoreId());
            HashMap<String, Object> extData = new HashMap<>();
            extData.put("phone", phone);
            extData.put("tip", memberOrderService.tip("teamActivityOK"));
            response.setExtData(extData);
            return response.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return response.setError(e.getMessage());
        }
    }


    @RequestMapping("/tip/{route}")
    @ResponseBody
    public JsonResponse tip(@PathVariable String route) {
        JsonResponse response = JsonResponse.getInstance();
        try {
            String tip = memberOrderService.tip(route);
            return response.setSuccessful().setData(tip);
        } catch (Exception e) {
            e.printStackTrace();
            return response.setSuccessful().setData("欢迎使用俞姐姐平台");
        }
    }

}