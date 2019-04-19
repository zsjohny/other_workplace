package com.e_commerce.miscroservice.store.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.order.StoreOrderItem;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.conver.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.store.dao.*;
import com.e_commerce.miscroservice.store.entity.*;
import com.e_commerce.miscroservice.store.entity.emuns.OrderStatus;
import com.e_commerce.miscroservice.store.entity.emuns.RefundOrderActionLogEnum;
import com.e_commerce.miscroservice.store.entity.emuns.RefundStatus;
import com.e_commerce.miscroservice.store.entity.response.RefundOrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.*;
import com.e_commerce.miscroservice.store.mapper.StoreOrderMapper;
import com.e_commerce.miscroservice.store.mapper.StoreRefundOrderMapper;
import com.e_commerce.miscroservice.store.mapper.WXOrderMapper;
import com.e_commerce.miscroservice.store.service.StoreRefundOrderService;
import com.e_commerce.miscroservice.store.utils.BizUtil;
import com.e_commerce.miscroservice.store.utils.GlobalSettingName;
import com.e_commerce.miscroservice.store.utils.NumberUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 售后
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:27
 * @Copyright 玖远网络
 */
@Service
public class StoreRefundOrderServiceImpl implements StoreRefundOrderService {

    private Log logger = Log.getInstance(StoreRefundOrderServiceImpl.class);

    private static final long FEB_TENTH = 1518192000000L;
    @Autowired
    private StoreRefundOrderMapper storeRefundOrderMapper;
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private WXOrderMapper wxOrderMapper;
    @Resource
    private StoreRefundOrderDao storeRefundOrderDao;

    @Resource
    private StoreOrderDao storeOrderDao;

    @Resource
    private SupplierUserDao supplierUserDao;

    @Resource
    private BrandDao brandDao;

    @Resource
    private StoreRefundOrderActionLogDao storeRefundOrderActionLogDao;

    @Override
    public Response getRefundOrderList(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        logger.info("userId={}查询售后订单列表",userId);
        PageInfo<RefundOrderListResponse> list = storeRefundOrderDao.findRefundOrderListByUserId(pageNum,pageSize,userId);
        return Response.success(list);
    }

    @Override
    public Response applyRefund1(Long orderNo, long storeId, Integer refundType, Long SkuId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(orderNo);
        long currentTime = System.currentTimeMillis();
        if(currentTime >= FEB_TENTH && currentTime <= FEB_TENTH){
            logger.info("目前工厂处于休假状态，平台暂停受理退款退货服务:orderNo:"+orderNo+",storeId:"+storeId);
            return Response.errorMsg("亲爱的客户：目前工厂处于休假状态，平台暂停受理退款退货服务，于2018年3月3日重新开始受理售后，为您带来的不便敬请谅解。");
        }
        Integer orderStatus = storeOrderNew.getOrderStatus();
        // 先进行判断是否订单
        if (orderStatus !=10&& orderStatus != 50) {
            logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
            return Response.errorMsg("该订单不在售后期限内，无法申请售后！");
        }
        StoreRefundOrder  refundOrder = storeRefundOrderMapper.selectRefundOrder(orderNo, SkuId);
        //该订单是否在售后中
//        if (refundOrder!=null){
//            if(storeOrderNew.getRefundUnderway() == 1){
//                logger.error("当前订单已有进行中的售后单。不能再申请售后。");
//                return Response.errorMsg("当前订单已有进行中的售后单。不能再申请售后。");
//            }
//        }
        List<StoreRefundOrder> refundList = storeRefundOrderMapper.selectByOrderOrderNo(orderNo);
//       if (refundList!=null){
//           for (StoreRefundOrder storeRefundOrder : refundList) {
//               //再判断是否已经该订单是否已经在售后或者售后成功
//                   Integer refundStatus = storeRefundOrder.getRefundStatus();
//                   if (refundStatus == 1 ||
//                           refundStatus == 3 ||
//                           refundStatus == 2
//                          ) {
//                       return Response.errorMsg("该订单正在售后中请勿重复申请!");
//                   }
////                   if (refundStatus==4){
////                       return Response.errorMsg("该订单已有成功退款记录不可在申请退款");
////                   }
//           }
//       }
            //判断该订单状态是否能够申请为退货退款
            if(refundType ==RefundOrder.refundType_refund_and_product && orderStatus ==10){
                logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
                return Response.errorMsg("该订单无法申请退货退款！");
            }
            double expressMoney = storeOrderNew.getTotalExpressMoney();
            //获取订单信息
            Map<String,Object> orderMap = new HashMap<String,Object>();
            map.put("orderNo", storeOrderNew.getOrderNo());
            map.put("totalExpressMoney",BizUtil.savepoint(expressMoney,2) );

            Map<String, Object> refuseMap = new HashMap<String,Object>();

            if(refundType == RefundOrder.refundType_refund){
                refuseMap = getRefuseReasonList(3);
            }
            if(refundType == RefundOrder.refundType_refund_and_product){
                refuseMap = getRefuseReasonList(4);
            }
            // 最大退款额为实付金额（不含邮费）

            // 售后类型 1：仅退款 2：退货退款
            map.put("refundType", refundType);


            // 最大退款金额
            double MostrefundFee = storeOrderNew.getTotalPay() ;

        // 最大退款金额
            //map.put("MostrefundFee", BizUtil.savepoint(MostrefundFee,2));

            StoreOrderItemNew storeOrderItem = storeRefundOrderMapper.selectOrderItem(orderNo, SkuId);
            //最大退款金额
            Double totalPay = storeOrderItem.getTotalPay();
            Double totalExpressMoney = storeOrderItem.getTotalExpressMoney();
        //最大退款数量
            Integer buyCount = storeOrderItem.getBuyCount();
            String skuSnapshot = storeOrderItem.getSkuSnapshot();
            map.put("skuId",storeOrderItem.getSkuId());//商品skuId
            map.put("maxMoney",totalPay);//实付价格

            map.put("buyCount",buyCount);//商品数量

        //商品图片和商品名称
        long productId = storeOrderItem.getProductId();
        ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
        map.put("firstDetailImage", product.getFirstDetailImage());//商品图片
        map.put("firstDetailImageArr", product.getFirstDetailImage().split(","));
        map.put("name", product.getName());//商品名称
        map.put("clothesNumber", product.getClothesNumber());//商品款号

        if(StringUtils.isEmpty(skuSnapshot)){
            map.put("color","");
            map.put("size","");
        }else{
            String[] split = skuSnapshot.split("  ");
            String[] color = split[0].split(":");
            String[] size = split[1].split(":");
            map.put("color",color[1]);//商品颜色
            map.put("size",size[1]);//商品尺码
        }
        //最大退款数量
            //refundMap.put("MaxRefundCount",)
            map.putAll(refuseMap);//拒绝原因列表
        return Response.success(map);
    }

    /**
     * 申请售后
     * @param orderNo
     * @param SkuId
     * @return
     */
    @Override
    public Response applyRefund(Long orderNo, Long SkuId) {
        Map<String, Object> refundMap = new HashMap<String, Object>();
        StoreOrderItemNew storeOrderItem = storeRefundOrderMapper.selectOrderItem(orderNo, SkuId);
//        StoreRefundOrder storeRefundOrder = storeRefundOrderMapper.selectRefundOrder(orderNo, SkuId);
//        if(storeRefundOrder!=null){
//            Integer refundStatus = storeRefundOrder.getRefundStatus();
//
//            //0显示申请退款丶1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
//            // 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
//
//            if (refundStatus!=7||refundStatus!=8||refundStatus!=9||refundStatus!=6||refundStatus!=5){
//                return Response.errorMsg("该商品已申请过售后单,请勿重新申请");
//            }
//        }
//        StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(orderNo);
//        if (storeOrderNew.getRefundUnderway()==1){
//            return Response.errorMsg("该商品所在订单已有售后中的商品");
//        }
        //最大退款金额
       // Double totalPay = storeOrderItem.getTotalPay();
        //最大退款数量
        Integer buyCount = storeOrderItem.getBuyCount();
        String skuSnapshot = storeOrderItem.getSkuSnapshot();

        //refundMap.put("totalPay",totalPay);
        refundMap.put("buyCount",buyCount);
        Double money = storeOrderItem.getMoney();//商品单价
        refundMap.put("money",money);//商品的单价
        //商品图片和商品名称
        long productId = storeOrderItem.getProductId();
        ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
        refundMap.put("firstDetailImage", product.getFirstDetailImage());//商品图片
        refundMap.put("firstDetailImageArr", product.getFirstDetailImage().split(","));
        refundMap.put("name", product.getName());//商品名称
        refundMap.put("clothesNumber", product.getClothesNumber());

        if(StringUtils.isEmpty(skuSnapshot)){
            refundMap.put("color","");
            refundMap.put("size","");
        }else{
            String[] split = skuSnapshot.split("  ");
            String[] color = split[0].split(":");
            String[] size = split[1].split(":");
            refundMap.put("color",color[1]);
            refundMap.put("size",size[1]);
        }
        return Response.success(refundMap);
    }

