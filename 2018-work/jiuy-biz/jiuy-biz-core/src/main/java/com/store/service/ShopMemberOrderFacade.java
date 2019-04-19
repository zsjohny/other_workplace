package com.store.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jiuyuan.common.CouponRbRef;
import com.jiuyuan.dao.mapper.CommonRefMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.MyStoreActivityService;
import com.jiuyuan.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.store.dao.mapper.ShopMemberDeliveryAddressMapper;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.member.ShopMember;
import com.store.service.coupon.ShopMemberCouponService;
import com.store.service.store.ServiceAdviceFacade;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.util.ObjectUtils;


@Service
public class ShopMemberOrderFacade{
    private static final Log logger = LogFactory.get("ShopMemberOrderFacade");

    @Autowired
    private ShopMemberOrderService shopMemberOrderService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private PropertyServiceShop propertyService;
    @Autowired
    private ShopMemberCouponService shopMemberCouponService;

    @Autowired
    private WxaMemberFavoriteService wxaMemberFavoriteService;
    @Autowired
    private ServiceAdviceFacade serviceAdviceFacade;

    @Autowired
    private ShopMemberDeliveryAddressMapper shopMemberDeliveryAddressMapper;
    @Autowired
    private ShopMemberOrderMapper shopMemberOrderMapper;
    @Autowired
    private IMyStoreActivityService myStoreActivityService;

    @Autowired
    private ShopGoodsCarService shopGoodsCarService;

    @Autowired
    private TeamBuyActivityMapper teamBuyActivityMapper;
    @Autowired
    private SecondBuyActivityMapper secondBuyActivityMapper;

    @Autowired
    private CommonRefMapper commonRefMapper;

    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;

    @Autowired
    private ShopProductMapper shopProductMapper;

    /**
     * 确认订单
     * @param orderProductAndSkuInfo 下单的商品和sku信息
     */
    public Map<String, Object> confirmOrder(List<Map<String, String>> orderProductAndSkuInfo, long storeId, long memberId, String version) {
//		logger.info("会员确认订单ShopMemberOrderFacade:storeId-"+storeId+";paramList-"+paramList);
//		logger.info("会员确认订单ShopMemberOrderFacade:productInfos-"+productInfos);
        // int shopMemberCouponCount = shopMemberCouponService.getUsableShopMemberCouponListCount(storeId, memberId, (double) productInfos.get("allProductPrice"));
//		logger.info("会员确认订单ShopMemberOrderFacade:shopMemberCouponCount-"+shopMemberCouponCount);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> productInfos = getProductSkuList(orderProductAndSkuInfo, storeId, version);
        data.put("productInfos", productInfos);
        return data;
    }

    /**
     * 获取订单中的商品列表和商品总价
     *
     * @param paySkuList 下单的商品和sku信息
     * @return
     */
    private Map<String, Object> getProductSkuList(List<Map<String, String>> paySkuList, long storeId, String version) {
        double allProductPrice = 0;
        Map<String, Object> productInfos = new HashMap<String, Object>();
        List<Map<String, String>> productSKUList = new ArrayList<Map<String, String>>();
        long shopProductId = 0;
        int buyType = 0;//购买方式:0未参加活动、1参与团购、2参与秒杀
        long activityId = 0;//活动ID
        for (Map<String, String> paySku : paySkuList) {
            Map<String, String> product = new HashMap<String, String>();
            shopProductId = Long.parseLong(paySku.get("productId"));
            long skuId = Long.parseLong(paySku.get("skuId"));
            long count = Long.parseLong(paySku.get("count"));
            String liveProductIdStr = paySku.get("liveProductId");
            long liveProductId = StringUtils.isBlank(liveProductIdStr) ? 0L : Long.parseLong(liveProductIdStr);
            ShopProduct shopProduct = shopProductService.getShopProductById(shopProductId);
            if (shopProduct == null) {
                throw new RuntimeException("没有找到商品信息");
            }

            //=========================== 下架商品不让下单
            String name = shopProduct.getName();
            Integer soldOut = shopProduct.getSoldOut();
            if (! soldOut.equals(1)) {
                throw new RuntimeException("商品已下架");
            }
            Integer own = shopProduct.getOwn();
            if (own.equals(0)) {
                if (! "0".equals(shopProduct.getPlatformProductState())) {
                    throw new RuntimeException("商品已下架");
                }
            }
            //=========================== 下架商品不让下单

            Double price = Double.parseDouble("0");
            if (VersionUtil.lt(version, "1.3.4")) {
                price = shopProduct.getPrice();
                logger.info("小于1.3.4，商品价格采用商品价格");
            }
            else {
                logger.info("大于等于1.3.4，根据商品是否活动取活动价格");
                //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
                if (liveProductId > 0) {
                    //直播商品
                    Map<String, Object> liveProduct = shopProductMapper.findLiveProductByLiveProductId(liveProductId);
                    if (liveProduct == null) {
                        throw new RuntimeException("没有找到商品信息");
                    }
                    Object livePriceObj = liveProduct.get("livePrice");
                    if (livePriceObj == null) {
                        throw new RuntimeException("没有找到直播商品价格");
                    }
                    price = Double.parseDouble(livePriceObj.toString());
                }else {
                    //非直播商品
                    int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
                    if (intoActivity == 1) {//1表示参与团购活动
                        TeamBuyActivity teamBuyActivity = myStoreActivityService.getCurrentTeamBuyActivity(shopProduct.getId(), storeId);
                        if (teamBuyActivity != null) {
                            price = teamBuyActivity.getActivityPrice();
                            buyType = 1;//购买方式:0未参加活动、1参与团购、2参与秒杀
                            activityId = teamBuyActivity.getId();
                        }
                    }
                    else if (intoActivity == 2) {//2表示参与秒杀活动
                        SecondBuyActivity secondBuyActivity = myStoreActivityService.getCurrentSecondBuyActivity(shopProduct.getId(), storeId);
                        if (secondBuyActivity != null) {
                            price = secondBuyActivity.getActivityPrice();
                            buyType = 2;//购买方式:0未参加活动、1参与团购、2参与秒杀
                            activityId = secondBuyActivity.getId();
                        }
                    }
                    else {
                        price = shopProduct.getPrice();
                    }
                }

            }


            allProductPrice += price * count;


            product.put("productId", shopProductId + "");
            product.put("skuId", skuId + "");
            product.put("price", price + "");
            product.put("image", shopProduct.getFirstImage());
            product.put("name", name);
            product.put("count", count + "");

            //直接取这2个字段  -hyq
            if(skuId > 0){
                ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
                product.put("color", productSkuNew.getColorName());
                product.put("size", productSkuNew.getSizeName());
            }else {
                product.put("color", "");
                product.put("size", "");
            }
            productSKUList.add(product);
        }
        productInfos.put("productSKUList", productSKUList);

        String allProductPriceString = "0";
        Double allPrice = allProductPrice;
        if (allPrice.intValue() - allProductPrice == 0) {//判断是否符合取整条件
            allProductPriceString = allPrice.intValue() + "";
        }
        else {
            allProductPriceString = String.format("%.2f", allProductPrice);
        }

        productInfos.put("productId",shopProductId+"");
        productInfos.put("allProductPrice", Double.parseDouble(allProductPriceString));
        productInfos.put("buyType", buyType);//购买方式:0未参加活动、1参与团购、2参与秒杀
        productInfos.put("activityId", activityId);//活动ID
        return productInfos;
    }

