package com.e_commerce.miscroservice.store.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.ServiceAdvice;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.application.conver.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.store.entity.StaticVariableEntity;
import com.e_commerce.miscroservice.store.entity.emuns.OrderStatus;
import com.e_commerce.miscroservice.store.entity.emuns.RefundStatus;
import com.e_commerce.miscroservice.store.entity.vo.*;
import com.e_commerce.miscroservice.store.mapper.StoreRefundOrderMapper;
import com.e_commerce.miscroservice.store.mapper.WXOrderMapper;
import com.e_commerce.miscroservice.store.service.ShopOrderWXService;
import com.e_commerce.miscroservice.store.service.WXOrderService;
import com.e_commerce.miscroservice.store.utils.NumberUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WXOrderServiceImp implements WXOrderService {
//    @Value("${serviceSend}")
//    private String serviceSend;
    //http://weixinonline.yujiejie.com/
   //public static String weixinServiceUrl ="https://local.yujiejie.com/jweixin";
   //public static String weixinServiceUrl ="https://storeonline.yujiejie.com/jweixin";
    //https://weixinonline.yujiejie.com/serviceAdvice/sendTemplateAdvice
    //public String weixinServiceUrl="https://local.yujiejie.com/jweixin/";
    public static String weixinServiceUrl ="https://weixinonline.yujiejie.com";
    public static String sendTemplateAdviceUrl = "/serviceAdvice/sendTemplateAdvice";
    public static String getTemplateAdviceUrl = "/serviceAdvice/getTemplateAdvice";
    public static String addTemplateAdviceUrl = "/serviceAdvice/addTemplateAdvice";
    @Autowired
    private WXOrderMapper wxOrderMapper;

    @Autowired
    public StoreRefundOrderMapper storeRefundOrderMapper;
    private Log logger = Log.getInstance(WXOrderServiceImp.class);
    @Value("${skipShop.storeId}")
    private String shopInId;

    /**
     * 查询所有订单
     * @param request
     * @return
     */
    @Override
    public Response getRefundOrderList(ShopMemberOrderRequest request) {
        PageHelper.startPage(request.getPageNumber(),request.getPageSize());
        List<ShopMemberOrderResponse> refundOrderList = wxOrderMapper.getRefundOrderList(request);
        for (ShopMemberOrderResponse shopMemberOrderResponse : refundOrderList) {
            Integer buyWay = shopMemberOrderResponse.getBuyWay();

            if (buyWay==1){
                //判断是否满足参团人数
                TeamBuyActivity teamAct = wxOrderMapper.selectById(shopMemberOrderResponse.getTeamId());
                int buttonStatus = isShowButton(teamAct);
                shopMemberOrderResponse.setButtonStatus(buttonStatus);
                shopMemberOrderResponse.setProductId(teamAct.getShopProductId());
                shopMemberOrderResponse.setShopProductName(teamAct.getShopProductName());
            }

            Long id = shopMemberOrderResponse.getId();//订单号
            List<ShopMemberOrderItemResponse> shopMemberOrderItemResponses = wxOrderMapper.selectByOrderId(id);
            for (ShopMemberOrderItemResponse response : shopMemberOrderItemResponses) {
                response.setBuyCount(response.getCount());//商品购买数量;
                Long skuId = response.getProductSkuId();//商品的sku
                Long orderId = response.getOrderId();//订单编号
                ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefund(orderId.toString(), skuId);
                if (shopOrderAfterSale!=null){
                    Integer status = shopOrderAfterSale.getStatus();//退款状态：0 默认 退款中，1 退货成功 2 退款失败" 3,不在售后中
                    response.setRefundStatus(status);
                }else {
                    response.setRefundStatus(3);
                }
            }
            shopMemberOrderResponse.setList(shopMemberOrderItemResponses);
            Map<String,Object> map=new HashMap<>();
            //订单剩余时间
            Map<String, Object> pendingPaymentTime = getPendingPaymentTime(shopMemberOrderResponse, map, shopMemberOrderResponse.getStoreId(), shopMemberOrderResponse.getMemberId());
            if (pendingPaymentTime.get("pendingPaymentTime")!=null){
                shopMemberOrderResponse.setPendingPaymentTime(pendingPaymentTime.get("pendingPaymentTime").toString());//订单支付时间
            }
            Object orderStatus = pendingPaymentTime.get("orderStatus");
            if (orderStatus!=null){
                shopMemberOrderResponse.setOrderStatus(Integer.parseInt(orderStatus.toString()));//订单状态
            }
    }
        PageInfo<ShopMemberOrderResponse> poolResponsePageInfo = new PageInfo<>(refundOrderList);
        return Response.success(poolResponsePageInfo);
    }
/**
 * 拼团
 */
public static boolean isTeamSuccess(TeamBuyActivity teamAct) {
    if (teamAct.getConditionType () ==2) {
        if (teamAct.getMeetProductCount () <= teamAct.getOrderedProductCount ()) {
            return true;
        }
    }
    else {
        if (teamAct.getUserCount () <= teamAct.getActivityMemberCount ()) {
            return true;
        }
    }
    return false;
}

/**
 * 拼团
 */
private int isShowButton(TeamBuyActivity teamAct) {
    if (isTeamSuccess (teamAct)) {
        //拼团成功付款
        return 1;
    }

    //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
    if (teamAct.haveActivityStatusInt() == 2) {
        //2进行中,邀请参团
        return 2;
    }
    //不显示
    return 0;
}

    /**
     * 获取订单剩余支付时间
     * @param shopMemberOrder
     * @param shopMemberOrderData
     * @param storeId
     * @param memberId
     * @return
     */
    private Map<String, Object> getPendingPaymentTime(ShopMemberOrderResponse shopMemberOrder,Map<String, Object> shopMemberOrderData,long storeId, long memberId) {
        //订单过期时间
        long overdueTime = 0L;
        long nowTime = System.currentTimeMillis();
        int cancelType = 1;
        String cancelReason = "";
        //普通订单
        if ( shopMemberOrder.getBuyWay() ==0) {
            overdueTime = shopMemberOrder.getCreateTime()+ 24*60*60*1000;
            cancelType = 1;
            cancelReason = "您已取消订单";
        }
        //秒杀订单
        if ( shopMemberOrder.getBuyWay() == 2) {
            overdueTime = shopMemberOrder.getCreateTime()+ 2*60*60*1000;
            SecondBuyActivity secondBuyActivity = wxOrderMapper.selectBySId(shopMemberOrder.getSecondId());
            //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
            int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
            //判断活动是否结束(并且是手动结束)
            if (haveActivityStatusInt == 3 && secondBuyActivity.getActivityHandEndTime() > 0) {
                overdueTime =-1;//立即结束
                cancelType = 2;
                cancelReason = "商家手动结束活动，关闭订单";
            }
            //秒杀订单剩余保留时间
            long remainTime = nowTime-shopMemberOrder.getCreateTime();
            if (haveActivityStatusInt == 2 && remainTime > 2*60*60*1000) {
                overdueTime =-1;//立即结束
                cancelType = 3;
                cancelReason = "超过两小时未付款，系统自动取消";
            }
        }
        //团购订单
        if ( shopMemberOrder.getBuyWay() ==1) {
            TeamBuyActivity teamBuyActivity = wxOrderMapper.selectById(shopMemberOrder.getTeamId());
            //判断团购订单状态
            //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
            int haveActivityStatusInt = teamBuyActivity.haveActivityStatusInt();
            if (haveActivityStatusInt == 3 && teamBuyActivity.getActivityHandEndTime() == 0) {
                overdueTime = -1;//立即结束
                cancelType = 3;
                cancelReason = "活动已结束，订单已被系统自动取消";
            }
            if (haveActivityStatusInt == 3 && teamBuyActivity.getActivityHandEndTime() > 0) {
                overdueTime =-1;//立即结束
                cancelType = 2;
                cancelReason = "商家手动结   束活动，关闭订单";
            }
            if (haveActivityStatusInt == 2){
                overdueTime = teamBuyActivity.getActivityEndTime();
            }
        }

        //剩余时间
        long time = overdueTime-nowTime;
        if(time<=0){

            shopMemberOrderData.put("orderStatus",3);//订单关闭
        }else{
            shopMemberOrderData.put("pendingPaymentTime", time);//剩余订单时间
        }
        return shopMemberOrderData;
    }
    /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
    @Override
    public Response selectOrderItem1(Long orderNo,Long userId) {
        //ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectByOrderNo(orderNo);
        Map<String,Object> map = new HashMap<>();
        List<ShopMemberOrderItem> shopMemberOrderItems = wxOrderMapper.selectByOrderNo(orderNo);
        List<Map<String,Object>> list=new ArrayList<>();
        for (ShopMemberOrderItem shopMemberOrderItem : shopMemberOrderItems) {
            Map<String,Object> data=new HashMap<>();
            Long productSkuId = shopMemberOrderItem.getProductSkuId();
            ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefund(orderNo.toString(), productSkuId);
            if (shopOrderAfterSale!=null){
                data.put("refundStatus",shopOrderAfterSale.getStatus());//'退款状态：0 默认 退款中，1 退货成功 2 退款失败'
            }else {
                data.put("refundStatus",4);// 4代表着该订单不在售后中
            }
            data.put("name",shopMemberOrderItem.getName());//商品名称
            data.put("price",shopMemberOrderItem.getPrice());//商品价格
            data.put("photo",shopMemberOrderItem.getSummaryImages());//商品图片
            data.put("size",shopMemberOrderItem.getSize());//商品尺码
            data.put("count",shopMemberOrderItem.getCount());//商品数量;
            data.put("color",shopMemberOrderItem.getColor());//颜色
            data.put("userId",userId);//会员id
            data.put("skuId",shopMemberOrderItem.getProductSkuId());//skuId
            list.add(data);
        }
        map.put("itemList",list);

        return Response.success(map);
    }

    /**
     * 查询订单详情
     * @param orderId
     * @param skuId
     * @return
     */
    @Override
    public Response selectItem(Long orderId, Long skuId) {
        logger.info("根据订单号="+orderId+"和商品skuid="+skuId+"查询商品");
        ShopMemberOrder shopMemberOrder1 = wxOrderMapper.selectOrder(orderId);
        Long confirmSignedTime = shopMemberOrder1.getConfirmSignedTime();//订单收货的时间
       if (confirmSignedTime!=0){
           long timeMillis = System.currentTimeMillis();//现在的时间
           if (timeMillis>confirmSignedTime+1000*60*60*24*7){
               return Response.errorMsg("该订单确认收货已超过7天不可再申请售后服务");
           }
       }
        Map<String,Object> map=new HashMap<>();
        ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItemNew(orderId, skuId);
        map.put("skuId",shopMemberOrderItem.getProductSkuId());//商品的skuid
        map.put("color",shopMemberOrderItem.getColor());//商品的颜色
        map.put("count",shopMemberOrderItem.getCount());//商品的数量
        map.put("name",shopMemberOrderItem.getName());//商品名称
        map.put("orderNumber",shopMemberOrderItem.getOrderNumber());//订单编号
        map.put("photo",shopMemberOrderItem.getSummaryImages());//商品的主图
        map.put("size",shopMemberOrderItem.getSize());//商品的尺码
        map.put("money",shopMemberOrderItem.getPrice());//商品的价格
        ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(orderId);
        BigDecimal saleMoney = shopMemberOrder.getSaleMoney();//优惠金额
        String saleMoneyString = saleMoney.toString();
        double v = Double.parseDouble(saleMoneyString);

        if (shopMemberOrder.getBuyWay()==0){
           if (saleMoney!=null&&v!=0.00){
               //当使用优惠券的时候
               //BigDecimal s=new BigDecimal();
               BigDecimal price = shopMemberOrderItem.getPrice();
               BigDecimal count = BigDecimal.valueOf(shopMemberOrderItem.getCount());
               BigDecimal realMoney = price.multiply(count);
               BigDecimal totalMoney = shopMemberOrder.getTotalMoney();
               float f1=Float.parseFloat(realMoney.toString());
               float f2=Float.parseFloat(totalMoney.toString());
               float f4=Float.parseFloat(saleMoney.toString());
               float f5=Float.parseFloat(price.toString());
               float f3=f1/f2;
               float f6=f1-f4*f3;
               DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
               String p=decimalFormat.format(f6);
//            //可退金额
//            BigDecimal divide = realMoney.divide(totalMoney, 5);
//            DecimalFormat f=new DecimalFormat("0.0000000");
//            String format = f.format(divide);
//            double d1 = Double.parseDouble(format);
//            BigDecimal decimal = BigDecimal.valueOf(d1);
//            BigDecimal multiply = saleMoney.multiply(decimal);
//            BigDecimal subtract = price.subtract(multiply);
//            //BigDecimal refundMoney = price.subtract(saleMoney.multiply(realMoney.multiply(count).divide(totalMoney,2)));
               map.put("refundMoney",p);//可退金额

           }else {
               BigDecimal price = shopMemberOrderItem.getPrice();
               BigDecimal count = BigDecimal.valueOf(shopMemberOrderItem.getCount());
               BigDecimal realMoney = price.multiply(count);
               map.put("refundMoney",realMoney);//可退金额
           }
       }else {
           if (shopMemberOrder.getBuyWay()==1){
               map.put("refundMoney",wxOrderMapper.selectTeamNew(shopMemberOrder.getTeamId()).getActivityPrice()*shopMemberOrderItem.getCount());
           }else{
               map.put("refundMoney",wxOrderMapper.selectActiveNew(shopMemberOrder.getSecondId()).getActivityPrice()*shopMemberOrderItem.getCount());
           }
       }
        return Response.success(map);
        }
    /**
     * 提交申请
     */
    @Override
    public synchronized  Response submit(SubmitRequest request) {
        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefund(request.getOrderId(), request.getSkuId());
        if (shopOrderAfterSale!=null){
            if (shopOrderAfterSale.getStatus()==2){
                wxOrderMapper.deleteRefundOrder(Long.parseLong(request.getOrderId()),request.getSkuId());
            }else {
                return Response.errorMsg("该订单已经申请过售后,请勿重新申请");
            }
        }
        ShopOrderAfterSale refundOrder=new ShopOrderAfterSale();
        Long timeMillis = System.currentTimeMillis();//时间戳
        String afterSaleId=timeMillis.toString()+request.getOrderId();
        refundOrder.setAfterSaleId(afterSaleId);//售后订单编号
        //refundOrder.setMsg("");//退款原因
        refundOrder.setName(request.getName());//退款人姓名
        refundOrder.setPhone(request.getPhone());//退款人手机号
        refundOrder.setOrderId(request.getOrderId().toString());//订单号
        refundOrder.setApplyTime(timeMillis);//申请时间
        refundOrder.setType(request.getType());//退款类型
        String money = request.getRefundMoney().toString();
        refundOrder.setApplyBackMoney(Double.parseDouble(money));//申请退款金额
        refundOrder.setSkuId(request.getSkuId());//商品的skuId
        refundOrder.setReasons(request.getReson());//申请退款理由
        refundOrder.setImgFirst(request.getPhoto1());//退款图片
        refundOrder.setStoreId(request.getStoreId());//商家id
        refundOrder.setMemberId(request.getUserId());//会员id
        refundOrder.setRefundRemark(refundOrder.getRefundRemark());//退款说明
        ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItem(Long.parseLong(request.getOrderId()), request.getSkuId());
        refundOrder.setRefundName(shopMemberOrderItem.getName());
        refundOrder.setRefundCount(request.getCount());
        //改变订单状态
        refundOrder.setStatus(0);
        wxOrderMapper.insertRefundOrder(refundOrder);
        try {
            applyRefund(Long.parseLong(refundOrder.getOrderId()),refundOrder.getSkuId(),ServiceAdvice.applyRefund);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("服务推送失败,请稍后重试");
        }
        /**
         * 填充操作日志
         */
        StoreRefundOrderActionLog log=new StoreRefundOrderActionLog();
        log.setActionName("开始申请售后");//添加操作
        log.setActionTime(timeMillis);//添加操作时间
        log.setRefundOrderId(Long.parseLong(refundOrder.getAfterSaleId()));//添加订单号
        wxOrderMapper.updateLog(log);
        Map<String,Object> map=new HashMap<>();
        map.put("afterSaleId",afterSaleId);//返回新的售后单号
        return Response.success(map);
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
        StoreRefundOrder reOrder = MybatisOperaterUtil.getInstance().findOne(new StoreRefundOrder(),
                new MybatisSqlWhereBuild(StoreRefundOrder.class).eq(StoreRefundOrder::getOrderNo, orderNo));
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
    /**
     * 查询所有售后列表
     * @param request
     * @return
     */
    @Override
    public Response selectRefundList(RefundRquest request) {
        PageHelper.startPage(request.getPageNumber(),request.getPageSize());
        List<RefundResponse> list = wxOrderMapper.selectRefundList(request);
        long timeMillis = System.currentTimeMillis();
        if(list!=null){
        for (RefundResponse refundResponse : list) {
            String orderId = refundResponse.getOrderId();//订单号
            long OrderNo = Long.parseLong(orderId);
            Long skuId = refundResponse.getSkuId();//skuid
            ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItem(OrderNo, skuId);
            if (shopMemberOrderItem==null){
                return Response.errorMsg("获取售后列表失败,稍后重试");
            }
            Integer count = shopMemberOrderItem.getCount();//商品数量
            String name = shopMemberOrderItem.getName();//商品名称
            String summaryImages = shopMemberOrderItem.getSummaryImages();//商品主图
            String size = shopMemberOrderItem.getSize();//商品尺码
            //refundResponse.setRefundCount(count);
            refundResponse.setRefundName(name);
            refundResponse.setRefundSize(size);
            refundResponse.setRefundSummaryImages(summaryImages);
            refundResponse.setColor(shopMemberOrderItem.getColor());//商品颜色
            ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(shopMemberOrderItem.getOrderId());
            Long confirmSignedTime = shopMemberOrder.getConfirmSignedTime();
            if (confirmSignedTime!=0){
                if ((timeMillis-(refundResponse.getCreateTime().getTime()+1000*60*60*24*7))>0){
                    refundResponse.setIsRefund("1");//为1的时候不在显示申请售后按钮  为0的时候显示申请售后按钮
                }else{
                    refundResponse.setIsRefund("0");
                }
            }else {
                refundResponse.setIsRefund("0");
            }


        }
        }
//        else {
//            return Response.errorMsg("该用户暂时没有任何相售后订单");
//        }
        PageInfo<RefundResponse> poolResponsePageInfo = new PageInfo<>(list);
        return Response.success(poolResponsePageInfo);
    }

    /**
     * 根据售后订单编号查询订单详情
     * @param afterSaleId
     * @return
     */
    @Override
    public Response selectRefundItem(Long afterSaleId,Long userId) {
        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefundItem(afterSaleId,userId);
        ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(Long.parseLong(shopOrderAfterSale.getOrderId()));
        String orderNumber = shopMemberOrder.getOrderNumber();
        Map<String,Object> map=new HashMap<>();

        map.put("status",shopOrderAfterSale.getStatus());//售后订单状态
        Integer status = shopOrderAfterSale.getStatus();
        //：0 默认 退款中，1 退货成功 2 退款失败', 3 处理中
        if(status==0){
            map.put("mation","商家正在加急处理中,请您耐心等候");
        }
        if (status==3){
            map.put("mation","商家正在加急处理中,请您耐心等候");
        }
        if(status==1){
            map.put("mation","商家已退款成功,请注意查收");
        }if(status==2){
            map.put("mation",shopOrderAfterSale.getMsg());
        }
        map.put("refundMoney",shopOrderAfterSale.getApplyBackMoney());//申请退款金额
        map.put("type",shopOrderAfterSale.getType());//'退款类型：0 默认 退款，1 退货退款',
        Long orderId = Long.parseLong(shopOrderAfterSale.getOrderId());
        ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItem(orderId, shopOrderAfterSale.getSkuId());
        map.put("refundName",shopMemberOrderItem.getName());//退款商品名称
        map.put("refundCount",shopOrderAfterSale.getRefundCount());//退款数量
        map.put("refundColor",shopMemberOrderItem.getColor());//退款商品颜色
        map.put("refundSize",shopMemberOrderItem.getSize());//退款商品尺码
        map.put("refundPhoto",shopMemberOrderItem.getSummaryImages());//退款商品主图
        map.put("refundApplyTime",TimeUtils.longFormatString(shopOrderAfterSale.getApplyTime()));//申请退款时间
        map.put("refundAfterSaleId",shopOrderAfterSale.getAfterSaleId());//售后订单编号
        map.put("refundReson",shopOrderAfterSale.getReasons());//退款原因
        map.put("refundRemark",shopOrderAfterSale.getRefundRemark());//退款说明
        map.put("orderId",shopMemberOrderItem.getOrderId());//订单号
        map.put("orderNumber",orderNumber);
        map.put("skuId",shopMemberOrderItem.getProductSkuId());
        if (shopOrderAfterSale.getImgFirst()!=null){
            String[] split = shopOrderAfterSale.getImgFirst().split(",");
            //String[]photo=new String[5];
            map.put("photo",split);//图片数组
        }
        map.put("refundDealTime",TimeUtils.longFormatString(shopOrderAfterSale.getOperateTime()));//卖家受理时间
        map.put("realMoney",shopOrderAfterSale.getBackMoney());//商家退款金额
        return Response.success(map);
    }
    /**
     * 查看订单详情
     * @param id
     * @param userId
     * @return
     */
    @Override
    public Response selectOrderItem(Long id, Long userId,Long storeId) {
        ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrderItem(id, userId);
//        ShopMemberOrder shopMemberOrder = MybatisOperaterUtil.getInstance().findOne(new ShopMemberOrder(), new MybatisSqlWhereBuild(ShopMemberOrder.class)
//                .eq(ShopMemberOrder::getId, id));
        if (shopMemberOrder==null){
            return Response.errorMsg("暂无该商品详情数据,清稍后重试");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("TESTttttttttttttttttttt",id+shopMemberOrder.getId());
        map.put("orderType",shopMemberOrder.getOrderType());//'订单类型：到店提货或送货上门(0:到店提货;1:送货上门)',
        if (shopMemberOrder.getOrderType()==0){
            StoreBusiness storeBusiness = wxOrderMapper.selectAdress(storeId);
            map.put("reciveName",storeBusiness.getBusinessName());//收件人姓名
            map.put("phone",storeBusiness.getPhoneNumber());//收件人电话
            map.put("adress",storeBusiness.getProvince()+storeBusiness.getCity()+storeBusiness.getCounty()+storeBusiness.getBusinessAddress());//收件人地址
        }else {
            map.put("reciveName",shopMemberOrder.getReceiverName());//收件人姓名
            map.put("phone",shopMemberOrder.getReceiverPhone());//收件人电话
            map.put("adress",shopMemberOrder.getReceiverAddress());//收件人地址
        }
        Integer orderStatus = shopMemberOrder.getOrderStatus();
        map.put("orderStatus",orderStatus);//订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货',
        //TimeUtils时间转换工具
        long timeMillis = System.currentTimeMillis();
        if(orderStatus==0){
         //购买方式 0：普通   1：团购  2：秒杀'
            if (shopMemberOrder.getBuyWay()==2||shopMemberOrder.getBuyWay()==1){
                Long time=shopMemberOrder.getCreateTime()+60*1000*45;//剩余付款时间
              timeMillis = System.currentTimeMillis();
                time=time-timeMillis;
                map.put("time",time);
            }else {
                Long time=shopMemberOrder.getCreateTime()+60*1000*60*24;//剩余付款时间
                time=time-timeMillis;
                map.put("time",time);
            }
        }
        if (orderStatus==6){
            Long time=shopMemberOrder.getDeliveryTime()+60*1000*60*24*7;//自动确认收货时间
            time=time-timeMillis;
            map.put("time",time);
        }

        //获取订单下的子订单详情
//        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopMemberOrderItem.class);
//        mybatisSqlWhereBuild.eq(ShopMemberOrderItem::getOrderId,shopMemberOrder.getId());
//        List<ShopMemberOrderItem> shopMemberOrderItems = MybatisOperaterUtil.getInstance().finAll(new ShopMemberOrderItem(), mybatisSqlWhereBuild);
        List<ShopMemberOrderItem> shopMemberOrderItems = wxOrderMapper.selectByOrderNo(id);
        logger.info("根据订单号去查询商品详情订单号="+id);
        Double money=0.00;
        List<Map<String,Object>> list=new ArrayList<>();
        Integer countTest=0;
        int size = shopMemberOrderItems.size();
        for (ShopMemberOrderItem shopMemberOrderItem : shopMemberOrderItems) {

            Map<String,Object> data=new HashMap<>();
            data.put("size",shopMemberOrderItem.getSize());//商品尺码
            data.put("color",shopMemberOrderItem.getColor());//商品颜色
            data.put("name",shopMemberOrderItem.getName());//商品名称
            data.put("count",shopMemberOrderItem.getCount());//商品数量
            data.put("meney",shopMemberOrderItem.getPrice());//商品价格
            data.put("photo",shopMemberOrderItem.getSummaryImages());//商品主图
            data.put("skuId",shopMemberOrderItem.getProductSkuId());//商品的skuId
            data.put("productId",shopMemberOrderItem.getProductId());
            //用户实际付款金额；需付款=订单总价格+运费-优惠券
            BigDecimal price = shopMemberOrderItem.getPrice();
            Integer count = shopMemberOrderItem.getCount();
            String string = price.multiply(BigDecimal.valueOf(count)).toString();
            if (shopMemberOrder.getBuyWay()==0){
                money =money+ Double.parseDouble(string);
            }else {
                if (shopMemberOrder.getBuyWay()==1){
                    money=money+wxOrderMapper.selectTeam(shopMemberOrder.getTeamId()).getActivityPrice()*shopMemberOrderItem.getCount();
                }else{
                    money=money+wxOrderMapper.selectActive(shopMemberOrder.getSecondId()).getActivityPrice()*shopMemberOrderItem.getCount();
                }
            }
            ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefund(shopMemberOrderItem.getOrderId().toString(), shopMemberOrderItem.getProductSkuId());
            if (shopOrderAfterSale!=null){
                data.put("refundStatus",shopOrderAfterSale.getStatus());//退款状态：0 默认 退款中，1 退货成功 2 退款失败
                if (shopOrderAfterSale.getStatus()!=2){
                    countTest=countTest+1;
                }
            }
            if (shopMemberOrder.getBuyWay()==1){//团购
                map.put("activeMoney",wxOrderMapper.selectTeam(shopMemberOrder.getTeamId()).getActivityPrice());
            }
            if (shopMemberOrder.getBuyWay()==2){//秒杀
                map.put("activeMoney",wxOrderMapper.selectActive(shopMemberOrder.getSecondId()).getActivityPrice());
            }
            list.add(data);
        }
        if (countTest==size){
            map.put("banner",1);
        }else {
            map.put("banner",0);//0显示  1不显示
        }
        Long confirmSignedTime = shopMemberOrder.getConfirmSignedTime();//订单收货的时间


        if (confirmSignedTime!=0){
            if (timeMillis>confirmSignedTime+1000*60*60*24*7){
                map.put("fullApplyTime",1);//超过申请售后的时间,不再显示申请售后按钮
            }else{
                map.put("fullApplyTime",0);//没超过申请售后的时间,显示申请售后按钮
            }
        }else {
            map.put("fullApplyTime",0);//没超过申请售后的时间,显示申请售后按钮
        }
        map.put("itemList",list);
        map.put("totalMoney",money);//商品总价
        map.put("expressMoney",shopMemberOrder.getExpressMoney());//邮费
        map.put("saleMoney",shopMemberOrder.getSaleMoney());//优惠金额
        map.put("needMoney",BigDecimal.valueOf(money).add(shopMemberOrder.getExpressMoney()).subtract(shopMemberOrder.getSaleMoney()));//需要付款的金额
        map.put("orderNumber",shopMemberOrder.getOrderNumber());//订单编号
        map.put("caeatTime",TimeUtils.longFormatString(shopMemberOrder.getCreateTime()));//下单时间
        map.put("buyWay",shopMemberOrder.getPaymentType());//'第三方支付类型：0(无)、1微信小程序、2微信公众号',
        map.put("dealNumber",shopMemberOrder.getPaymentNo());//交易编号(交易编号为支付后的微信的支付编号)
        if (orderStatus!=0){
            if (orderStatus!=5){
                map.put("sendTime",TimeUtils.longFormatString(shopMemberOrder.getDeliveryTime()));//发货时间
            }

        }
        return Response.success(map);
    }

    /**
     * 删除订单
     * @param afterSaleId
     * @param userId
     * @return
     */
    @Override
    public Response deleteOrder(Long afterSaleId, Long userId) {
        //ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(id);
        wxOrderMapper.deleteRefundOrderNew(afterSaleId,userId);
        return Response.success();
    }

    @Override
    public Response selecrTime(Long afterSaleId, Long userId) {
        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRufund(afterSaleId.toString(), userId);
        if(shopOrderAfterSale==null){
            return Response.errorMsg("");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("status",shopOrderAfterSale.getStatus());//'退款状态：0 默认 退款中，1 退货成功 2 退款失败 3处理中',
        map.put("applyMoney",shopOrderAfterSale.getApplyBackMoney());//申请退款金额
        map.put("dealTime",TimeUtils.longFormatString(shopOrderAfterSale.getOperateTime()));//受理时间
        map.put("refundTime",TimeUtils.longFormatString(shopOrderAfterSale.getOperateTime()));
        map.put("realyMonry",shopOrderAfterSale.getBackMoney());//实际退款金额
//        ArrayList<Long> list = wxOrderMapper.selectStorePhone(shopOrderAfterSale.getStoreId());//商家手机号
//        if (list!=null){
//            //该功能已移除
//            for (Long aLong : list) {
//                map.put("phone",aLong );//商家手机号
//            }
//        }
        return Response.success(map);
    }

    @Override
    public Response orderDelete(Long id, Long userId) {
        wxOrderMapper.updateOrderNew(id,userId);
        return Response.success();
    }

    /**
 * 订单自动确认时间
 */
public long buildSurplusSupplierAutoTakeTime(long sendTime) {
    if(sendTime == 0 ){//未发货则返回0
        return 0;
    }

    //15天
    long maxTime = 14 * 24 * 60 * 60 * 1000;

    long time = 0;//


    long supplierAutoTakeTime = sendTime + maxTime;//自动确认收货时间节点
    long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
    surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
    if(surplusSupplierAutoTakeTime < 0){
        surplusSupplierAutoTakeTime = 0;
    }
    return surplusSupplierAutoTakeTime;
}
    /**
     * 售后相关通知
     */
    public void  applyRefund(long shopMemberOrderId,Long skuId,ServiceAdvice serviceAdvice) {
        if(StringUtils.isEmpty(weixinServiceUrl)){
            logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
            return ;
        }

        ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(shopMemberOrderId);
        if(shopMemberOrder == null){
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
            return ;
        }
        //获取APPID
        Long storeId = shopMemberOrder.getStoreId();

        //如果是店中店用户
//        if (style==1){
//            storeId=3;
//        }
        StoreBusiness storeBusiness = wxOrderMapper.getStoreBusinessByIdNew(storeId);
        String appId = null;
        //获取OpenId
        long memberId = shopMemberOrder.getMemberId();
        ShopMember member = wxOrderMapper.getMemberByIdNew(memberId);
        String openId =null;
        if (storeBusiness.getWxaBusinessType()==1){//是店中店用户
            //storeId=11878L;
            storeId=Long.parseLong(shopInId);
            Long inShopMemberId = member.getInShopMemberId();
            if (inShopMemberId==null){
                inShopMemberId=memberId;
            }
            ShopMember shopMember = wxOrderMapper.selectShopMemberNew(inShopMemberId);
            openId=shopMember.getBindWeixin();
            List<StoreWxa> storeWxas = wxOrderMapper.selectStoreWxaListNew(storeId);
            StoreWxa storeWxa = null;
            if (storeWxas.size() > 0) {
                storeWxa = storeWxas.get(0);
            }
            appId=storeWxa.getAppId();
        }else {
            openId = member.getBindWeixin();
            appId=storeBusiness.getWxaAppId();
        }

        String form_id = shopMemberOrder.getPayFormId();
        if(StringUtils.isNotEmpty(form_id) && StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(appId)){
            logger.info("开始发送支付相关模板通知");
//            //如果是店中店用户
//            if (style==1){
//                form_id="";
//                openId="";
//                appId="";
//            }
            serviceSenda(form_id,skuId,openId,appId,serviceAdvice,shopMemberOrderId);
        }else{
            logger.info("必要条件缺失无法发送服务通知，请注意排查问题！");
        }
    }

    /** 售后
     * form_id 标示
     * openId 微信UId
     *appId 小程序id
     *
     * shopMemberOrderId 订单号
     * 发送模板通知
     */
    public void serviceSenda(String form_id,Long skuId,String openId,String appId,ServiceAdvice serviceAdvice,long shopMemberOrderId) {
        logger.info("----------------------");
        logger.info("------发送模板通知sendTemplateAdvice----------------form_id:"+form_id);
        logger.info("------发送模板通知sendTemplateAdvice----------------openId:"+openId);
        logger.info("------发送模板通知sendTemplateAdvice----------------appId:"+appId);
        logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+JSON.toJSONString(serviceAdvice));
        logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+shopMemberOrderId);
        logger.info("----------------------");
        if(StringUtils.isEmpty(weixinServiceUrl)){
            logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
            return ;
        }
        Map<String, String> map = new HashMap<String, String>();
        //map.put("page", buildPage(serviceAdvice,shopMemberOrderId));
        map.put("form_id", form_id);
        map.put("appId", appId);
        map.put("openId", openId);
        //获取模板
        map.put("template_id", getTemplateId(appId,serviceAdvice));
        map.put("skuId",skuId.toString());
//		map.put("template_title", serviceAdvice.getTitle());
//		map.put("template_keywordIds", serviceAdvice.getKeywordIds());
//		map.put("data", JSONObject.toJSONString(buildData(serviceAdvice,shopMemberOrder)));
        fillKeyword(map,serviceAdvice,shopMemberOrderId);
        map.put("page", buildPage1(map,shopMemberOrderId));
        logger.info("----------------------");
        logger.info("----------------------");
        logger.info("----------------------");
        logger.info("----------------------");
        logger.info("模板通知发送参数:"+map.toString());
        logger.info("----------------------");
        logger.info("----------------------");
        logger.info("----------------------");
        logger.info("----------------------");
//Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
        //Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
        String resp = HttpClientUtil.doGet(weixinServiceUrl + sendTemplateAdviceUrl, map);
        logger.info("模板通知发送结果body:"+resp);
    }



    /**
     * 根据模板类型获得模板ID（如果无则创建）
     * @param serviceAdvice
     * @return
     */
    public String getTemplateId(String appId,ServiceAdvice serviceAdvice) {
        //最终返回的模板ID
        String wxaTemplateId = "";
        //获取小程序账号下的模板ID
        wxaTemplateId = getWxaTemplateId(appId, serviceAdvice);
        return wxaTemplateId;
    }

    /**
     * 获取小程序账号下的模板ID
     * 说明，如果没有该模板则进行创建
     * @param appId
     * @param serviceAdvice
     * @return
     */
    public String getWxaTemplateId(String appId, ServiceAdvice serviceAdvice) {
        String wxaTemplateId = "";
        //1、获取账号下模板列表，解析出模板ID
        String title = serviceAdvice.getTitle();
        String adviceListRet =  getTemplateAdviceList(appId);//获取通知模板列表
        logger.info("获取模板列表结果为adviceListRet"+adviceListRet);
        if(StringUtils.isEmpty(adviceListRet)){
            logger.info("获取模板列表结果为空，请排查问题");
            return wxaTemplateId;
        }
        JSONObject retJSON = JSON.parseObject(adviceListRet);//json字符串转换成jsonobject对象
        JSONArray arr = retJSON.getJSONArray("list");//jsonobject对象取得some对应的jsonarray数组
        for(int i=0;i<arr.size();i++){
            JSONObject job = arr.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            String template_title = (String)job.get("title");
            String template_id = (String)job.get("template_id");
            //如果标题相同则说明该模板已经存在
            if(template_title.equals(title)){
                wxaTemplateId = template_id;
            }
        }

        //2、如果模板不存在则进行添加模板
        if(StringUtils.isEmpty(wxaTemplateId)){
            String addRet = addTemplateAdvice(appId,serviceAdvice);
            logger.info("添加模板通知获取模板返回结果addRet："+addRet);
            if(StringUtils.isNotEmpty(addRet)){
                JSONObject addRetJSON = JSON.parseObject(addRet);//json字符串转换成jsonobject对象
                int errcode = addRetJSON.getIntValue("errcode");
                if(errcode == 0){
                    wxaTemplateId = addRetJSON.getString("template_id");
                    logger.info("添加模板通知获取模板ID成功，wxaTemplateId:"+wxaTemplateId+"返回结果addRet："+addRet);
                }else{
                    logger.info("添加模板通知获取模板ID失败，返回结果addRet："+addRet);
                }
            }else{
                logger.info("添加模板通知获取模板ID失败，返回结果addRet："+addRet);
            }
        }else{
            logger.info("从账号模板列表中解析出模板ID成功，wxaTemplateId:"+wxaTemplateId);
        }
        return wxaTemplateId;
    }

    public String getTemplateAdviceList(String appId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        // Response<String> resp = Requests.get(weixinServiceUrl + getTemplateAdviceUrl).params(map).text();
        String adviceListRet=HttpClientUtil.doGet(weixinServiceUrl + getTemplateAdviceUrl,map);
        //String adviceListRet = resp.getBody();
        if(StringUtils.isEmpty(adviceListRet)){
            logger.info("获取模板列表结果为空，请排查问题"+weixinServiceUrl + getTemplateAdviceUrl+",map:"+JSON.toJSONString(map));
        }
        return adviceListRet;
    }
    /**
     * @param appId
     */
    public String addTemplateAdvice(String appId,ServiceAdvice serviceAdvice) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        map.put("template_id",serviceAdvice.getId());
        map.put("template_keywordIds", serviceAdvice.getKeywordIds());
        //Response<String> resp = Requests.get(weixinServiceUrl + addTemplateAdviceUrl).params(map).text();
        String adviceListRet = HttpClientUtil.doGet(weixinServiceUrl + addTemplateAdviceUrl,map);
        return adviceListRet;
    }

    /**
     * 通知点击跳转的小程序页面（可带参数）
     * @param serviceAdvice
     * @return
     */
    public String buildPage(ServiceAdvice serviceAdvice,long shopMemberOrderId) {
        return "pages/component/orderDetail/orderDetail?orderId="+shopMemberOrderId;
    }

    public String buildPage1(Map<String, String> map,long shopMemberOrderId) {
        String afterSaleId = map.get("afterSaleId");
        String storeId = map.get("storeId");
        String userId=map.get("userId");
        return "pages/component/refundDetail/refundDetail?saleId="+afterSaleId+"&userId="+userId+"&storeId="+storeId;
    }
    /**
     * 填充关键字
     * @param serviceAdvice
     * @return
     */
    public void fillKeyword(Map<String, String> map, ServiceAdvice serviceAdvice, long shopMemberOrderId) {
        //Long skuId=45439L;
        String skuId = map.get("skuId");
//		ShopOrderAfterSale shopOrderAfterSale =null;
//		if (skuId!=null){
//			shopOrderAfterSale=shopMemberOrderService.selectRefund(shopMemberOrderId, Long.parseLong(skuId));
//		}
        ShopOrderAfterSale shopOrderAfterSale =null;
        Long orderId=shopMemberOrderId;
        if (skuId!=null){
            shopOrderAfterSale=wxOrderMapper.selectRefund(orderId.toString(),Long.parseLong(skuId));
            //shopOrderAfterSale = shopRefundMapper.applyRefund(orderId.toString(), Long.parseLong(skuId));
            map.put("afterSaleId",shopOrderAfterSale.getAfterSaleId());
            map.put("storeId",shopOrderAfterSale.getStoreId().toString());
            map.put("userId",shopOrderAfterSale.getMemberId().toString());
        }

        ShopMemberOrder shopMemberOrder=wxOrderMapper.selectOrder(shopMemberOrderId);
        //ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(shopMemberOrderId);

        int buyWay = shopMemberOrder.getBuyWay();//购买方式 0：普通  1：团购  2：秒杀
        logger.info("======大发顺丰====购买方式 0：普通  1：团购  2：秒杀======buyWay："+buyWay+",shopMemberOrderId:"+shopMemberOrderId);
        List<ShopMemberOrderItem> list=wxOrderMapper.selectByOrderNo(shopMemberOrder.getId());
        //List<ShopMemberOrderItem> list = shopMemberOrderService.getMemberOrderItemList(shopMemberOrder.getId());
        String names = "";//商品详情，商品标题集合，逗号分隔
        for(ShopMemberOrderItem  item : list){
            names = "," + names + item.getName();
        }
        if(names.length()>0){
            names = names.substring(1,names.length() );
        }
        logger.info("======大发顺丰====订单商品名称names："+names);
        logger.info("======大发顺丰====订单类型serviceAdvice："+JSON.toJSONString(serviceAdvice));
        String id = serviceAdvice.getId();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //待付款提醒
        if(id.equals(ServiceAdvice.waitPayAdvice.getId())){
            map.put("keyword1", DateUtil.parseLongTime2Str(shopMemberOrder.getCreateTime()));//下单时间
            if(buyWay == 1){
                map.put("keyword2", "团购商品"+names);//商品详情
                map.put("keyword3",  "活动已成团，请尽快完成支付");//支付提醒
            }else if(buyWay == 2){
                map.put("keyword2", "秒杀商品"+names);//商品详情
                map.put("keyword3",  "活动即将到期，请尽快完成支付");//支付提醒
            }else{
                map.put("keyword2", names);//商品详情
                map.put("keyword3",  "保留即将到期，请尽快完成支付");//支付提醒
            }
        }else if(id.equals(ServiceAdvice.paySuccessAdvice.getId())){   //付款成功通知
            map.put("keyword1",  DateUtil.parseLongTime2Str(shopMemberOrder.getPayTime()));//付款时间
            map.put("keyword2",  names);//商品详情
            int orderType = shopMemberOrder.getOrderType();//订单类型：到店提货或送货上门(0:到店提货;1:送货上门) /**
            if(orderType == 0){
                map.put("keyword3","待提货");//订单状态
            }else{
                map.put("keyword3","待发货");//订单状态
            }
            map.put("keyword4","提货二维码在订单详情中查看");//备注说明

            //订单取消通知
        }else if(id.equals(ServiceAdvice.orderCancelAdvice.getId())){
            map.put("keyword1",  names);//商品详情
            map.put("keyword2", "已取消");//订单状态
            map.put("keyword3",  "抱歉，您购买的商品目前缺货！");//取消原因
        }else if(id.equals(ServiceAdvice.applyRefund.getId())) {////申请退款
            map.put("keyword1", "您好您申请的退款，商家已受理，请注意查收");//提示
            map.put("keyword3", shopOrderAfterSale.getRefundName());//商品名称
            map.put("keyword2", shopOrderAfterSale.getApplyBackMoney().toString());//退款金额  shopOrderAfterSale.getApplyBackMoney().toString()
            map.put("keyword4",  sdf.format(shopOrderAfterSale.getApplyTime()));//受理时间  shopOrderAfterSale.getApplyTime().toString()
        }else if (id.equals(ServiceAdvice.applyRefust.getId())){//退款失败
            map.put("keyword1", "您好您申请的退款，商家已拒绝，请尽快联系商家或重新申请");//提示
            map.put("keyword2", shopOrderAfterSale.getRefundName());//商品名称
            map.put("keyword3", shopOrderAfterSale.getApplyBackMoney().toString());//退款金额  shopOrderAfterSale.getApplyBackMoney().toString()
            map.put("keyword4",  sdf.format(shopOrderAfterSale.getOperateTime()));//受理时间  shopOrderAfterSale.getApplyTime().toString()
        }else if (id.equals(ServiceAdvice.applySuccess.getId())){//退款成功
            map.put("keyword1", "您好您申请的退款，商家退款完成，请注意查收");//提示
            map.put("keyword2", shopOrderAfterSale.getRefundName());//商品名称
            map.put("keyword3", shopOrderAfterSale.getBackMoney().toString());//退款金额
            map.put("keyword4", sdf.format(shopOrderAfterSale.getOperateTime()));//受理时间
        }else{
            logger.info("位置模板通知ID，请排查问题，id:"+id);
        }
        logger.info("======大发顺丰====填充后的Map："+JSON.toJSONString(map));
    }

}