    public Map<String, Object> getRefuseReasonList(int refundReasonType) {
//        Wrapper<ShopStoreAuthReason> wrapper = new EntityWrapper<ShopStoreAuthReason>().eq("type", refundReasonType)
//                .eq("is_delete", 0)
//                .orderBy("weight", false);
//        List<ShopStoreAuthReason> list = shopStoreAuthReasonMapper.selectList(wrapper);
        List<ShopStoreAuthReason> list = storeRefundOrderMapper.selectList(refundReasonType);
        List<Map<String,Object>> infoList = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        for(ShopStoreAuthReason s:list){
            Map<String,Object> infomap = new HashMap<String,Object>();
            infomap.put("id", s.getId());//拒绝原因ID
            infomap.put("noPassReason", s.getNoPassReason());//拒绝原因
            infoList.add(infomap);
        }
        map.put("RefuseReasonList", infoList);//拒绝原因
        return map;
    }
//        Map<String, Object> map = new HashMap<String, Object>();
//        StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
//        long currentTime = System.currentTimeMillis();
//        if(currentTime >= FEB_TENTH && currentTime <= FEB_TENTH){
//            logger.info("目前工厂处于休假状态，平台暂停受理退款退货服务:orderNo:"+orderNo+",storeId:"+storeId);
//            throw new RuntimeException("亲爱的客户：目前工厂处于休假状态，平台暂停受理退款退货服务，于2018年3月3日重新开始受理售后，为您带来的不便敬请谅解。");
//        }
//
//        // 填充商品数据
////		long productId = storeOrderItemNew.getProductId();
////		ProductNew product = productNewMapper.selectById(productId);
//        Integer orderStatus = storeOrderNew.getOrderStatus();
//        // 先进行判断是否订单
//        if (orderStatus != OrderStatus.PAID.getIntValue() && orderStatus != OrderStatus.DELIVER.getIntValue()) {
//            logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
//            throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
//        }
//        RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
//        //该订单是否在售后中
//        if(storeOrderNew.getRefundUnderway() == StoreOrderNew.REFUND_UNDERWAY){
//            if(reOrder == null){
//                logger.error("当前订单已申请平台介入，不能再申请售后。");
//                throw new RuntimeException("当前订单已申请平台介入，不能再申请售后。");
//            }
//            logger.error("当前订单已有进行中的售后单。不能再申请售后。");
//            throw new RuntimeException("当前订单已有进行中的售后单。不能再申请售后。");
//        }
//        //再判断是否已经该订单是否已经在售后或者售后成功
//        if(null != reOrder){
//            int refundStatus = reOrder.getRefundStatus();
//            if(refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()){
//                logger.error("当前订单已有退款成功的售后单，不能再申请售后。");
//                throw new RuntimeException("当前订单已有退款成功的售后单，不能再申请售后。");
//            }
//            if(refundStatus == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()||
//                    refundStatus == RefundStatus.CUSTOMER_DELIVERY.getIntValue()||
//                    refundStatus == RefundStatus.SELLER_ACCEPT.getIntValue()){
//                logger.error("当前订单已有进行中的售后单。不能再申请售后。");
//                throw new RuntimeException("当前订单已有进行中的售后单。不能再申请售后。");
//            }
//        }
//        //判断该订单状态是否能够申请为退货退款
//        if(refundType == RefundOrder.refundType_refund_and_product && orderStatus == OrderStatus.PAID.getIntValue()){
//            logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
//            throw new RuntimeException("该订单无法申请退货退款！");
//        }
//
//        double expressMoney = storeOrderNew.getTotalExpressMoney();
//        //获取订单信息
//        Map<String,Object> orderMap = new HashMap<String,Object>();
//        orderMap.put("orderNo", storeOrderNew.getOrderNo());
//        orderMap.put("totalExpressMoney",BizUtil.savepoint(expressMoney,2) );
//        String orderNoStr = String.format("%09d", orderNo);
//        orderMap.put("orderNoStr", orderNoStr);
//        Map<String, Object> refuseMap = new HashMap<String,Object>();
//        // 获取退款原因列表
//        //
//        if(refundType == RefundOrder.refundType_refund){
//            refuseMap = shopStoreAuthReasonService.getRefuseReasonList(ONLY_REFUND_REASON_TYPE);
//        }
//        if(refundType == RefundOrder.refundType_refund_and_product){
//            refuseMap = shopStoreAuthReasonService.getRefuseReasonList(REFUND_AND_PRODUCT_REASON_TYPE);
//        }
//        // 最大退款额为实付金额（不含邮费）
//        Map<String, Object> refundMap = new HashMap<String, Object>();
//        // 售后类型 1：仅退款 2：退货退款
//        refundMap.put("refundType", refundType);
//        // 最大退款金额
//        double MostrefundFee = storeOrderNew.getTotalPay() ;
//
//        // 老版本的已经付款未发货的情况需要将金额添加起来  已发货的情况不需要相加..
//        if(storeOrderNew.getOrderStatus()==OrderStatus.PAID.getIntValue()) {
//            if(version<BizUtil.VERSION372) {
//                MostrefundFee +=  expressMoney;
//            }
//        }
//
//        refundMap.put("MostrefundFee", BizUtil.savepoint(MostrefundFee,2));
//        map.putAll(orderMap);
//        map.putAll(refuseMap);
//        map.putAll(refundMap);
//        return map;


    @Override
    public Response submitRefundOrder(Integer refundType, String refundReason, Long refundReasonId, Long orderNo,
                                      Double refundCost, String refundRemark, String refundProofImages,
                                      long storeId, Integer version, Long orderItemId,Long skuId,Integer count) {

        long currentTime = System.currentTimeMillis();
        logger.info(String.valueOf(currentTime));
        //根据订单号以及详情号查询订单
        //StoreOrder storeOrderNew = storeOrderDao.getStoreOrderByOrderNoOrderItemId(orderNo,orderItemId);
        StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(orderNo);

        if (null == storeOrderNew) {
            logger.warn("订单不存在");
            return Response.errorMsg("订单不存在");
        }
        // 不能退款别人的订单..
        if (storeOrderNew.getStoreId() != storeId) {
            return Response.errorMsg("订单号码错误");
        }
        //退款金额
        if (refundCost == 0) {
            logger.warn("退款金额不能为0!");
            return Response.errorMsg("退款金额不能为0!");
        }
        //refundType
        if (refundType != StaticVariableEntity.REFUND_TYPE_REFUND &&
                refundType != StaticVariableEntity.EFUND_TYPE_REFUND_AND_PRODUCT) {
            logger.warn("退款方式未知！");
            return Response.errorMsg("退款方式未知！");
        }

        //校验业务逻辑
        long supplierId = 0;
        try {
            supplierId = checkBusiness372(orderNo, refundCost, refundType, storeOrderNew);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("该商品不符合申请售后条件");
        }
        SupplierUser userNew = supplierUserDao.getSupplierUserInfo(supplierId);
        StoreRefundOrder storeRefundOrder = storeRefundOrderMapper.selectRefundOrder(orderNo, skuId);
        if (storeRefundOrder!=null){
            storeRefundOrderMapper.deleteRefundOrderNo(orderNo,skuId);
        }
        // 开始生成售后订单
            StoreRefundOrder refundOrder = new StoreRefundOrder();
            Long store = storeId;
            RefundOrderNewL refundOrderNewL = new RefundOrderNewL();
            refundOrderNewL.setRefundOrderNo(Long.valueOf(NumberUtil.genOrderNo(3)));
            refundOrderNewL.setStoreId(storeId);
            refundOrderNewL.setOrderNo(orderNo);
            refundOrderNewL.setBrandId(userNew.getBrandId());
           String brandname = storeRefundOrderMapper.getBrandByBrandId(userNew.getBrandId());
            refundOrderNewL.setBrandName(brandname);
            refundOrderNewL.setRefundType(refundType);
            refundOrderNewL.setRefundStatus(RefundStatus.CUSTOMER_APPLY_REFUND.getKey());
            // 退款金额
            refundOrderNewL.setRefundCost(new BigDecimal(refundCost));
            refundOrderNewL.setRefundCount(count);
            refundOrderNewL.setRefundReason(refundReason);
            refundOrderNewL.setRefundRemark(refundRemark);
            refundOrderNewL.setRefundProofImages(refundProofImages);
            refundOrderNewL.setRefundWay(storeOrderNew.getPaymentType());
            refundOrderNewL.setApplyTime(System.currentTimeMillis());
            refundOrderNewL.setSupplierId(storeOrderNew.getSupplierId());
            refundOrderNewL.setRefundReasonId(refundReasonId);
            refundOrderNewL.setPlatformInterveneState(StaticVariableEntity.PLATFORM_NOT_INTERVENE);
            refundOrderNewL.setStoreName(storeOrderNew.getExpressName());
            refundOrderNewL.setStorePhone(storeOrderNew.getExpressPhone());
            refundOrderNewL.setSkuId(skuId);
         storeRefundOrderMapper.insertRefundOrder(refundOrderNewL);
         //将订单的状态改为售后中
         storeRefundOrderMapper.updateIsRefundOrder(orderNo);
         storeOrderDao.addRefundSign(orderNo);
         Map<String, Object> map = new HashMap<String, Object>();
        StoreRefundOrderActionLog  storeRefundOrderActionLog=new StoreRefundOrderActionLog();
        storeRefundOrderActionLog.setRefundOrderId(refundOrderNewL.getRefundOrderNo());
        storeRefundOrderActionLog.setActionTime(System.currentTimeMillis());
        storeRefundOrderActionLog.setActionName("已提交申请，待卖家确认");
        storeRefundOrderMapper.insertLog(storeRefundOrderActionLog);
        map.put("refundOrderId",refundOrderNewL.getRefundOrderNo());



        return Response.success(map);
        }