    /**
     * 生成订单
     *
     * @param activeId
     * @param buyType
     * @param paySkuList
     * @param couponId
     * @param orderType
     * @param storeId
     * @param member
     * @param remark
     * @param deliveryAddressId
     */
    @Transactional( rollbackFor = Exception.class )
    public long addShopMemberOrder(int buyType, long activeId, List<Map<String, String>> paySkuList, long couponId, int orderType,
                                   long storeId, ShopMember member, String formId, String remark, long deliveryAddressId, String version, String carIds
                                   ) {
        Long memberId = member.getId ();
        logger.info("生成订单ShopMemberOrderFacade:skuInfo-" + paySkuList + ";couponId-" + couponId + ";orderType-" + orderType
                + ";storeId-" + storeId + ";memberId-" + memberId);
        if (deliveryAddressId != 0) {
            ShopMemberDeliveryAddress shopMemberDeliveryAddress1 = shopMemberDeliveryAddressMapper.selectById(deliveryAddressId);
            if (shopMemberDeliveryAddress1 == null) {
                throw new RuntimeException("收货地址不存在,请确认");
            }
        }

        ShopMemberOrder shopMemberOrder = new ShopMemberOrder();
        //判断订单类型
        //团购
        TeamBuyActivity historyTeam = null;
        if (buyType == 1 && activeId != 0) {
            boolean isCanJoinThisActivity = isCanJoinThisActivity (activeId, buyType, storeId, memberId);
            if (! isCanJoinThisActivity) {
                //当前逻辑ACTIVE_HAVE_JOIN
                throw new ResultCodeException(ResultCode.ACTIVE_HAVE_JOIN);
            }

//            historyTeam = teamBuyActivityMapper.findHistoryTeamById(activeId);
            historyTeam = teamBuyActivityMapper.selectById(activeId);
            if (historyTeam == null) {
                throw new ResultCodeException(ResultCode.ACTIVE_OVER_TIME);
            }

            //判断活动已结束
            if (historyTeam.haveActivityStatusInt() == 3) {
                throw new ResultCodeException(ResultCode.ACTIVE_OVER_TIME);
            }


            //购买数量
            Integer buyCount = acquireBuyCount (paySkuList);
            //抢购刷库存
            boolean rushResult = myStoreActivityService.rushBuyTeamBuyProduct(activeId, historyTeam.getConditionType (), buyCount, storeId);
            if (! rushResult) {
                throw new ResultCodeException(ResultCode.ACTIVE_SALE_OVER);
            }
            //填充订单活动信息
            shopMemberOrder.setBuyWay(buyType);
            shopMemberOrder.setTeamId(activeId);
        }

        //秒杀
        if (buyType == 2 && activeId != 0) {
            //已参加活动
            boolean isCanJoinThisActivity = isCanJoinThisActivity (activeId, buyType, storeId, memberId);
            if (!isCanJoinThisActivity) {
                throw new ResultCodeException(ResultCode.ACTIVE_HAVE_JOIN);
            }
            SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(activeId);
            //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
//            SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.findHaveActivityStatusById(activeId);
            int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
            //判断活动是否结束
            if (haveActivityStatusInt == 3) {
                throw new ResultCodeException(ResultCode.ACTIVE_PRODUCT_INSUFFICIENT);
            }

            //抢购秒杀商品
            Integer buyCount = acquireBuyCount (paySkuList);
            boolean rushResult = myStoreActivityService.rushSecondBuyProduct(activeId, buyCount, storeId);

            if (! rushResult) {
                throw new ResultCodeException(ResultCode.ACTIVE_SALE_OVER);
            }
            //填充订单活动信息
            shopMemberOrder.setBuyWay(buyType);
            shopMemberOrder.setSecondId(activeId);
        }
        long time = System.currentTimeMillis();
        Random random = new Random();
        shopMemberOrder.setOrderNumber("" + time / 1000 + (random.nextInt(9999999 - 1000000 + 1) + 1000000));
        shopMemberOrder.setStoreId(storeId);
        shopMemberOrder.setMemberId(memberId);


        //留言信息
        shopMemberOrder.setRemark(remark);
        //修改收货地址最后使用时间
        if (deliveryAddressId != 0 && orderType == ShopMemberOrder.order_type_delivery) {
            ShopMemberDeliveryAddress shopMemberDeliveryAddress = new ShopMemberDeliveryAddress();
            shopMemberDeliveryAddress.setLastUsedTime(System.currentTimeMillis());
            shopMemberDeliveryAddress.setId(deliveryAddressId);
            shopMemberDeliveryAddressMapper.updateById(shopMemberDeliveryAddress);
            //填充订单收货信息
            ShopMemberDeliveryAddress shopMemberDeliveryAddress1 = shopMemberDeliveryAddressMapper.findDeliveryAddressById(deliveryAddressId);
            shopMemberOrder.setReceiverPhone(shopMemberDeliveryAddress1.getPhoneNumber());
            shopMemberOrder.setReceiverName(shopMemberDeliveryAddress1.getLinkmanName());
            shopMemberOrder.setReceiverAddress(shopMemberDeliveryAddress1.getLocation().replace("-", "") + shopMemberDeliveryAddress1.getAddress());
        }
        Map<String, Object> productSkuList = getProductSkuList(paySkuList, storeId, version);
        logger.info("生成订单ShopMemberOrderFacade:productSkuList-" + productSkuList);
        double totalMoney = (double) productSkuList.get("allProductPrice");
        double expressMoney = 0;
        double totalExpressAndMoney = totalMoney + expressMoney;
        double saleMoney = 0;
        //使用了优惠券
        if (couponId > 0) {

            CouponRbRef couponRbRef =  commonRefMapper.selectCoupon(couponId,2);
            // ShopMemberCoupon shopMemberCoupon = shopMemberCouponService.getMemberCouponInfo(couponId);
            if (couponRbRef == null) {
                throw new RuntimeException("优惠券不存在,请确认");
            }
            if (couponRbRef.getLimitMoney().compareTo(new BigDecimal(totalExpressAndMoney)) > 0 ) {
                throw new RuntimeException("订单金额小于优惠券限制金额");
            }



            // 判断是使用折扣还是金额减免
            BigDecimal price = couponRbRef.getPrice();
            BigDecimal discount = couponRbRef.getDiscount();
            if(price!= null && price.compareTo(BigDecimal.ZERO) > 0) {
                saleMoney = couponRbRef.getPrice().doubleValue();
            } else if(discount!=null && discount.compareTo(BigDecimal.ZERO)>0) {
                discount = discount.divide(BigDecimal.TEN);
                saleMoney = BigDecimal.ONE.subtract(discount).multiply(new BigDecimal(totalMoney)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            shopMemberOrder.setCouponId(couponRbRef.getId());
            shopMemberOrder.setCouponName(couponRbRef.getName());
            shopMemberOrder.setCouponLimitMoney(couponRbRef.getLimitMoney().doubleValue());
            // 修改优惠券到已使用

            int rs =  commonRefMapper.updateCouponStatusMoney(couponId,Long.valueOf(shopMemberOrder.getOrderNumber()),0,1,BigDecimal.valueOf(totalMoney), BigDecimal.valueOf(saleMoney));
            if(rs==0) {
                throw new RuntimeException("优惠券核销失败");
            }
        }

        double payMoney = totalExpressAndMoney - saleMoney;
        if (payMoney < 0) {
            payMoney = 0;
        }
        shopMemberOrder.setTotalMoney(totalMoney);
        shopMemberOrder.setExpressMoney(expressMoney);
        shopMemberOrder.setTotalExpressAndMoney(totalExpressAndMoney);
        shopMemberOrder.setSaleMoney(saleMoney);
        shopMemberOrder.setPayMoney(payMoney);

        int count = 0;
        for (Map<String, String> map : paySkuList) {
            count += Integer.parseInt(map.get("count"));
        }
        shopMemberOrder.setCount(count);

        String summaryImages = "";
        int i = 0;
        List<Long> productIdList = new ArrayList<Long>();
        List<Long> skuIdList = new ArrayList<Long>();
        List<Map<String, String>> productSKUs = (List<Map<String, String>>) productSkuList.get("productSKUList");
        logger.info("生成订单ShopMemberOrderFacade:productSKUs-" + productSKUs);
        for (Map<String, String> productsku : productSKUs) {
//			if(i<4){
            long productId = Long.parseLong(productsku.get("productId"));
            long skuId = Long.parseLong(productsku.get("skuId"));
            boolean productContainsResult = productIdList.contains(productId);
            boolean skuContainsResult = skuIdList.contains(skuId);
            if (! productContainsResult && ! skuContainsResult) {
                if (i == 0) {
                    summaryImages += productsku.get("image");
                }
                else {
                    summaryImages = summaryImages + "," + productsku.get("image");
                }
                i++;
            }
            if (! productContainsResult) {
                productIdList.add(productId);
            }
            if (! skuContainsResult) {
                skuIdList.add(skuId);
            }
//			}
        }
        shopMemberOrder.setSummaryImages(summaryImages);
        //设置订单取货方式
        if (orderType == ShopMemberOrder.order_type_get_product_at_store) {
            shopMemberOrder.setOrderType(ShopMemberOrder.order_type_get_product_at_store);
        }
        else if (orderType == ShopMemberOrder.order_type_delivery) {
            shopMemberOrder.setOrderType(ShopMemberOrder.order_type_delivery);
        }

        if (payMoney == 0) {
            //判断订单类型：使用优惠券后金额为0 时  到店取货的订单状态为 待提货   送货上门的订单状态为待发货
            if (orderType == ShopMemberOrder.order_type_get_product_at_store) {
                shopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_paid);
            }
            else if (orderType == ShopMemberOrder.order_type_delivery) {
                shopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_pending_delivery);
            }
            shopMemberOrder.setPayTime(time);//付款时间
        }
        else {
            shopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_pending_payment);
        }
        shopMemberOrder.setUpdateTime(time);
        shopMemberOrder.setCreateTime(time);
        shopMemberOrder.setUserNickname(member.getUserNickname());
        shopMemberOrder.setPayFormId(formId);
        int record = shopMemberOrderService.addShopMemberOrder(shopMemberOrder);
        logger.info("生成订单ShopMemberOrderFacade:addOrderRecord-" + record);
        if (record != 1) {
            throw new RuntimeException("生成新订单失败");
        }


