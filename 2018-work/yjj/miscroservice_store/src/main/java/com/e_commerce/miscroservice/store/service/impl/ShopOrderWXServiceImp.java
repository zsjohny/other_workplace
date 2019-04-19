package com.e_commerce.miscroservice.store.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.ServiceAdvice;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.store.entity.StaticVariableEntity;
import com.e_commerce.miscroservice.store.entity.emuns.OrderStatus;
import com.e_commerce.miscroservice.store.entity.emuns.RefundStatus;
import com.e_commerce.miscroservice.store.entity.vo.*;
import com.e_commerce.miscroservice.store.mapper.StoreRefundOrderMapper;
import com.e_commerce.miscroservice.store.mapper.WXOrderMapper;
import com.e_commerce.miscroservice.store.rpc.ShopMemberRpcService;
import com.e_commerce.miscroservice.store.rpc.StoreBusinessAccountRpcService;
import com.e_commerce.miscroservice.store.service.ShopOrderWXService;
import com.e_commerce.miscroservice.store.utils.NumberUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopOrderWXServiceImp implements ShopOrderWXService {
    @Value("${skipShop.storeId}")
    public String skipShopStoreId;

    private Long operateTime;
    private Double money;
    private Integer status;


    public static String weixinServiceUrl ="https://weixinonline.yujiejie.com";
    //public String weixinServiceUrl="https://local.yujiejie.com/jweixin/";
    public static String sendTemplateAdviceUrl = "/serviceAdvice/sendTemplateAdvice";
    public static String getTemplateAdviceUrl = "/serviceAdvice/getTemplateAdvice";
    public static String addTemplateAdviceUrl = "/serviceAdvice/addTemplateAdvice";
    @Autowired
    public WXOrderMapper wxOrderMapper;
    @Autowired
    public StoreRefundOrderMapper storeRefundOrderMapper;
    private Log logger = Log.getInstance(ShopOrderWXServiceImp.class);
    @Autowired
    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;
     @Autowired
    private ShopMemberRpcService shopMemberRpcService;


    @Override
    public Response getRefundOrderList(ShopMemberOrderRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ShopMemberOrderResponse> refundOrderList = wxOrderMapper.getRefundOrderListNew(request);
        if (refundOrderList==null){
            return Response.errorMsg("暂无数据");
        }
        for (ShopMemberOrderResponse shopMemberOrderResponse : refundOrderList) {
            Long id = shopMemberOrderResponse.getId();//订单号
            List<ShopMemberOrderItemResponse> shopMemberOrderItemResponses = wxOrderMapper.selectByOrderId(id);
            for (ShopMemberOrderItemResponse response : shopMemberOrderItemResponses) {
                response.setBuyCount(response.getCount());//商品购买数量;
                Long skuId = response.getProductSkuId();//商品的sku
                Long orderId = response.getOrderId();//订单编号
                ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefund(orderId.toString(), skuId);
                if (shopOrderAfterSale != null) {
                    shopMemberOrderResponse.setIsRefund("1");
                    String afterSaleId = shopOrderAfterSale.getAfterSaleId();
                    response.setAfterSaleId(afterSaleId);//添加售后单号
                } else {
                    shopMemberOrderResponse.setIsRefund("0");
                }
            }
            shopMemberOrderResponse.setList(shopMemberOrderItemResponses);
        }
        PageInfo<ShopMemberOrderResponse> poolResponsePageInfo = new PageInfo<>(refundOrderList);
        return Response.success(poolResponsePageInfo);
    }

    /**
     * 查询售后订单列表
     *
     * @param request
     * @return
     */
    @Override
    public Response selectMyRefundList(RefundRquest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<RefundResponse> list = wxOrderMapper.selectNewRefund(request);
        if (list==null){
            return Response.errorMsg("暂无数据");
        }
        for (RefundResponse refundResponse : list) {
            ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(Long.parseLong(refundResponse.getOrderId()));
            refundResponse.setOrderNumber(shopMemberOrder.getOrderNumber());
            String orderId = refundResponse.getOrderId();//订单号
            long OrderNo = Long.parseLong(orderId);
            Long skuId = refundResponse.getSkuId();//skuid
            ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItem(OrderNo, skuId);
            Integer count = shopMemberOrderItem.getCount();//商品数量
            String name = shopMemberOrderItem.getName();//商品名称
            String summaryImages = shopMemberOrderItem.getSummaryImages();//商品主图
            String size = shopMemberOrderItem.getSize();//商品尺码
            //refundResponse.setRefundCount(count);
            refundResponse.setRefundName(name);
            refundResponse.setRefundSize(size);
            refundResponse.setRefundSummaryImages(summaryImages);
            refundResponse.setColor(shopMemberOrderItem.getColor());//商品颜色
        }
        PageInfo<RefundResponse> poolResponsePageInfo = new PageInfo<>(list);
        return Response.success(poolResponsePageInfo);
    }


    /**
     * 根据售后订单编号查寻订单详情
     *
     * @param afterSaleId
     * @param storeId
     * @return
     */
    @Override
    public Response selectOrderDetail(Long afterSaleId, Long storeId) {
        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectRefundNew(afterSaleId, storeId);
        Map<String, Object> map = new HashMap<>();
        if (shopOrderAfterSale != null) {

            Integer status = shopOrderAfterSale.getStatus();//'退款状态：0 默认 退款中，1 退货成功 2 退款失败',
            map.put("status", status);//退款状态
            map.put("afterSaleId", shopOrderAfterSale.getAfterSaleId());//售后订单id
            map.put("type", shopOrderAfterSale.getType());//退款类型 '退款类型：0 默认 退款，1 退货退款',
            map.put("refundCount", shopOrderAfterSale.getRefundCount());//退款数量
            map.put("refundMoney", shopOrderAfterSale.getApplyBackMoney());//申请退款金额
            if (shopOrderAfterSale.getStatus()==1){
                map.put("refundMoney",shopOrderAfterSale.getBackMoney());
            }
            map.put("refundReson", shopOrderAfterSale.getReasons());//退款原因
            //map.put("refundPhoto", shopOrderAfterSale.getImgFirst());//退款图片
            map.put("applyName", shopOrderAfterSale.getName());//申请人姓名
            map.put("applyPhone", shopOrderAfterSale.getPhone());//申请人电话
            map.put("applyTime", shopOrderAfterSale.getApplyTime());//申请时间
            map.put("mation", "进行售后申请");
//            if (shopOrderAfterSale.getType() != 0) {
//                //退货退款需要显示商家回寄信息
//                // TODO: 2018/11/26
//                map.put("myAdress","");
//                map.put("myName","");
//                map.put("myPhone","");
//                List<Address> addresses = wxOrderMapper.selecrMyAdress(Integer.parseInt(storeId.toString()));
//               if (addresses.size()>0){
//                   if (addresses.size()>1){
//                       for (Address address : addresses) {
//                       if (address.getIsDefault()==0){
//                           String receiverName = address.getReceiverName();
//                           String telephone = address.getTelephone();
//                           String addrFull = address.getAddrFull();
//                           map.put("myAdress",addrFull);
//                           map.put("myName",receiverName);
//                           map.put("myPhone",telephone);
//                       }
//                   }}else {
//                       Address address = addresses.get(0);
//                       String receiverName = address.getReceiverName();
//                       String telephone = address.getTelephone();
//                       String addrFull = address.getAddrFull();
//                       map.put("myAdress",addrFull);
//                       map.put("myName",receiverName);
//                       map.put("myPhone",telephone);
//                   }
//               }
//            }
            //根据订单号码查询订单详情信息
            ShopMemberOrderItem shopMemberOrderItem = wxOrderMapper.selectItem(Long.parseLong(shopOrderAfterSale.getOrderId()), shopOrderAfterSale.getSkuId());
            map.put("photo", shopMemberOrderItem.getSummaryImages());//商品主图
            map.put("size", shopMemberOrderItem.getSize());//商品尺码
            map.put("name", shopMemberOrderItem.getName());//商品名称
            map.put("color", shopMemberOrderItem.getColor());//商品颜色
            map.put("reson", shopOrderAfterSale.getReasons());//退款理由
            if (shopOrderAfterSale.getRefundRemark()!=null){
                map.put("remark", shopOrderAfterSale.getRefundRemark());//退款说明
            }else {
                map.put("remark","");
            }
            if (shopOrderAfterSale.getImgFirst()!=null&&shopOrderAfterSale.getImgFirst().length()!=0){
                String[] split = shopOrderAfterSale.getImgFirst().split(",");
                    map.put("photos",split);//退款商品详情
            }
            ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(Long.parseLong(shopOrderAfterSale.getOrderId()));
            BigDecimal saleMoney = shopMemberOrder.getSaleMoney();
            String saleMoneyString = saleMoney.toString();
            double v = Double.parseDouble(saleMoneyString);
            Double backMoney = shopOrderAfterSale.getBackMoney();
//           if (shopMemberOrder.getBuyWay()==0){
//               if (backMoney!=null&&backMoney!=0.00){
//                   map.put("realMoney",backMoney);
//               }else {
//                   if (saleMoney!=null&&v!=0.00){
//                       //当使用优惠券的时候
//                       //BigDecimal s=new BigDecimal();
//                       BigDecimal price = shopMemberOrderItem.getPrice();
//                       BigDecimal count = BigDecimal.valueOf(shopMemberOrderItem.getCount());
//                       BigDecimal realMoney = price.multiply(count);
//                       BigDecimal totalMoney = shopMemberOrder.getTotalMoney();
//                       float f1=Float.parseFloat(realMoney.toString());
//                       float f2=Float.parseFloat(totalMoney.toString());
//                       float f4=Float.parseFloat(saleMoney.toString());
//                       float f5=Float.parseFloat(price.toString());
//                       float f3=f1/f2;
//                       float f6=f1-f4*f3;
//                       DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                       String p=decimalFormat.format(f6);
//                       map.put("realMoney",p);//可退金额
//
//                   }else {
//                       BigDecimal price = shopMemberOrderItem.getPrice();
//                       BigDecimal count = BigDecimal.valueOf(shopMemberOrderItem.getCount());
//                       BigDecimal realMoney = price.multiply(count);
//                       map.put("realMoney",realMoney);//可退金额
//                   }
//               }
//           }else {
//               if (shopMemberOrder.getBuyWay()==1){
//                   map.put("realMoney",wxOrderMapper.selectTeam(shopMemberOrder.getTeamId()).getActivityPrice()*shopOrderAfterSale.getRefundCount());
//               }else{
//                   map.put("realMoney",wxOrderMapper.selectActive(shopMemberOrder.getSecondId()).getActivityPrice()*shopOrderAfterSale.getRefundCount());
//               }
//           }


            // 填充售后订单操作日志
            List<StoreRefundOrderActionLog> refundOrderActionLogList = wxOrderMapper.selectLog(afterSaleId);
            List<Map<String, String>> actionLogList = new ArrayList<Map<String, String>>();
            for (StoreRefundOrderActionLog refundOrderActionLog : refundOrderActionLogList) {
                Map<String, String> actionLogMap = new HashMap<String, String>();
                actionLogMap.put("actionTime", TimeUtils.longFormatString(refundOrderActionLog.getActionTime()));//操作时间
                actionLogMap.put("actionName", refundOrderActionLog.getActionName());//操作名称
                actionLogList.add(actionLogMap);
            }
            map.put("actionLogList", actionLogList);//日志记录

        }
        return Response.success(map);
    }

    /**
     * 处理订单
     *
     * @param type
     * @param status
     * @param storeId
     * @param refundOrderNo
     * @return
     */
    @Override
    public Response dealOrder(Integer type, Integer status, Long storeId, Long refundOrderNo) {
        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectByStoreId(refundOrderNo, storeId);
        Map<String, Object> map = new HashMap<>();
        map.put("applyMoney", shopOrderAfterSale.getApplyBackMoney());//申请退款金额
        map.put("type",shopOrderAfterSale.getType());//退款类型
        return Response.success(map);
    }


    /**
     * 提交处理结果
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public Response dealOrderResult(ShopOrderWXRequest request) {
        Map<String,Object> map=new HashMap<>();

        long timeMillis = System.currentTimeMillis();

        ShopOrderAfterSale shopOrderAfterSale = wxOrderMapper.selectByStoreId(request.getRefundOrderNo(), request.getStoreId());

        if (request.getStatus()==1&&request.getType()==0){//退款   成功
            shopOrderAfterSale.setStatus(1);//更改退款状态
            status=1;
            shopOrderAfterSale.setBackMoney(request.getRealMoney());//实际退款金额
            money=request.getRealMoney();
            shopOrderAfterSale.setMsg(request.getMsg());//操作理由
            shopOrderAfterSale.setOperateTime(timeMillis);//操作时间
            operateTime=timeMillis;
        }
        if (request.getStatus()==2&&request.getType()==0){ //退款  失败
            shopOrderAfterSale.setStatus(2);
            shopOrderAfterSale.setMsg(request.getMsg());
            shopOrderAfterSale.setOperateTime(timeMillis);//操作时间
        }
        if (request.getStatus()==3&&request.getType()==1){//退货退款  同意

            //此时需要等待商家收到货以后才能退款成功
            shopOrderAfterSale.setStatus(3);//处理中
            shopOrderAfterSale.setOperateTime(timeMillis);//处理时间
            shopOrderAfterSale.setMsg(request.getMsg());//处理理由
        }
        if (request.getStatus()==1&&request.getType()==1){//退货退款  成功
            shopOrderAfterSale.setMsg(request.getMsg());//处理理由
            shopOrderAfterSale.setBackMoney(request.getRealMoney());//实际退款金额
            money=request.getRealMoney();
            operateTime=shopOrderAfterSale.getOperateTime();
            shopOrderAfterSale.setStatus(1);
            status=1;
            /**
             * 用户退款成功,如果该订单是商家申请平台代发,那么就应该进行同时退款,
             * 先通过售后单号查询订单号,然后根据订单号去查找与之相关联的商家订单!进行退款
             */

        }
        if (request.getStatus()==2&&request.getType()==1){//退货退款  失败
            shopOrderAfterSale.setStatus(2);;
            shopOrderAfterSale.setMsg(request.getMsg());//拒绝理由
            shopOrderAfterSale.setOperateTime(timeMillis);//操作时间
            operateTime=timeMillis;
            status=2;
        }
        shopOrderAfterSale.setStoreId(request.getStoreId());
        shopOrderAfterSale.setAfterSaleId(request.getRefundOrderNo().toString());
        int i = wxOrderMapper.updateRefundOrder(shopOrderAfterSale);
        if (i!=1){
            logger.info("售后单更新失败,请查看原因");
        }
        Map<String, String> param = new HashMap<>();
        param.put("orderId",shopOrderAfterSale.getOrderId());
        param.put("skuId",shopOrderAfterSale.getSkuId().toString());


        //小程序订单对应的供应商代发货订单
        StoreOrderNew storeOrderNew = null;
        /**
         * 资金明细!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        if (request.getStatus()==1&&request.getType()==0||request.getStatus()==1&&request.getType()==1){
            /**
             * 用户退款成功,如果该订单是商家申请平台代发,那么就应该进行同时退款,
             * 先通过售后单号查询订单号,然后根据订单号去查找与之相关联的商家订单!进行退款
             **/
            Long orderId = wxOrderMapper.selectOrderByRefundOrder(request.getRefundOrderNo());//获取到订单编号
            /**
             * 通过查询的i订单号去查询关联的商家订单 假设现在 查询到商家订单
             */
            Long appOrderId = wxOrderMapper.selectAppOrderId(orderId);
            Long orderNo=appOrderId;
            if (orderNo!=null){
                storeOrderNew = wxOrderMapper.selectByOrderNoNewF(orderNo);
                Integer count=shopOrderAfterSale.getRefundCount();
                StoreRefundOrderActionLog  storeRefundOrderActionLog=new StoreRefundOrderActionLog();
                storeRefundOrderActionLog.setRefundOrderId(appOrderId);
                storeRefundOrderActionLog.setActionTime(System.currentTimeMillis());
                String wxOrderId = shopOrderAfterSale.getOrderId();
                long wxOrderIdNew = Long.parseLong(wxOrderId);
                Integer countNew = wxOrderMapper.selectMemberOrderById(wxOrderIdNew);
                //根据memberId获取到用户的昵称
                String name=wxOrderMapper.selectName(shopOrderAfterSale.getMemberId());

                System.out.println("===============================================================");
                DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                Float totalMoney=Float.parseFloat(storeOrderNew.getTotalMoney().toString());
                Float f1=Float.parseFloat(count.toString());
                Float f2=Float.parseFloat(countNew.toString());
                Float surplus=f1/f2;
                Float moneyNew=totalMoney*surplus;
                String format = decimalFormat.format(moneyNew);
                Double money2=Double.parseDouble(format);
                System.out.println("===============================================================");

                storeRefundOrderActionLog.setActionName("门店<"+name+">进行售后"+count+"件故此订单原"+countNew+"件变为"+(countNew-count)+"件," +
                        "原订单付了"+storeOrderNew.getTotalPay()+",申请售后价格变为"+money2);
                 storeRefundOrderMapper.insertLog(storeRefundOrderActionLog);
                 /**
                 * 商家购买平台的订单也要进行退货
                 */
                YjjStoreBusinessAccount yjjStoreBusinessAccount = wxOrderMapper.selectBusinessAccount(request.getStoreId());

                //退款金额计算

                /**
                 * 填充日志,15以后退款金额加到可用资金 15天以前退到待结算资金
                 */
                Long deliveryTime=wxOrderMapper.selectCreateTime(wxOrderId);
                Long remainingTime = deliveryTime + (15 * 24 * 60 * 60 * 1000);
                if (timeMillis<=remainingTime){
                    //没超过15天时间,退到待结算
                    yjjStoreBusinessAccount.setWaitInMoney(yjjStoreBusinessAccount.getWaitInMoney()+money2);
                    int save = MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class)
                            .eq(YjjStoreBusinessAccount::getUserId,yjjStoreBusinessAccount.getUserId()));
                    if (save!=1){
                        logger.info("退款失败");
                    }
                }else {
                    //超过15天 退到可用
                    yjjStoreBusinessAccount.setRealUseMoney(yjjStoreBusinessAccount.getRealUseMoney()+money2);
                    int save = MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class)
                            .eq(YjjStoreBusinessAccount::getUserId,yjjStoreBusinessAccount.getUserId()));
                    if (save!=1){
                        logger.info("退款失败");
                    }
                }

                YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
                yjjStoreBusinessAccountLog.setUserId(request.getStoreId());
                yjjStoreBusinessAccountLog.setOperMoney(money2);
                yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.INCOME.getCode());
                yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue()+"-"+"APP商品退款");
                yjjStoreBusinessAccountLog.setType(StoreBillEnums.APP_REFUND_MONEY_SUCCESS.getCode());
                yjjStoreBusinessAccountLog.setAboutOrderNo(appOrderId.toString());
                yjjStoreBusinessAccountLog.setOrderNo(appOrderId.toString());
                yjjStoreBusinessAccountLog.setRemainderMoney(yjjStoreBusinessAccount.getWaitInMoney());
                int log = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
                if (log!=1){
                    logger.info("日志记录失败");
                }
            }
        }
        if (request.getStatus()==2){
            applyRefund(Long.parseLong(shopOrderAfterSale.getOrderId()),shopOrderAfterSale.getSkuId(),ServiceAdvice.applyRefust);
        }
        if (request.getStatus()==1){
            applyRefund(Long.parseLong(shopOrderAfterSale.getOrderId()),shopOrderAfterSale.getSkuId(),ServiceAdvice.applySuccess);
        }
        /**
         * 填充操作日志
         */
        String str=null;
        StoreRefundOrderActionLog log=new StoreRefundOrderActionLog();
        if (request.getStatus()==1){
          if (request.getType()==2){
              str="收货成功并退款,"+request.getMsg() ;
          }else{
              str="退款成功,"+request.getMsg();
          }
            ShopMember shopMember = shopMemberRpcService.findById(shopOrderAfterSale.getMemberId());
            //              Store账户操作
            YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setUserId(shopOrderAfterSale.getStoreId());
            yjjStoreBusinessAccountLog.setMemberId(shopOrderAfterSale.getMemberId());
            yjjStoreBusinessAccountLog.setOperMoney(shopOrderAfterSale.getBackMoney());
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue()+"-"+shopMember.getUserNickname()+"的订单");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.REFUND_MONEY_SUCCESS.getCode());
            /**
             * 根据订单编号查询orderNumber;
             */
            String orderNumber=wxOrderMapper.selectOrderNumber(shopOrderAfterSale.getOrderId());
            yjjStoreBusinessAccountLog.setAboutOrderNo(orderNumber);
            //账户修改
            storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        }
        if (request.getStatus()==2){
                str="退款失败,"+request.getMsg();

        }
        if (request.getStatus()==3){
            str="同意退款等待收货,"+request.getMsg();
        }
        log.setActionName(str);//添加操作
        log.setActionTime(timeMillis);//添加操作时间
        log.setRefundOrderId(request.getRefundOrderNo());//添加订单号
        wxOrderMapper.updateLog(log);

        //发货15天以后, 店家待结算资金入账
        if (storeOrderNew != null) {
            Long orderId = Long.parseLong(shopOrderAfterSale.getOrderId());
            ShopMemberOrder shopMemberOrder = wxOrderMapper.selectOrder(orderId);
            Integer type = storeOrderNew.getType();
            Long afterSend15Days = storeOrderNew.getSendTime() + 1000*3600*24*15;
            boolean isOverflow = System.currentTimeMillis() >= afterSend15Days;
            boolean isSendGoodsOrder = type.equals(1);
            logger.info("发货15天以后, 店家待结算orderId={}资金入账 发货后15天={}, 是否超出={}", orderId, afterSend15Days, isOverflow);
            if (isSendGoodsOrder && isOverflow ) {
                storeBusinessAccountRpcService.sendGoodsAfter15DaysWaitMoneyIn(shopMemberOrder.getOrderNumber(), shopOrderAfterSale.getStoreId(), storeOrderNew.getOrderNo());
            }
        }
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
    @Override
    public Response deleteMyOrder(Long refundOrderNo, Long storeId) {
        wxOrderMapper.deleteOrderMy(refundOrderNo.toString(),storeId);
        return Response.success();
    }

    @Override
    public Response refoundOrder(Long refundOrderNo, Long storeId) {
        wxOrderMapper.refoundOrder(refundOrderNo,storeId);
        return Response.success();
    }

    @Override
    public Response test() {
        logger.info("测试测试");
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
        yjjStoreBusinessAccountLog.setUserId(279L);
//				收入
        yjjStoreBusinessAccountLog.setInOutType(0);
        yjjStoreBusinessAccountLog.setOperMoney(Double.valueOf(30));
        yjjStoreBusinessAccountLog.setRemarks("测试");
        storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        return Response.success(yjjStoreBusinessAccountLog);
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
//        long storeId = shopMemberOrder.getStoreId();
//        StoreBusiness storeBusiness = wxOrderMapper.getStoreBusinessById(storeId);
//        String appId = storeBusiness.getWxaAppId();
//        //获取OpenId
//        long memberId = shopMemberOrder.getMemberId();
//        ShopMember member = wxOrderMapper.getMemberById(memberId);
//        String openId = member.getBindWeixin();
        //获取APPID
        Long storeId = shopMemberOrder.getStoreId();

        //如果是店中店用户
//        if (style==1){
//            storeId=3;
//        }
        StoreBusiness storeBusiness = wxOrderMapper.getStoreBusinessById(storeId);
        String appId = null;
        //获取OpenId
        long memberId = shopMemberOrder.getMemberId();
        ShopMember member = wxOrderMapper.getMemberById(memberId);
        String openId =null;
        if (storeBusiness.getWxaBusinessType()==1){//是店中店用户
            storeBusiness.setId(Long.parseLong(skipShopStoreId));
            Long inShopMemberId = member.getInShopMemberId();
            if (inShopMemberId==null){
                inShopMemberId=memberId;
            }
            ShopMember shopMember = wxOrderMapper.selectShopMember(inShopMemberId);
            openId=shopMember.getBindWeixin();
            List<StoreWxa> storeWxas = wxOrderMapper.selectStoreWxaList(storeBusiness.getId());
            StoreWxa storeWxa = null;
            if (storeWxas.size() > 0) {
                storeWxa = storeWxas.get(0);
            }
            appId=storeWxa.getAppId();

        }else {
            openId = storeBusiness.getBindWeixinId();
            appId=storeBusiness.getWxaAppId();
        }

        String form_id = shopMemberOrder.getPayFormId();
        if(StringUtils.isNotEmpty(form_id) && StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(appId)){
            logger.info("开始发送支付相关模板通知");
            //如果是店中店用户
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
            //map.put("keyword4",  sdf.format(shopOrderAfterSale.getOperateTime()));//受理时间  shopOrderAfterSale.getApplyTime().toString()
            map.put("keyword4",  sdf.format(operateTime));//受理时间  shopOrderAfterSale.getApplyTime().toString()
        }else if (id.equals(ServiceAdvice.applySuccess.getId())){//退款成功
            map.put("keyword1", "您好您申请的退款，商家退款完成，请注意查收");//提示
            map.put("keyword2", shopOrderAfterSale.getRefundName());//商品名称
            map.put("keyword3", shopOrderAfterSale.getBackMoney().toString());//退款金额
            map.put("keyword3", money.toString());//退款金额
            map.put("keyword4", sdf.format(operateTime));//受理时间
        }else{
            logger.info("位置模板通知ID，请排查问题，id:"+id);
        }
        logger.info("======大发顺丰====填充后的Map："+JSON.toJSONString(map));
    }


}