    @Override
    public Response getRefundOrderDetailed(Long refundOrderId) {

        StoreRefundOrder refundOrder = storeRefundOrderDao.findRefundOrderById(refundOrderId);
        if (refundOrder == null) {
            logger.warn("未找到该售后单");
            return Response.errorMsg("未找到该售后单");
        }
        Map<String, Object> refundOrderData = new HashMap<String, Object>();
        long orderNo = refundOrder.getOrderNo();
        Long skuId = refundOrder.getSkuId();
        // 填充售后订单数据
        //订单详情的图片
        Map<String, Object> refundMap = new HashMap<String, Object>();

        StoreOrderItemNew storeOrderItem = storeRefundOrderMapper.selectOrderItem(orderNo, skuId);
        //退款金额
        BigDecimal totalPay = refundOrder.getRefundCost();
        String skuSnapshot = storeOrderItem.getSkuSnapshot();

        //商品图片和商品名称
        long productId = storeOrderItem.getProductId();
        ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
        refundOrderData.put("firstDetailImage", product.getFirstDetailImage());//商品图片
        refundOrderData.put("firstDetailImageArr", product.getFirstDetailImage().split(","));//商品主图
        refundOrderData.put("name", product.getName());//商品名称
        refundOrderData.put("clothesNumber", product.getClothesNumber());//商品款号

        if(StringUtils.isEmpty(skuSnapshot)){
            refundOrderData.put("color","");
            refundOrderData.put("size","");
        }else{
            String[] split = skuSnapshot.split("  ");
            String[] color = split[0].split(":");
            String[] size = split[1].split(":");
            refundOrderData.put("color",color[1]);//颜色
            refundOrderData.put("size",size[1]);//尺码
        }
        int refundStatus = refundOrder.getRefundStatus();
        refundOrderData.put("refundOrderId", refundOrder.getId());// 售后单ID
        refundOrderData.put("refundOrderNo", refundOrder.getRefundOrderNo());//售后单编号
        refundOrderData.put("refundStatus", refundStatus);// 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
        refundOrderData.put("platformInterveneState", refundOrder.getPlatformInterveneState());// platformInterveneState 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中
        String refundStatusName = ExchangeType.buildInfoRefundStatusName(refundStatus);
        if (refundStatusName==null){
            logger.warn("未知售后订单状态");
            return Response.errorMsg("未知售后订单状态");
        }
        refundOrderData.put("refundStatusName", refundStatusName);// 售后订单状态名称
        refundOrderData.put("refundType", refundOrder.getRefundType());// 退款类型：1.仅退款
        // 2.退货退款
        refundOrderData.put("returnCount", refundOrder.getReturnCount());// 退款数量
        refundOrderData.put("refundCost", refundOrder.getRefundCost());// 退款金额

        refundOrderData.put("refundReason", refundOrder.getRefundReason());// 退款原因
        refundOrderData.put("refundRemark", refundOrder.getRefundRemark());// 退款说明
        refundOrderData.put("refundProofImages", refundOrder.getRefundProofImages());// 退款凭证

        refundOrderData.put("takeProductStateName", ExchangeType.buildTakeProductStateName(refundOrder));// 货物状态名称：已收到货、未收到货
        // 填充订单数据

        refundOrderData.put("orderNo",orderNo );// 订单Id
        refundOrderData.put("brandName", refundOrder.getBrandName());// 订单品牌名称
        StoreOrder storeOrderNew = storeOrderDao.getStoreOrderByOrderNo(orderNo);
        refundOrderData.put("orderStatus",storeOrderNew.getOrderstatus());//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
        double  totalExpressMoney = storeOrderNew.getTotalexpressmoney().doubleValue();

        BigDecimal practicalTotalPay = totalPay.add(BigDecimal.valueOf(totalExpressMoney));

        refundOrderData.put("totalBuyCount",storeOrderNew.getTotalbuycount());//订单实付金额（含运费）
        refundOrderData.put("practicalTotalPay",practicalTotalPay);//订单实付金额（含运费）
        refundOrderData.put("totalExpressMoney",totalExpressMoney);//运费金额


        // 填充订单明细
        refundOrderData.put("storeRefuseReason", refundOrder.getStoreRefuseReason());// 卖家拒绝退款理由
        refundOrderData.put("storeAgreeRemark", refundOrder.getStoreAgreeRemark());// 卖家同意退款备注   对应APP端  卖家备注：这里显示卖家同意时的备注
        refundOrderData.put("handlingSuggestion", refundOrder.getHandlingSuggestion());//平台介入处理意见
		refundOrderData.put("refundOrderCloseReason", buildRefundOrderCloseReason(refundOrder));
        // 售后订单关闭原因：买家主动关闭售后单、买家超时未发货、平台关闭填写的关闭理由

        refundOrderData.put("overtimeNoDeliverCloseReason", "在卖家同意后，买家3天内没有发货，导致售后单超时失效。");//超时失效未发货关闭原因
        refundOrderData.put("platformCloseReason", refundOrder.getPlatformCloseReason());//平台关闭关闭原因：这里显示由平台填写的关闭理由

        refundOrderData.put("surplusAffirmTime", ExchangeType.buildSurplusAffirmTime(refundOrder));//剩余卖家确认时间毫秒数
        refundOrderData.put("surplusDeliverTime", ExchangeType.buildSurplusDeliverTime(refundOrder));//剩余买家发货时间毫秒数
        refundOrderData.put("surplusSupplierAutoTakeTime", ExchangeType.buildSurplusSupplierAutoTakeTime(refundOrder));//剩余卖家自动确认收货时间毫秒数


        refundOrderData.put("customerExpressNo", refundOrder.getCustomerExpressNo());// 买家发货快递单号
        refundOrderData.put("customerExpressCompany", refundOrder.getCustomerExpressCompany());// 买家发货快递公司
        refundOrderData.put("customerExpressCompanyName", refundOrder.getCustomerExpressCompanyName());// 买家发货快递公司名称
        //售后收货地址
        refundOrderData.put("receiveAddress", refundOrder.getSupplierReceiveAddress());// 收货地址
        refundOrderData.put("receiver", refundOrder.getReceiver());// 收货人
        refundOrderData.put("receiverPhone", refundOrder.getReceiverPhone());// 收货人联系电话
        // 填充售后订单操作日志
        List<StoreRefundOrderActionLog> refundOrderActionLogList = storeRefundOrderActionLogDao.getRefundOrderActionLogList(refundOrderId);
        List<Map<String, String>> actionLogList = new ArrayList<Map<String, String>>();
        for (StoreRefundOrderActionLog refundOrderActionLog : refundOrderActionLogList) {
            Map<String, String> actionLogMap = new HashMap<String, String>();
            actionLogMap.put("actionTime", DateUtil.parseLongTime2Str(refundOrderActionLog.getActionTime()));
            actionLogMap.put("actionName", refundOrderActionLog.getActionName());
            actionLogList.add(actionLogMap);
        }
        refundOrderData.put("actionLogList", actionLogList);
        return Response.success(refundOrderData);
    }