        //添加订单item
        addShopMemberOrderItem(paySkuList, shopMemberOrder);

//        if (couponId > 0) {
//            //核销优惠券
//            ShopMemberCoupon shopMemberCoupon = new ShopMemberCoupon();
//            shopMemberCoupon.setId(couponId);
//            long couponTime = System.currentTimeMillis();
//            shopMemberCoupon.setStatus(ShopMemberCoupon.status_used);
//            shopMemberCoupon.setAdminId(1L);
//            shopMemberCoupon.setCheckMoney(shopMemberCoupon.getMoney());
//            shopMemberCoupon.setCheckTime(couponTime);
//            shopMemberCoupon.setUpdateTime(couponTime);
//            boolean flag = shopMemberCouponService.updMemberCoupon(shopMemberCoupon);
//            if (! flag) {
//                throw new RuntimeException("核销优惠券失败couponId:" + shopMemberCoupon.getId());
//            }
//        }

        long orderId = shopMemberOrder.getId();

        //判断团购订单是否成团，成团则发送付款通知，成团以后的不管
        if (shopMemberOrder.getTeamId() != null) {
            TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(activeId);
            boolean isTeamSuccessTransient = ShopMemberOrderService.isTeamSuccessTransient(historyTeam, teamBuyActivity);
            if (isTeamSuccessTransient) {
                //查询所有该活动的订单且未代付款状态
                Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>()
                        .eq("team_id", activeId)
                        .eq("store_id", storeId)
                        .eq("order_status", ShopMemberOrder.order_status_pending_payment);//待付款
                List<ShopMemberOrder> shopMemberOrders = shopMemberOrderMapper.selectList(wrapper);
                for (ShopMemberOrder shopMemberOrder2 : shopMemberOrders) {
                    //发送付款通知
                    try {
                        serviceAdviceFacade.waitPayAdvice(shopMemberOrder2.getId());
                    } catch (Exception e) {
                        e.printStackTrace ();
                        logger.error ("团购刚好成团, 向用户推送通知失败");
                    }
                }
            }
        }