    public String buildRefundOrderCloseReason(StoreRefundOrder refundOrder) {
        int refundStatus = refundOrder.getRefundStatus();
        String refundOrderCloseReason = "";
        if (refundStatus == 6) {// 6（买家超时未发货自动关闭）、
            refundOrderCloseReason = "买家超时未发货";
        } else if (refundStatus == 7) {// 7(买家主动关闭)、
            refundOrderCloseReason = "买家主动关闭售后单";
        } else if (refundStatus == 8) {// 8（平台客服主动关闭）
            refundOrderCloseReason = refundOrder.getPlatformCloseReason();
        } else if (refundStatus == 9) {// 7(买家主动关闭)、
            refundOrderCloseReason = "买家主动关闭售后单";
        }

        return refundOrderCloseReason;
    }


    /**
     *查询订单详情
     */
    @Override
    public Map<String, Object> getOrderInfoMation(Long storeId, Long orderNo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long time1 = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        StoreOrderNew storeOrder = storeRefundOrderMapper.selectByOrderNo(orderNo);
        //map.put("disableConfirmationReceipt",storeOrder.getRefundUnderway());
        Double totalMoney = storeOrder.getTotalMoney();//订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）',
        Double totalExpressMoney = storeOrder.getTotalExpressMoney();//邮费
        List<String> cancelReasonList = new ArrayList<String>();
        cancelReasonList.add("我不想买了");
        cancelReasonList.add("信息填写错误");
        cancelReasonList.add("其它");
        map.put("cancelReasonList", cancelReasonList);
        map.put("restrictionActivityProductId",storeOrder.getRestrictionActivityProductId());
        map.put("platformTotalPreferential",storeOrder.getPlatformTotalPreferential());//平台优惠
        map.put("supplierPreferential",storeOrder.getSupplierPreferential());//商家店铺优惠
        map.put("totalExpressMoney",totalExpressMoney);
        map.put("orderTotalMoney",totalMoney+totalExpressMoney);//订单总价
        List<StoreOrderItemNew> orderItems = storeRefundOrderMapper.getOrderNewItemsByOrderNO(storeId,orderNo);
        ExpressInfo expressInfo=storeRefundOrderMapper.getUserExpressInfoByOrderNo(orderNo);
        if (expressInfo!=null){
            ExpressSupplier expressSuppliers = storeRefundOrderMapper.selectListExpressInfo(expressInfo.getExpressSupplier());
            String cnName = expressSuppliers.getCnName();
            map.put("cnName",cnName);//物流的中文名称
        }
        map.put("refundUnderway",storeOrder.getRefundUnderway());//是否在售后中 '是否是售后进行中：0(否)、1(是)',
        map.put("expresName",storeOrder.getExpressName());//收件人姓名
        map.put("expresPhone",storeOrder.getExpressPhone());//收件人电话
        map.put("expresAdress",storeOrder.getExpressAddress());//收件人地址
        map.put("OrderNo",storeOrder.getOrderNo());//订单编号
        map.put("OrderStatus",storeOrder.getOrderStatus());
        // '订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、
        // 30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)',
             //待发货
        long timeMillis = System.currentTimeMillis();
        if(storeOrder.getStatus()==0){
            map.put("mation","该商品还未付款");
        }
        if (storeOrder.getOrderStatus()==10){
          map.put("mation","订单正在处理中,请耐心等待");
            //            //已发货
        }if (storeOrder.getOrderStatus()==50){

            //获取未售后时自动确认收货倒计时毫秒值
            long payTime = storeOrder.getPayTime();
            if(payTime > 0 && storeOrder.getOrderStatus()==50){
                long autoConfirmTime = payTime + 14*24*60*60*1000 ;
                autoConfirmTime=autoConfirmTime-timeMillis;
                if (autoConfirmTime<0){
                    autoConfirmTime=6*24*60*60*1000;
                    map.put("autoConfirmTime",autoConfirmTime);//自动确认时间  sdf.format(autoConfirmTime)
                }else {
                    map.put("autoConfirmTime",autoConfirmTime);//自动确认时间  sdf.format(autoConfirmTime)
                }

            }
//            Long sendTime = storeOrder.getSendTime();
//            Long autoTime=sendTime+60*1000*60*24*15;
//            map.put("mation",autoTime);//显示买家自动确认收货时间
            //交易成功
        }if (storeOrder.getOrderStatus()==70){
            int status = expressInfo.getStatus();
            map.put("expressStatus",status);//显示物流状态
            map.put("expressOrderNo",expressInfo.getExpressOrderNo());//显示物流编号
            map.put("expressTime",expressInfo.getExpressUpdateTime());//物流信息时间
            long dealTime = storeOrder.getConfirmSignedTime();
            map.put("dealTime", sdf.format(new Date(dealTime)));//成交时间
        }
        List<Map<String,Object>> list=new ArrayList<>();

        for (StoreOrderItemNew storeOrderItem : orderItems) {
            Map<String,Object> data=new HashMap<>();
            Long skuId = storeOrderItem.getSkuId();//skuid
            StoreRefundOrder storeRefundOrder = storeRefundOrderMapper.selectRefundOrder(storeOrderItem.getOrderNo(), skuId);
            if (storeRefundOrder!=null){
                data.put("refundOrderId",storeRefundOrder.getRefundOrderNo());
                data.put("refundStatus",storeRefundOrder.getRefundStatus());
            }else {
                data.put("refundStatus",0);
            }
//            if (storeOrder.getOrderStatus()!=0){
//                    data.put("refundOrderId",storeRefundOrder.getRefundOrderNo());
//                data.put("refundStatus",storeRefundOrder.getRefundStatus());
//                    //订单售后状态
//                    //1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
//                    // 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)',
//                }else {
//                    if (storeOrder.getSendTime()!=0){
//                    if ((storeOrder.getSendTime()+1000*60*60*24*7)>=timeMillis){
//                        data.put("refundStatus",0);//0显示申请退款
//                    }
//
//                }else {
//                        data.put("refundStatus",0);//0显示申请退款
//                    }
//                }

            //对应的商品金额
            Double totalPay = storeOrderItem.getTotalPay();
            //对应的商品数量
            Integer buyCount = storeOrderItem.getBuyCount();
            String skuSnapshot = storeOrderItem.getSkuSnapshot();
            Double money = storeOrderItem.getMoney();
            data.put("skuId",storeOrderItem.getSkuId());//商品skuId
            data.put("totalPay",totalPay);
            data.put("buyCount",buyCount);
            data.put("money",money);//商品单价
            //商品图片和商品名称

            long productId = storeOrderItem.getProductId();
            data.put("productId",productId);
            ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
            data.put("firstDetailImage", product.getFirstDetailImage());//商品图片
            data.put("firstDetailImageArr", product.getFirstDetailImage().split(","));
            data.put("name", product.getName());//商品名称
            data.put("clothesNumber", product.getClothesNumber());
            if(StringUtils.isEmpty(skuSnapshot)){
                data.put("color","");
                data.put("size","");
            }else{
                String[] split = skuSnapshot.split("  ");
                String[] color = split[0].split(":");
                String[] size = split[1].split(":");
                data.put("color",color[1]);//商品颜色
                data.put("size",size[1]);//商品尺码
            }

            list.add(data);
        }
        map.put("itemList",list);
        map.put("createTime",storeOrder.getCreateTime());//订单创建时间
        map.put("orderId",orderNo);//订单编号
        map.put("dealOrderNo",orderNo+""+storeOrder.getCreateTime());//交易编号
        return map;
    }

    /**
     * 查询订单详情
     * @param storeId
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, Object> getOrderInfo(Long storeId, Long orderNo) {

        long time1 = System.currentTimeMillis();
        Map<String, Object> data = new HashMap<String, Object>();
        StoreOrderNew storeOrder = storeRefundOrderMapper.selectByOrderNo(orderNo);
        List<StoreOrderItemNew> orderItems = storeRefundOrderMapper.getOrderNewItemsByOrderNO(storeId,orderNo);
        ShopStoreOrderInfoVo orderVo = this.buildOrderInfoVo(storeOrder,orderItems);
        data.put("order", orderVo);
        long time2 = System.currentTimeMillis();
        logger.info("time2:"+(time2-time1));//time2:1003
        double deductibleAmount = 0;  //玖币抵扣金额
        double discountAmount = 0;		//优惠金额
        double orderTotalMoney = storeOrder.getTotalMoney();		//总金额
        data.put("orderTotalMoney", orderTotalMoney);
        for (StoreOrderItemNew item : orderItems) {
            deductibleAmount += item.getTotalMarketPrice() - item.getTotalMoney();
        }
        discountAmount = storeOrder.getTotalMarketPrice() - deductibleAmount - storeOrder.getTotalPay();
        discountAmount = BizUtil.savepoint(discountAmount,2);
        if(discountAmount < 0){
            discountAmount = 0;
        }
        long time3 = System.currentTimeMillis();
        logger.info("time3:"+(time3-time2));//time3:2
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ORDER_AUTO_CONFIRM_TIME);
//        JSONArray jsonArrayExp = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
        int minuteTime = 60 * 1000;
        int confirmMinutes = 0;
        int payExpMinutes = 0;
//        for(Object obj : jsonArrayConfirm) {
//            confirmMinutes = (int) ((JSONObject)obj).get("autoConfirmMinutes");
//            break;
//        }
//        for(Object obj : jsonArrayExp) {
//            payExpMinutes = (int) ((JSONObject)obj).get("overdueMinutes");
//            break;
//        }
//        if(confirmMinutes <= 0){
//            confirmMinutes = 20160;
//        }
//        if(payExpMinutes <=0){
//            payExpMinutes = 1440;
//        }

        long createTime = storeOrder.getCreateTime();
        long dealTime = createTime;
        if(storeOrder.getOrderStatus()>0 && storeOrder.getOrderStatus() == 100){
            dealTime = storeOrder.getUpdateTime();
        }
        data.put("dealTime", sdf.format(new Date(dealTime)));
        if(storeOrder.getOrderStatus() > 0){
            long payTime = 0;
            long shipTime = 0;
            OrderNewLog orderNewLog = selectOrderLogByOrderNoAndStatus(storeOrder.getOrderNo(), 10);
            long time4 = System.currentTimeMillis();
            logger.info("time4:"+(time4-time3));//time4:126
            if(orderNewLog!=null){
                payTime = orderNewLog.getCreateTime();
            }
            orderNewLog=selectOrderLogByOrderNoAndStatus(storeOrder.getOrderNo(), 50);
            long time5 = System.currentTimeMillis();
            logger.info("time5:"+(time5-time4));//time5:13
            if(orderNewLog!=null){
                shipTime = orderNewLog.getCreateTime();
                data.put("shipTime", sdf.format(new Date(shipTime)));
            }
            if(payTime > 0){
                long autoConfirmTime = payTime + minuteTime * confirmMinutes ;
                data.put("autoConfirmTime", autoConfirmTime);
                data.put("payTime", sdf.format(new Date(payTime)));
            }

            if(storeOrder.getRefundStartTime()>0){
                long buildSurplusSupplierAutoTakeTime = buildSurplusSupplierAutoTakeTime(storeOrder.getSendTime(),
                        storeOrder.getRefundStartTime(), storeOrder.getAutoTakeGeliveryPauseTimeLength());
                data.put("autoConfirmTimeString", DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
            }
            long time6 = System.currentTimeMillis();
            logger.info("time6:"+(time6-time5));//time6:1
        }
        data.put("deductibleAmount", deductibleAmount);
        data.put("discountAmount", discountAmount);
        long time7 = System.currentTimeMillis();
        //取最新的一条物流信息
        if(storeOrder.getOrderStatus() >= 50 && storeOrder.getOrderStatus() != 100){
            Map<String, String> result = this.getNewestTrackInfo(storeId, storeOrder.getOrderNo());
            data.put("trackContext", result.get("context"));
            data.put("trackTime", result.get("time"));
        }
        long time8 = System.currentTimeMillis();
        logger.info("time8:"+(time8-time7));//time8:1224
        String shipMsg = "订单正在处理中，请耐心等待";
        String closedMsg = "我不想买了";
        if(storeOrder.getCancelReason() != null && storeOrder.getCancelReason().length() > 0){
            closedMsg += "（"+storeOrder.getCancelReason()+"）";
        }
        String cancelingMsg = "请耐心等待系统处理";
        String splitMsg = "本订单已按配送包裹拆分成多个订单";

        data.put("shipMsg", shipMsg);
        data.put("closedMsg", closedMsg);
        data.put("cancelingMsg", cancelingMsg);
        data.put("splitMsg", splitMsg);

        List<String> cancelReasonList = new ArrayList<String>();
        cancelReasonList.add("我不想买了");
        cancelReasonList.add("信息填写错误");
        cancelReasonList.add("其它");
        data.put("cancelReasonList", cancelReasonList);
        String refundedMsg = "您的退款将于1到3个工作日退返还到支付账户。";
        data.put("refundedMsg", refundedMsg);
        data.put("refundTips", "(退款金额不含收益部分，收益部分请在财务中提现)");
        ExpressInfo expressInfo = storeRefundOrderMapper.getUserExpressInfoByOrderNo(storeOrder.getOrderNo());
        data.put("ExpressInfo", expressInfo);
        long time9 = System.currentTimeMillis();
        logger.info("time9:"+(time9-time8));//time9:12
        int totalBuyCount = storeOrder.getTotalBuyCount();//总购买件数
        double totalProductPrice = storeOrder.getTotalMoney();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
//
//        UserNew supplierUser = userNewService.getSupplierUserInfo(storeOrder.getSupplierId());
//        int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量
//        double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
//        if(wholesaleCount==0 && wholesaleCost==0){//没有设置
//            orderVo.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
//        }else if((totalBuyCount < wholesaleCount) || (totalProductPrice < wholesaleCost)){
//            orderVo.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
//        }else{
//            orderVo.setMatchWholesaleLimit(1);;//是否符合混批限制：0不符合、1符合
//        }
//        long time10 = System.currentTimeMillis();
//        logger.info("time10:"+(time10-time9));//time10:22
//
//        //是否启用确认收货按钮
//        boolean disableConfirmationReceipt = false;
//        Map<String,String> refundOrderMap = orderService.getRefundOrderStatus(orderNo,storeOrder.getOrderStatus());
//        String refundOrderStatus = refundOrderMap.get("refundOrderStatus");
//        if("售后中".equals(refundOrderStatus)){
//            disableConfirmationReceipt = true;
//        }
//        orderVo.setOrderItemStatus(refundOrderStatus);
//        orderVo.setRefundOrderId(refundOrderMap.get("refundOrderId"));
//        if("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus)){
//            //有售后按钮
//            orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.applyAfterSaleButton);
//        }else{
//            //无售后按钮
//            orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.unApplyAfterSaleButton);
//        }
//        //设置是否启用确认收货按钮
//        orderVo.setDisableConfirmationReceipt(disableConfirmationReceipt);

        return data;
    }



    public Map<String, String> getNewestTrackInfo(long userId, long orderNo) {
        //取最新的一条物流信息
        ExpressInfo info = storeRefundOrderMapper.getUserExpressInfoByOrderNo(orderNo);
        String context = "";
        String time = "";
        Map<String, String> result = new HashMap<String, String>();
        if (null == info) {
            context = "暂无物流信息";
            result.put("context", context);
            result.put("time", time);
            return result;
        }
//        JSONObject object;
//        String supplier = info.getExpressSupplier();
//        String expressOrderNo = info.getExpressOrderNo();
//        if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
//            JSONObject expressData = (JSONObject) queryExpressInfo(supplier, expressOrderNo);
//            //System.out.println(((JSONObject)expressData.get("result")).get("data").toString());
//            if (expressData != null && expressData.get("result") != null && ((JSONObject)expressData.get("result")).get("data") != null){
//                List<JSONObject> trackList= (List<JSONObject>) ((JSONObject)expressData.get("result")).get("data");
//                if(trackList.size()>0){
//
//                    object = trackList.get(trackList.size()-1);
//                    if(object != null && object.get("context")!=null){
//                        context = (String) object.get("context");
//                    }
//                    if(object != null && object.get("time")!=null){
//                        time = (String) object.get("time");
//                    }
//                }
//            }
//        }
//        result.put("context", context);
//        result.put("time", time);
//
//        return result;
    return null;
    }


    public JSON queryExpressInfo(final String expressSupplier, final String orderNoWithSupplier) {
//
        String groupKey = "express";
//        String jsonExpress =   "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\""+expressSupplier+"\",\"com\":\""
//                +expressSupplier+"\",\"no\":\""
//                +orderNoWithSupplier+"\"}}";
//        JSONObject json1 = JSON.parseObject(jsonExpress);
//        String key = expressSupplier+":"+orderNoWithSupplier;
//        Object obj = memcachedService.get(groupKey, key);
//        if (obj != null) {
//            return (JSON) obj;
//        }
//        else {
//            boolean noResult = false;
//            ExpressSupplier expressSupplierTemp = expressSupplierService.getExpressSupplierByEngName(expressSupplier);
//            if(expressSupplierTemp != null && expressSupplierTemp.getCnName() != null  && expressSupplierTemp.getCnName().length() > 0){
//                jsonExpress =   "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\""+expressSupplierTemp.getCnName()+"\",\"com\":\""
//                        +expressSupplier+"\",\"no\":\""
//                        +orderNoWithSupplier+"\"}}";
//                json1 = JSON.parseObject(jsonExpress);
//            }
//            for (IExpressQuery query : expressQueries) {
//                if (query.support(expressSupplier)) {
//                    String text = query.queryExpressInfo(expressSupplier, orderNoWithSupplier);
//                    JSONObject json = JSON.parseObject(text.toString());
//                    if (json == null || json.get("result") == null || ((JSONObject)json.get("result")).get("company") == null){
//                        noResult = true;
//                        json = json1;
//                    }
//                    memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_HOUR, json);
//                    return json;
//                }
//            }
//        }    return json1;
        return null;
    }

    public Object get(String groupKey, String key) {
//        String realKey = makeRealKey(groupKey, key);
//        try {
//            return this.memcachedClient.get(realKey);
//        } catch (Exception e) {
//            log.error("Memcached Get object failed with key: {}", realKey, e.getMessage());
//            return null;
//        }
    return null;
    }
    private String makeRealKey(String groupKey, String key) {
//        groupKey = StringUtils.defaultString(groupKey);
//        return EncodeUtil.encodeURL(realKeyPrefix + groupKey + key, "UTF-8");
    return null;
    }
    public long buildSurplusSupplierAutoTakeTime(long sendTime,long refundStartTime,long autoTakeDeliveryPauseTimeLength) {
        if(sendTime == 0 ){//未发货则返回0
            return 0;
        }

        //15天
        long maxTime = 14 * 24 * 60 * 60 * 1000;

        long time = 0;//
        if(refundStartTime == 0){//未暂停
            time = System.currentTimeMillis();//当前时间
        }else{//卖家申请平台介入售后订单暂停
            time = refundStartTime;//暂停时间
        }

        long supplierAutoTakeTime = sendTime + maxTime + autoTakeDeliveryPauseTimeLength;//自动确认收货时间节点
        long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
        surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
        if(surplusSupplierAutoTakeTime < 0){
            surplusSupplierAutoTakeTime = 0;
        }
        return surplusSupplierAutoTakeTime;
    }
    public  OrderNewLog selectOrderLogByOrderNoAndStatus( long orderNo, Integer orderStatus){
        return storeRefundOrderMapper.selectOrderLogByOrderNoAndStatus(orderNo, orderStatus);
    }

    /**
     * 买家撤销售后
     * @param refundOrderNo
     * @return
     */
    @Override
    public void cancelRefundOrder(Long refundOrderNo) {
        StoreRefundOrder storeRefundOrder = storeRefundOrderMapper.selectRefundByRefundOrder(refundOrderNo);

        if(null == storeRefundOrder ){
            logger.info("无售后订单无法撤销！");
            throw new RuntimeException("无售后订单无法撤销！");
        }
        Integer refundStatus = storeRefundOrder.getRefundStatus();

        if(refundStatus != 1&&refundStatus !=2){
            logger.info("该售后订单无法撤销！");
            throw new RuntimeException("该售后订单无法撤销！");
        }

        //添加相关操作日志以及更改售后单状态
        if(storeRefundOrder.getRefundStatus() == 1){
            updateOrderStatusWhenClose(refundOrderNo,7,storeRefundOrder.getOrderNo(),7,null);
        }
        if(storeRefundOrder.getRefundStatus() == 2){
            updateOrderStatusWhenClose(refundOrderNo,9,storeRefundOrder.getOrderNo(),8,null);
        }

    }