        //修改库存
        productSKUs.stream().forEach(action->{
            long skuId=0;
            int count1 =0;
            try {

                long productId = Long.parseLong(action.get("productId"));
//                平台供应商商品
                Integer own = 0;
                Integer ownProduct = shopProductMapper.findOwnProductByIdOwn(productId,own);

                if(ownProduct>0){
                    return;
                }

                skuId = Long.parseLong(action.get("skuId"));

                Integer remainCount = productSkuNewMapper.findRemainCountById(skuId);

                if(remainCount==0){
                    return;
                }

                ProductSkuNew entity = new ProductSkuNew();
                entity.setId(skuId);
                count1 = remainCount -Integer.parseInt(action.get("count"));
                entity.setRemainCount(count1 >0 ? count1:0);
                productSkuNewMapper.updateById(entity);
            } catch (Exception e) {
                String msg = "下订单更新库存是吧 sku:" + skuId + ",+ " + count1;
                logger.error(msg);
                throw BizException.defulat().msg(msg);
            }
        });


        // 删除 购物车中的商品
        shopGoodsCarService.deleteCarGoodsByIds(memberId, carIds);

        //返回订单编号,让前端支付
        return orderId;
    }


    /**
     * 用户购买sku数量
     * <p>目前仅支持购买一种sku</p>
     *
     * @param skuInfo skuInfo
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/8/2 19:45
     */
    private Integer acquireBuyCount(List<Map<String, String>> skuInfo) {
        if (ObjectUtils.isEmpty (skuInfo)) {
            throw new ResultCodeException (ResultCode.ACTIVITY_NO_SKU_INFO);
        }
        if (skuInfo.size () > 1) {
            throw new ResultCodeException(ResultCode.ACTIVITY_TOO_MANY_SKU_INFO);
        }
        return Integer.valueOf (skuInfo.get (0).get ("count"));
    }


    /**
     * 是否可以参加这次活动
     *
     * @param activityId activityId
     * @param whichActivity 哪种活动 1:团购活动, 2:秒杀活动
     * @param storeId storeId
     * @param memberId memberId
     * @return true 可以参加
     * @author Charlie
     * @date 2018/8/1 15:05
     */
    private boolean isCanJoinThisActivity(Long activityId, Integer whichActivity, Long storeId, Long memberId) {
        if (whichActivity == MyStoreActivityService.TEAM_ACTIVITY) {
            return isJoinTeamActReady(activityId, storeId, memberId);
        }
        else if (whichActivity == MyStoreActivityService.SECOND_ACTIVITY) {
            return isJoinSecondActReady (activityId, storeId, memberId);
        }
        else {
            logger.error ("判断用户是否参加活动, 未知的活动类型 whichActivity:"+whichActivity);
            throw new RuntimeException ("未知的活动类型 whichActivity:"+whichActivity);
        }
    }


    /**
     * 已参加秒杀活动
     *
     * @param scondId scondId
     * @param storeId storeId
     * @param memberId memberId
     * @return true 已参加
     * @author Charlie
     * @date 2018/8/1 15:05
     */
    private boolean isJoinSecondActReady(Long scondId, Long storeId, Long memberId) {
        Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder> ()
                .eq("member_id", memberId)
                .eq("second_id", scondId)
                .eq("store_id", storeId)
                .ne("order_status", 3);
        return shopMemberOrderMapper.selectCount(wrapper) <= 0;
    }


    /**
     * 用户是否已参加团购
     *
     * @param teamId 团购id
     * @param storeId storeId
     * @param memberId memberId
     * @return boolean
     * @author Charlie
     * @date 2018/8/1 14:11
     */
    private boolean isJoinTeamActReady(Long teamId, Long storeId, Long memberId) {
        Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder> ()
                .eq("member_id", memberId)
                .eq("team_id", teamId)
                .eq("store_id", storeId)
                .ne("order_status", 3);
        //判断是否参加过从活动
        return shopMemberOrderMapper.selectCount(wrapper) <=0;
    }
    /**
     * 添加订单item
     *
     * @param paramList 商品信息封装 :productId ,skuId, count
     * @param shopMemberOrder 订单
     */
    private void addShopMemberOrderItem(List<Map<String, String>> paramList, ShopMemberOrder shopMemberOrder) {
        for (Map<String, String> map : paramList) {
            long productId = Long.parseLong(map.get("productId"));
            long skuId = Long.parseLong(map.get("skuId"));
            int productSKUCount = Integer.parseInt(map.get("count"));
            Long liveProductId = Long.parseLong(map.get("liveProductId"));
            ShopProduct shopProduct = shopProductService.getShopProductById(productId);
            ShopMemberOrderItem shopMemberOrderItem = new ShopMemberOrderItem();
            shopMemberOrderItem.setOrderId(shopMemberOrder.getId());
            shopMemberOrderItem.setOrderNumber(shopMemberOrder.getOrderNumber());
            shopMemberOrderItem.setShopProductId(shopProduct.getId());
            shopMemberOrderItem.setOwn(shopProduct.getOwn());
            shopMemberOrderItem.setLiveProductId(liveProductId);
            ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);

            shopMemberOrderItem.setColor(productSkuNew.getColorName());
            shopMemberOrderItem.setSize(productSkuNew.getSizeName());

            //判断是否平台商品
            if (shopMemberOrderItem.getOwn() == ShopMemberOrderItem.platform_product) {
                shopMemberOrderItem.setProductId(shopProduct.getProductId());
                shopMemberOrderItem.setSupplierCount(productSKUCount);
                //判断sku是否大于0
//                if (skuId > 0) {
//                    ProductSKU productSKU = productSKUService.getProductSKU(skuId);
//                    if (productSKU.getProductId() != shopProduct.getProductId()) {
//                        throw new RuntimeException("精品商品和SKU的商品ID不一致");
//                    }
//                    //如果是平台商品，将对应的sku的颜色，尺码查询出来
//                    String propertyIds = productSKU.getPropertyIds();
//                    if (! StringUtils.isEmpty(propertyIds)) {
//                        String[] split = propertyIds.split(";");
//                        for (String string : split) {
//                            String[] property = string.split(":");
//                            long propertyNameId = Long.parseLong(property[0]);
//                            long propertyValueId = Long.parseLong(property[1]);
//                            if (propertyNameId == 7) {
//                                shopMemberOrderItem.setColor(propertyService.getPropertyValueById(propertyValueId));
//                            }
//                            else if (propertyNameId == 8) {
//                                shopMemberOrderItem.setSize(propertyService.getPropertyValueById(propertyValueId));
//                            }
//                        }
//                    }
//                }
            }

            if (shopMemberOrderItem.getOwn().intValue()==2) {
                shopMemberOrderItem.setProductId(shopProduct.getProductId());
                if (skuId > 0) {
                    if(productSkuNew.getRemainCount().intValue()>=productSKUCount){
                        //自营库存
                        shopMemberOrderItem.setSelfCount(productSKUCount);
                    }else {
                        shopMemberOrderItem.setSelfCount(productSkuNew.getRemainCount());
                        shopMemberOrderItem.setSupplierCount(productSKUCount-productSkuNew.getRemainCount().intValue());
                    }
                }
            }

            //自有商品
            if (shopProduct.getOwn().equals(ShopMemberOrderItem.OWN_PRODUCT)) {
//                shopMemberOrderItem.setColor(productSkuNew.getColorName());
//                shopMemberOrderItem.setSize(productSkuNew.getSizeName());
                shopMemberOrderItem.setSelfCount(productSKUCount);
            }
            Double price;
            if (liveProductId > 0) {
                Map<String, Object> liveProduct = shopProductMapper.findLiveProductByLiveProductId(liveProductId);
                if (liveProduct == null) {
                    throw new RuntimeException("没有找到商品信息");
                }
                Object livePriceObj = liveProduct.get("livePrice");
                if (livePriceObj == null) {
                    throw new RuntimeException("没有找到直播商品价格");
                }
                price = Double.parseDouble(livePriceObj.toString());
                logger.info("采用直播商品价格 liveProductId={},price={}", liveProductId, price);
            }
            else {
                price = shopProduct.getPrice();
            }
            shopMemberOrderItem.setPrice(price);


            shopMemberOrderItem.setProductSkuId(skuId);
            shopMemberOrderItem.setCount(productSKUCount);
            shopMemberOrderItem.setSummaryImages(shopProduct.getFirstImage());
            shopMemberOrderItem.setName(shopProduct.getName());
            shopMemberOrderItem.setCreateTime(shopMemberOrder.getCreateTime());
            shopMemberOrderItem.setUpdateTime(shopMemberOrder.getCreateTime());
            int shopMemberOrderItemRecord = shopMemberOrderService.addShopMemberOrderItem(shopMemberOrderItem);
            logger.info("生成订单ShopMemberOrderFacade:addOrderItemRecord-" + shopMemberOrderItemRecord + ";productId-" + productId + ";skuId-" + skuId);
            if (shopMemberOrderItemRecord != 1) {
                throw new RuntimeException("生成订单详情失败:paramMap" + map);
            }
        }
    }

    /**
     * 收藏订单商品
     */
    @Transactional( rollbackFor = Exception.class )
    public void addMemberOrderFavoriteProduct(long orderId, long memberId, long storeId) {
        List<ShopMemberOrderItem> memberOrderItemList = shopMemberOrderService.findProductListByOrderId(orderId);
        List<Long> shopProductIdList = new ArrayList<Long>();
        for (ShopMemberOrderItem shopMemberOrderItem : memberOrderItemList) {
            long shopProductId = shopMemberOrderItem.getShopProductId();
            if (shopProductIdList.contains(shopProductId)) {
                continue;
            }
            wxaMemberFavoriteService.addMemberFavoriteRecord(memberId, shopProductId, 0, 0, storeId);
            shopProductIdList.add(shopProductId);
        }
    }

    public Map<String, Object> chooseDeliveryWay(int otherPage, ShopDetail shopDetail, MemberDetail memberDetail) {
        Map<String, Object> addressInfo = new HashMap<>();
        if (otherPage == 0) {//判断是否从编辑保存或者新增保存页面调用接口0 ：正常调用接口 1：从其他页面调用接口
            Map<String, Object> map = shopMemberOrderMapper.selectOrderType(memberDetail.getId(), shopDetail.getId());
            if (map == null || (int) map.get("orderType") == 0) {
                addressInfo = getAtStoreAddress(shopDetail, shopDetail);
            }
            if (map != null && (int) map.get("orderType") == 1) {//送货上门
                //查找最后一次使用的收货地址
                Long time = shopMemberDeliveryAddressMapper.selectTime(memberDetail.getId(), shopDetail.getId());
                Map<String, Object> addressMap = shopMemberDeliveryAddressMapper.selectLastUsedAddress(memberDetail.getId(), shopDetail.getId(),time);
                if (addressMap == null) {
                    //获取到店取货地址
                    addressInfo = getAtStoreAddress(shopDetail, shopDetail);
                }
                else {
                    //获取送货送货上门地址
                    addressInfo = getDeliveryAddress(shopDetail, shopDetail, addressMap);
                }
            }
        }
        if (otherPage == 1) {//从编辑保存，新增保存，删除页面跳转到确认订单页面时不查询上次的收货方式，仅当上次的送货上门地址被删除时默认为到店取货
            //查找编辑保存和新增保存的地址
            Long time = shopMemberDeliveryAddressMapper.selectTime(memberDetail.getId(), shopDetail.getId());
            Map<String, Object> addressMap = shopMemberDeliveryAddressMapper.selectLastUsedAddress(memberDetail.getId(), shopDetail.getId(),time);
            if (addressMap == null) {
                addressInfo = getAtStoreAddress(shopDetail, shopDetail);
            }
            else {
                addressInfo = getDeliveryAddress(shopDetail, shopDetail, addressMap);
            }
        }
        return addressInfo;
    }

    /**
     * 送货上门获取收货地址信息
     *
     * @param shopDetail
     * @param shopDetail2
     * @param addressMap
     * @return
     */
    private Map<String, Object> getDeliveryAddress(ShopDetail shopDetail, ShopDetail shopDetail2, Map<String, Object> addressMap) {
        Map<String, Object> addressInfo = new HashMap<>();
        addressInfo.put("orderType", 1);
        addressInfo.put("deliveryAddressId", addressMap.get("deliveryAddressId"));
        addressInfo.put("address", ((String) addressMap.get("location")).replace("-", "") + (String) addressMap.get("address"));
        addressInfo.put("phoneNumber", addressMap.get("phoneNumber"));
        addressInfo.put("linkmanName", addressMap.get("linkmanName"));
        return addressInfo;
    }

    /**
     * 到店提货
     *
     * @param shopDetail
     * @param shopDetail2
     * @return
     */
    private Map<String, Object> getAtStoreAddress(ShopDetail shopDetail, ShopDetail shopDetail2) {
        Map<String, Object> addressInfo = new HashMap<>();
        addressInfo.put("orderType", 0);//默认到店取货
        addressInfo.put("businessName", shopDetail.getStoreBusiness().getBusinessName());//商家名称
        addressInfo.put("address", shopDetail.getStoreBusiness().getBusinessAddress());//到店取货地址
        return addressInfo;
    }

    private Map<String, Object> getAtStoreAddress(ShopDetail shopDetail) {
        return this.getAtStoreAddress(shopDetail, shopDetail);
    }

    /**
     * 获取收货方式以及地址信息
     */
    public Map<String, Object> getAddressInfo(int orderType, Long shopMemberDeliveryAddressId, ShopDetail shopDetail,
                                              MemberDetail memberDetail) {
        Map<String, Object> addressInfo = new HashMap<>();
        if (orderType == ShopMemberOrder.order_type_get_product_at_store) {
            //到店取货
            addressInfo = getAtStoreAddress(shopDetail);
            addressInfo.put("hasAddressInfo", 1);//是否有收货地址 0:无收货地址  1:有收货地址
        }

        else {
            if (shopMemberDeliveryAddressId != 0) {
                //送货上门，有确定收货地址
                ShopMemberDeliveryAddress shopMemberDeliveryAddress = shopMemberDeliveryAddressMapper.selectShopMemberDeliveryAddress(shopMemberDeliveryAddressId);
//                ShopMemberDeliveryAddress shopMemberDeliveryAddress = shopMemberDeliveryAddressMapper.selectById(shopMemberDeliveryAddressId);
                if (shopMemberDeliveryAddress == null) {
                    logger.info("没有该收货地址！shopMemberDeliveryAddressId：" + shopMemberDeliveryAddressId);
                    throw new TipsMessageException("没有该收货地址！");
                }
                addressInfo.put("hasAddressInfo", 1);//是否有收货地址 0:无收货地址  1:有收货地址
                addressInfo.put("orderType", ShopMemberOrder.order_type_delivery);
                addressInfo.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
                addressInfo.put("address", (shopMemberDeliveryAddress.getLocation()).replace("-", "") + shopMemberDeliveryAddress.getAddress());
                addressInfo.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
                addressInfo.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());

            }
            else {
                //送货上门，无确定收货地址
                Map<String, Object> addressMap = null;
                //Long time = shopMemberDeliveryAddressMapper.selectTime(memberDetail.getId(), shopDetail.getId());
                //addressMap = shopMemberDeliveryAddressMapper.selectLastUsedAddress(memberDetail.getId(), shopDetail.getId(),time);
                List<ShopMemberDeliveryAddress> shopMemberDeliveryAddresses = shopMemberDeliveryAddressMapper.selectAddress(memberDetail.getId());
                if (shopMemberDeliveryAddresses.size() ==0) {
                    addressInfo.put("hasAddressInfo", 0);//是否有收货地址 0:无收货地址  1:有收货地址
                    addressInfo.put("orderType", ShopMemberOrder.order_type_delivery);
                    addressInfo.put("deliveryAddressId", "");
                    addressInfo.put("address", "");
                    addressInfo.put("phoneNumber", "");
                    addressInfo.put("linkmanName", "");
                }
                else {
                    for (ShopMemberDeliveryAddress shopMemberDeliveryAddress : shopMemberDeliveryAddresses) {
                        if (shopMemberDeliveryAddress.getDefaultStatus()==1){
                            addressInfo.put("hasAddressInfo", 1);//是否有收货地址 0:无收货地址  1:有收货地址
                            addressInfo.put("orderType", ShopMemberOrder.order_type_delivery);
                            addressInfo.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
                            addressInfo.put("address", ((String) shopMemberDeliveryAddress.getLocation()).replace("-", "") + (String) shopMemberDeliveryAddress.getAddress());
                            addressInfo.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
                            addressInfo.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());
                            break;
                        }else {
                            addressInfo.put("hasAddressInfo", 1);//是否有收货地址 0:无收货地址  1:有收货地址
                            addressInfo.put("orderType", ShopMemberOrder.order_type_delivery);

//                            addressInfo.put("deliveryAddressId", addressMap.get("deliveryAddressId"));
//                            addressInfo.put("address", ((String) addressMap.get("location")).replace("-", "") + (String) addressMap.get("address"));
//                            addressInfo.put("phoneNumber", addressMap.get("phoneNumber"));
//                            addressInfo.put("linkmanName", addressMap.get("linkmanName"));
                            addressInfo.put("deliveryAddressId", shopMemberDeliveryAddress.getId());
                            addressInfo.put("address", ((String) shopMemberDeliveryAddress.getLocation()).replace("-", "") + (String) shopMemberDeliveryAddress.getAddress());
                            addressInfo.put("phoneNumber", shopMemberDeliveryAddress.getPhoneNumber());
                            addressInfo.put("linkmanName", shopMemberDeliveryAddress.getLinkmanName());

                        }
                    }
                }
            }
        }
        return addressInfo;
    }


    /**
     * 最新订单消息展示
     *
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie(唐静)
     * @date 2018/7/3 13:31
     */
    public Object lastestOrderMessage(Long StoreId) {
        String buyer = FortuneTeller.intitle();
//        String city = CityRandom.acquireRandomCity();

        //price>0,随机取,sql中体现
        ShopProduct query = new ShopProduct();
        query.setStatus(ShopProduct.normal_status);
        query.setSoldOut(ShopProduct.sold_out_up);
        query.setStoreId(StoreId);
        //获取门店一个随机的商品
        ShopProduct product = shopProductService.randomProduct(query);
        // 如果门店没有,搞个假的
        if (product == null) {
//            product = VirtualProduct.create();
            return null;
        }

        //这里配置成1~3件
        int buyCount = new Random().nextInt(3) + 1;
        BigDecimal totalMoney = new BigDecimal(String.valueOf(buyCount)).multiply(new BigDecimal(String.valueOf(product.getPrice())));

        //商品名最多保留8位
        String productName = product.getName().length() > 8 ? product.getName().substring(0, 8) : product.getName();

        return new StringBuilder(buyer).append(" ").append("刚刚买了").append(buyCount).append("件")
                .append(productName).append(", 共￥").append(totalMoney.intValue()).append("元");
    }



    /**
     * 随机生成姓名
     *
     * @author Charlie唐静)
     * @date 2018/7/3 14:52
     */
    private static class FortuneTeller{
        private static final String[] FAMILY_NAME = {
                "张", "王", "陈", "李", "周", "吴", "郑", "钱", "杨", "孙"
                , "赵", "林", "蒋", "沈", "韩", "冯", "朱", "秦", "尤", "许"
                , "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏"
                , "陶", "姜", "鲁", "韦", "昌", "马", "苗", "凤", "贾", "方"
                , "毛", "段", "习", "温", "邓", "夏", "崔", "江", "姚", "唐"
                , "项", "乐", "岳", "向", "郝", "西门", "童", "杜", "左", "胡"
                , "俞", "任", "袁", "柳", "鲍", "史", "方", "叶", "魏", "卫"
        };
        private static final String ONE_ASTERISK = "*";
        private static final String DOUBLE_ASTERISK = "**";

        /**
         * 获取全名
         *
         * @return java.lang.String
         * @author Charlie(唐静)
         * @date 2018/7/3 14:44
         */
        public static String intitle() {
            //姓
            String familyName = acquireFamilyName();
            //名
            String name = new Random().nextInt(2) == 0 ? ONE_ASTERISK : DOUBLE_ASTERISK;
            return familyName + name;
        }

        /**
         * 获取姓氏(越靠前命中率越大)
         *
         * @return java.lang.String
         * @author Charlie(唐静)
         * @date 2018/7/3 14:44
         */
        private static String acquireFamilyName() {
            String familyName;
            int index = FAMILY_NAME.length;
            Random random = new Random();
            int i1 = random.nextInt(index);
            int i2 = random.nextInt(index);
            if (i1 < i2) {
                familyName = FAMILY_NAME[i1];
            }
            else {
                familyName = FAMILY_NAME[i1 - i2];
            }
            return familyName;
        }

    }


    /**
     * 随机生成城市名
     *
     * @author Charli(唐静)
     * @date 2018/7/3 14:52
     */
    private static class CityRandom{

        private static final String[] CITY = {
                "杭州", "宁波", "上海", "温州", "金华", "嘉兴", "湖州", "丽水", "绍兴", "北京"
                , "广州", "深圳", "汕头", "南京", "镇江", "常州", "无锡", "苏州", "徐州", "连云港"
                , "惠州", "珠海", "佛山", "衢州", "舟山", "台州", "南昌", "九江", "赣州", "吉安"
                , "鹰潭", "上饶", "沈阳", "大连", "鞍山", "抚顺", "石家庄", "唐山", "保定", "张家口"
                , "合肥", "蚌埠", "芜湖", "淮南", "安庆", "马鞍山", "宣城", "福州", "厦门", "泉州"
                , "三明", "南平", "漳州", "莆田", "长沙", "株洲", "湘潭", "武汉", "宜昌", "郑州"
                , "洛阳", "开封", "漯河", "安阳", "太原", "大同", "西安", "咸阳", "铜川", "杭州"
        };

        private static final String[] HOT_CITY = {
                "杭州", "宁波", "上海", "温州", "广州", "深圳", "无锡", "苏州", "南京"
        };

        private static final int RANDOM_FACTOR = 5;

        /**
         * 获得一个随机的城市名
         *
         * @author Charlie(唐静)
         * @date 2018/7/3 15:17
         */
        public static String acquireRandomCity() {
            Random random = new Random();
            int hotCityBoundary = HOT_CITY.length * RANDOM_FACTOR;

            //命中热门城市
            int hostCityIndex = random.nextInt(hotCityBoundary);
            if (hostCityIndex < HOT_CITY.length) {
                return HOT_CITY[hostCityIndex];
            }

            return CITY[random.nextInt(CITY.length)];
        }

    }

    /**
     * 随机编的商品信息
     *
     * @author Charlie(静)
     * @date 2018/7/3 17:26
     */
    private enum VirtualProduct{
        P1(0, 38.0, "纯色压褶半身裙"),
        P2(1, 85.0, "休闲格子衬衫"),
        P3(2, 90.0, "吊带阔腿裤"),
        P4(3, 76.0, "银丝哈伦九分裤"),
        P5(4, 18.2, "立体贴花短袖衬衣"),
        P6(5, 35.0, "欧版印花合身裙"),
        P7(6, 49.0, "格子修身连衣裙"),
        P8(7, 37.8, "中袖短款小衫");

        private Integer index;
        private Double price;
        private String name;

        VirtualProduct(Integer index, Double price, String name) {
            this.index = index;
            this.price = price;
            this.name = name;
        }



        /**
         * 创建一个虚拟的商品
         *
         * @return com.jiuyuan.entity.newentity.ShopProduct
         * @author Charlie(唐静)
         * @date 2018/7/3 17:14
         */
        public static ShopProduct create() {
            int i = new Random().nextInt(8);
            VirtualProduct random = P1;
            for (VirtualProduct virtualProduct : VirtualProduct.values()) {
                if (virtualProduct.index.equals(i)) {
                    break;
                }
            }

            ShopProduct shopProduct = new ShopProduct();
            shopProduct.setPrice(random.getPrice());
            shopProduct.setName(random.getName());
            return shopProduct;
        }

        public Double getPrice() {
            return price;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}