    /**
     * 获取所有订单列表
     * @param request
     * @return
     */
    @Override
    public Response selectOrderList(ShopOrderRequest request) {
        PageHelper.startPage(request.getPageNumber(),request.getPageSize());
        List<StoreOrderNewResponse> list = storeOrderMapper.selectOrderList(request);
        for (StoreOrderNewResponse storeOrderNewRespons : list) {
            storeOrderNewRespons.setOrderTotalPay(storeOrderNewRespons.getTotalPay());
            storeOrderNewRespons.setOrderTotalMoney(storeOrderNewRespons.getTotalMoney());
            storeOrderNewRespons.setOrderTotalPay(storeOrderNewRespons.getTotalPay());
            Long orderNo = storeOrderNewRespons.getOrderNo();//母订单号
            Long storeId = storeOrderNewRespons.getStoreId();//用户id
            List<StoreOrderItemNew> orderItemNewList = storeRefundOrderMapper.getOrderNewItemsByOrderNO(storeId, orderNo);
            for (StoreOrderItemNew storeOrderItemNew : orderItemNewList) {
                ProductNew product = storeRefundOrderMapper.selectProdectById(storeOrderItemNew.getProductId());
                storeOrderNewRespons.setImg(product.getFirstDetailImage());//商品图片
                storeOrderNewRespons.setShopName(product.getName());//商品名称
                String skuSnapshot = storeOrderItemNew.getSkuSnapshot();
                if(StringUtils.isEmpty(skuSnapshot)){
                    storeOrderNewRespons.setColor("");
                    storeOrderNewRespons.setSize("");
                }else{
                    String[] split = skuSnapshot.split("  ");
                    String[] color = split[0].split(":");
                    String[] size = split[1].split(":");
                    storeOrderNewRespons.setColor(color[1]);//商品颜色
                    storeOrderNewRespons.setSize(size[1]);//商品尺码
                }
            }
            storeOrderNewRespons.setList(orderItemNewList);
        }
        PageInfo<StoreOrderNewResponse> poolResponsePageInfo = new PageInfo<>(list);
        return Response.success(poolResponsePageInfo);
    }

    /**
     * 获取订单详情
     * @param refundOrderId
     * @return
     */
    @Override
    public Response selectDetail(Long refundOrderId) {
        StoreRefundOrder storeRefundOrder = storeRefundOrderMapper.selectDetail(refundOrderId);
        Map<String,Object> map=new HashMap<>();
        map.put("id",storeRefundOrder.getId());
        Integer refundStatus = storeRefundOrder.getRefundStatus();//退款状态
        map.put("refundStatus",refundStatus);//售后单状态
        //'售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
        // 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)',
        //platform_intervene_state;
//        Integer platformInterveneState = storeRefundOrder.getPlatformInterveneState();
//        //'平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束',

        //StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(storeRefundOrder.getOrderNo());
        StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(storeRefundOrder.getOrderNo());
        map.put("orderStatus",storeOrderNew.getOrderStatus());
        if (refundStatus==1){//等待卖家确认
           if (storeRefundOrder.getPlatformInterveneState()==0){
               map.put("autoTime", ExchangeType.buildSurplusAffirmTime(storeRefundOrder));//剩余卖家确认时间毫秒数
           }
           }
        if (refundStatus==2||refundStatus==3041){//卖家同意
            map.put("autoTime",ExchangeType.buildSurplusDeliverTime(storeRefundOrder));//剩余卖家发货时间
            map.put("storeAgreeRemark",storeRefundOrder.getStoreAgreeRemark());//买家同意备注
        }
        if (refundStatus==7&&refundStatus==9){//已撤销
        }
        if (refundStatus==5||refundStatus==3011||refundStatus==2011){//卖家拒绝
            map.put("storeRefuseReason",storeRefundOrder.getStoreRefuseReason());//卖家拒绝原因;
        }
        if (refundStatus==6){//买家超时失效
            map.put("closeReson","在卖家同意后,买家3天内没有发货,到是售后订单超时失效");
        }
        if (refundStatus==3){//买家已发货,待卖家收货
            map.put("autoTime",ExchangeType.buildSurplusSupplierAutoTakeTime(storeRefundOrder));//剩余卖家自动确认收货
        }
        if (refundStatus==4||refundStatus==3051||refundStatus==2021){//退款成功
            map.put("realMoney",storeRefundOrder.getRealBackMoney());//实际退款金额
        }
        Integer refundType = storeRefundOrder.getRefundType();//退款类型
       // '退款类型：1.仅退款  2.退货退款',
        map.put("refundType",refundType);//'退款类型：1.仅退款  2.退货退款',
        map.put("refundMoney",storeRefundOrder.getRefundCost());//退款申请金额
        map.put("refundCount",storeRefundOrder.getReturnCount());//退款数量
        Long skuId = storeRefundOrder.getSkuId();
        if (skuId!=0){
            StoreOrderItemNew storeOrderItemNew = storeRefundOrderMapper.selectOrderItem(storeRefundOrder.getOrderNo(), skuId);
            long productId = storeOrderItemNew.getProductId();
            String skuSnapshot = storeOrderItemNew.getSkuSnapshot();
            ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
            map.put("firstDetailImage", product.getFirstDetailImage());//商品图片
            map.put("firstDetailImageArr", product.getFirstDetailImage().split(","));
            map.put("name", product.getName());//商品名称
            map.put("clothesNumber", product.getClothesNumber());//商品款号
            map.put("totalCount",storeOrderItemNew.getBuyCount());//购买的总数量
            map.put("totalMoney", storeOrderItemNew.getTotalPay()+storeOrderItemNew.getTotalExpressMoney());//实付的总价格

            map.put("expressMoney",storeOrderItemNew.getTotalExpressMoney());//总的邮费
            if(StringUtils.isEmpty(skuSnapshot)){
                map.put("color","");
                map.put("size","");
            }else{
                String[] split = skuSnapshot.split("  ");
                String[] color = split[0].split(":");
                String[] size = split[1].split(":");
                map.put("color",color[1]);//商品颜色
                map.put("size",size[1]);//商品尺码
            }
        }
        map.put("brandName",storeRefundOrder.getBrandName());//品牌名称
        map.put("applyTime",TimeUtils.longFormatString(storeRefundOrder.getApplyTime()));//申请时间
        map.put("refundOrderNo",storeRefundOrder.getRefundOrderNo());//售后编号
       // map.put("isRefund",1);
        map.put("orderNo",storeRefundOrder.getOrderNo());
        map.put("dealNo",storeRefundOrder.getRefundOrderNo()+""+storeRefundOrder.getOrderNo());//交易编号=售后编号+订单编号
        map.put("refundReson",storeRefundOrder.getRefundReason());//退款原因
        map.put("refundMark",storeRefundOrder.getRefundRemark());//退款说明
        map.put("refundPhoto",storeRefundOrder.getRefundProofImages());//退款凭证
        if (storeRefundOrder.getRefundProofImages()!=""&&storeRefundOrder.getRefundProofImages()!=null){
            String refundProofImages = storeRefundOrder.getRefundProofImages();
            String[] split = refundProofImages.split(",");
            map.put("split",split);
        }

        map.put("skuId",storeRefundOrder.getSkuId());
        map.put("receiveAddress",storeRefundOrder.getSupplierReceiveAddress());//收货人地址
        map.put("receiver",storeRefundOrder.getReceiver());//收货人姓名
        map.put("receiverPhone",storeRefundOrder.getReceiverPhone());//收货人电话
        // 填充售后订单操作日志
        List<StoreRefundOrderActionLog> refundOrderActionLogList = storeRefundOrderMapper.getRefundOrderActionLogList(refundOrderId);
        List<Map<String, String>> actionLogList = new ArrayList<Map<String, String>>();
        for (StoreRefundOrderActionLog refundOrderActionLog : refundOrderActionLogList) {
            Map<String, String> actionLogMap = new HashMap<String, String>();
            actionLogMap.put("actionTime", TimeUtils.longFormatString(refundOrderActionLog.getActionTime()).toString());
            actionLogMap.put("actionName", refundOrderActionLog.getActionName());
            actionLogList.add(actionLogMap);
        }
        map.put("actionLogList", actionLogList);
        return Response.success(map);
    }

    @Override
    public Response sendOrder(Long storeId,Long refundOrderNo) {
        Map<String,Object> map = new HashMap<String,Object>();
        StoreRefundOrder reOrder = storeRefundOrderMapper.selectDetail(refundOrderNo);
        map.put("receiver", reOrder.getReceiver());//收货人
        map.put("receiverPhone", reOrder.getReceiverPhone());//收货人手机号
        map.put("supplierReceiveAddress", reOrder.getSupplierReceiveAddress());//供应商收货地址
        //获取邮寄公司名称列表
        List<ExpressSupplier> allExpressCompanyNames = storeRefundOrderMapper.getAllExpressCompanyNames();
        List<Map<String,Object>> allExpressCompanys = new ArrayList<Map<String,Object>>();
        for (ExpressSupplier allExpressCompanyName : allExpressCompanyNames) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("CnName", allExpressCompanyName.getCnName());
            data.put("EngName", allExpressCompanyName.getEngName());
            data.put("Id", allExpressCompanyName.getId());
            allExpressCompanys.add(data);
        }
        map.put("allExpressCompanys", allExpressCompanys);//邮寄公司名称列表
        return Response.success(map);
    }

    /**
     * 售后订单关闭
     * @param refundOrderId
     * @param refundStatus
     * @param orderNo
     * @param refundOrderActionLogEnum
     * @param platformCloseReason
     */
    public void updateOrderStatusWhenClose(Long refundOrderId,Integer refundStatus,Long orderNo,Integer refundOrderActionLogEnum,String platformCloseReason) {
        //获取售后订单状态
        StoreRefundOrder reOrder = storeRefundOrderMapper.selectRefundByRefundOrder(refundOrderId);

        int status = reOrder.getRefundStatus();
        if(status == 7||
                status == 6||
                status == 8||
                status ==9){
            logger.error("该售后订单已经关闭，请勿重复操作！refundOrderId:"+refundOrderId+",refundStatus:"+status);
            throw new RuntimeException("该售后订单已经关闭，请勿重复操作！");
        }
        ;
        //更改售后单状态
        StoreRefundOrder refundOrder = new StoreRefundOrder();
        refundOrder.setRefundOrderNo(refundOrderId);
        refundOrder.setRefundStatus(refundStatus);
        //如果是平台关闭，还需要把卖家自动确认收货总暂停时长加进去
        if(null != platformCloseReason){
            refundOrder.setPlatformCloseReason(platformCloseReason);
        }
        //平台关闭时间
        if(refundStatus== 8){
            refundOrder.setPlatformInterveneCloseTime(System.currentTimeMillis());
        }
        //卖家拒绝时间
        if(refundStatus == 5){
            refundOrder.setStoreRefuseRefundTime(System.currentTimeMillis());
        }
        //买家超时未发货时间
        if(refundStatus ==6){
            refundOrder.setCustomerOvertimeTimeNoDelivery(System.currentTimeMillis());
        }
        //买家撤销时间
        if(refundStatus== 7){
            refundOrder.setCustomerCancelTime(System.currentTimeMillis());
        }
        //买家撤销时间
        if(refundStatus== 9){
            refundOrder.setCustomerCancelTime(System.currentTimeMillis());
        }
        storeRefundOrderMapper.updateByRefundOrder(refundOrder);

        //更改订单状态以及计算自动确认收货总暂停时长
        addAutoTakeGeliveryPauseTimeAndClearSign(orderNo);

        //添加操作日志
        addActionLog(refundOrderActionLogEnum, refundOrderId);
    }

    /**
     * 添加售后订单的操作日志
     * @param refundOrderActionLogEnum
     */
    public void addActionLog(Integer refundOrderActionLogEnum,Long refundOrderId){
        long time = System.currentTimeMillis();
        StoreRefundOrderActionLog log = new StoreRefundOrderActionLog();
        log.setActionName(refundOrderActionLogEnum.toString());
        log.setActionTime(time);
        log.setRefundOrderId(refundOrderId);
        storeRefundOrderMapper.insertLog(log);
        logger.info("添加操作日志完成，"+refundOrderActionLogEnum.toString());
    }
    /**
     * 更改订单状态以及计算自动确认收货总暂停时长
     */
    public void addAutoTakeGeliveryPauseTimeAndClearSign(long orderNo){
        StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(orderNo);
        StoreOrderNew storeOrderNew1 = new StoreOrderNew();
        //添加延长自动确认收货时间
        long currentTime = System.currentTimeMillis();
        long startTime = storeOrderNew.getRefundStartTime();
        long autoTakeGeliveryPauseTimeLength = currentTime-startTime;
        autoTakeGeliveryPauseTimeLength += storeOrderNew.getAutoTakeGeliveryPauseTimeLength();
        storeOrderNew1.setAutoTakeGeliveryPauseTimeLength(autoTakeGeliveryPauseTimeLength);//添加自动确认收货总暂停时长
        storeOrderNew1.setRefundStartTime(0L);//售后开始时间
        //清楚售后标志
        storeOrderNew1.setRefundUnderway(0);//开启订单，清楚售后标志
        storeOrderNew1.setOrderNo(orderNo);
        storeRefundOrderMapper.updateById(storeOrderNew1);
    }
//
//    public JSONArray getJsonArray(GlobalSettingName name) {
//        String value = getSetting(name);
//        return StringUtils.isBlank(value) ? new JSONArray() : JSON.parseArray(value);
//    }
//
//    public double getDouble(GlobalSettingName name) {
//        String value = getSetting(name);
//        return StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
//    }


//    public String getSetting(GlobalSettingName name) {
//        String groupKey = MemcachedKey.GROUP_KEY_GLOBAL_SETTING;
//        String key = name.getStringValue();
//        Object obj = memcachedService.get(groupKey, key);
//        if (obj != null) {
//            return (String) obj;
//        }
//
//        String setting = globalSettingMapper.getSetting(name);
//        if (setting != null) {
//            memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, setting);
//        }
//        return setting;
//    }

    public ShopStoreOrderInfoVo buildOrderInfoVo(StoreOrderNew orderObj,List<StoreOrderItemNew> orderItems) {
//		return orderService.getUserOrderNewDetailByNo(userDetail, orderNo);
        ShopStoreOrderInfoVo orderInfoVo = new ShopStoreOrderInfoVo();
        orderInfoVo.setOrderNo(orderObj.getOrderNo());
        orderInfoVo.setOrderStatus(orderObj.getOrderStatus());
        orderInfoVo.setCreateTime(DateUtil.format(orderObj.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfoVo.setExpiredTime(orderObj.getExpiredTime());
        orderInfoVo.setParentId(orderObj.getParentId());
        orderInfoVo.setRestrictionActivityProductId(orderObj.getRestrictionActivityProductId());//;
        orderInfoVo.setTotalExpressMoney(orderObj.getTotalExpressMoney());
        orderInfoVo.setHangUp(orderObj.getHangUp());
        orderInfoVo.setTotalPay(orderObj.getTotalPay());

        orderInfoVo.setTotalMoney(orderObj.getTotalMoney());
        orderInfoVo.setPaymentNo(orderObj.getPaymentNo());
        orderInfoVo.setExpressInfo(orderObj.getExpressInfo());

        List<Map<String,Object>> itemMapList = new ArrayList<Map<String,Object>>();
        for(StoreOrderItemNew item : orderItems){
            Map<String,Object> itemMap = new HashMap<String,Object>();
            itemMap.put("productId", item.getProductId());
            itemMap.put("marketPrice", item.getMarketPrice());
            itemMap.put("money", item.getMoney());
            itemMap.put("buyCount", item.getBuyCount());
            itemMap.put("skuSnapshot", item.getSkuSnapshot());
            itemMap.put("colorStr", item.getSkuSnapshot().split("  ")[0].trim());
            itemMap.put("sizeStr", item.getSkuSnapshot().split("  ")[1].trim());
            ProductNew productNew = storeRefundOrderMapper.selectProdectById(item.getProductId());
            //String platformProductState = productNewService.getPlatformProductState(productNew);//  平台商品状态:0已上架、1已下架、2已删除
           // itemMap.put("platformProductState", platformProductState);
            itemMap.put("clothesNumber",productNew.getClothesNumber() );//商品款号
            itemMap.put("productName",productNew.getName() );//商品名称
            itemMap.put("productMainImg",productNew.getMainImg() );//商品主图
            itemMap.put("brandName",productNew.getBrandName());//品牌名称
            //新添加的代码
            //获取该商品的orderno
            Long orderNo = item.getOrderNo();
            //获取到该商品的skuid
            Long skuId = item.getSkuId();
            StoreRefundOrder  refundOrder = storeRefundOrderMapper.selectRefundOrder(orderNo, skuId);
            if (refundOrder!=null){
                itemMap.put("refundStatus",refundOrder.getRefundStatus());//售后状态
                //'售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
                // 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)',
            }else {
                itemMap.put("refundStatus",0);//该订单没收售后信息,显示可以申请退款
            }
            itemMapList.add(itemMap);
        }
        orderInfoVo.setOrderItems(itemMapList);
//    	disableConfirmationReceipt
//    	refundMoney
        return orderInfoVo;
    }



    /**
     * 校验业务逻辑
     * @param storeOrderNew
     */
    private long checkBusiness372(Long orderNo, Double refundCost, Integer refundType, StoreOrderNew storeOrderNew) {
        // 获取该门店用户的订单
        // 填充商品数据
//				long productId = storeOrderItemNew.getProductId();
//				ProductNew product = productNewMapper.selectById(productId);
        Integer orderStatus = storeOrderNew.getOrderStatus();
        // 先进行判断是否订单
        if (orderStatus != 10 && orderStatus != 50) {
            logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
            throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
        }
        StoreRefundOrder reOrder = storeRefundOrderDao.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
        //判断该订单状态是否能够申请为退货退款
        if(refundType == StaticVariableEntity.EFUND_TYPE_REFUND_AND_PRODUCT && orderStatus.equals(OrderStatus.PAID.getIntValue()) ){
            logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
            throw new RuntimeException("该订单无法申请退货退款！");
        }
        //检测退款金额是否小于实付金额
        double totalPay = storeOrderNew.getTotalPay();

        // 已经发货了 则不能退运费 最大退款金额 totalPay
        if(orderStatus.equals(OrderStatus.DELIVER.getIntValue()) ){
            if(refundCost > totalPay){
                logger.error("该订单的退款金额大于最大退款金额！无法提交售后！");
                throw new RuntimeException("该订单的退款金额超过最大退款金额！");
            }
        }

        Double express = storeOrderNew.getTotalExpressMoney();
        BigDecimal totalPayBig = new BigDecimal(totalPay);
        BigDecimal expressBig= new BigDecimal(express);
        BigDecimal all = totalPayBig.add(expressBig).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal refundCostBig = new BigDecimal(refundCost).setScale(2,BigDecimal.ROUND_HALF_UP);

        //如果是已支付的状态 则最大退款金额要加上运费
        if(orderStatus.equals(OrderStatus.PAID.getIntValue()) ){
            // 如果不是未发货的状态 则最大退款金额为实际支付金额+运费
            if(refundCostBig.compareTo(all)>0) {
                logger.error("该订单的退款金额大于最大退款金额！无法提交售后！");
                throw new RuntimeException("该订单的退款金额超过最大退款金额！");
            }
        }

        //判断在已付款状态下，仅退款是否是全额退款
//        if(orderStatus.equals(OrderStatus.PAID.getIntValue()) && refundCostBig.compareTo(all) !=0){
//            logger.error("在已付款的状态下，订单仅退款不能退部分金额！orderNo:"+orderNo);
//            throw new RuntimeException("该订单只能全额退款");
//        }
        return storeOrderNew.getSupplierId();

    }
}
