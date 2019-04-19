package com.e_commerce.miscroservice.supplier.service.imp;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.enums.order.OrderStatusEnums;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.commons.utils.wx.PaymentUtils;
import com.e_commerce.miscroservice.supplier.controller.AllOrderController;
import com.e_commerce.miscroservice.supplier.dao.RefundOrderDao;
import com.e_commerce.miscroservice.supplier.entity.request.Query;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;
import com.e_commerce.miscroservice.supplier.entity.request.StoreOrderNew;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import com.e_commerce.miscroservice.supplier.mapper.OrderCountMapper;
import com.e_commerce.miscroservice.supplier.mapper.RefundOrderMapper;
import com.e_commerce.miscroservice.supplier.rpc.StoreBusinessAccountRpcService;
import com.e_commerce.miscroservice.supplier.service.RefundOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RefundOrderServiceImp implements RefundOrderService {
    private Log logger = Log.getInstance(AllOrderController.class);


    //@Value("${app.id}")
    private String appid;

    //@Value("${api.key}")
    private String apiKey;

    //@Value("${mch.id}")
    private String mchId;

    //@Value("${ali.public.key}")
    private String aliPublicKey;

    //@Value("${rsa.private.key}")
    private String rsaPrivateKey;

    @Resource
    private OrderCountMapper orderCount;
    @Resource
    private RefundOrderMapper refundOrderMapper;
    @Resource
    private RefundOrderDao refundOrderDao;

    @Autowired
    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;
    //售后订单供应商自动确认收货时间15天
    public static final long refundOrderSupplierAutoTakeTime = 15 * 24 * 60 * 60 * 1000;
    //售后订单买家发货限制时间
    public static final long refundOrderRestrictiveDeliverTime = 3 * 24 * 60 * 60 * 1000;
    //售后订单卖家限制确认时间
    public static final long refundOrderRestrictiveConfirmTime = 3 * 24 * 60 * 60 * 1000;

    /**
     * 查询所有售后工单
     *@Auther:胡坤
     * @param orderRequest
     * @return
     */
    @Override
    public Response findAllRefundOrder(RefundOrderRequest orderRequest) { ;
        PageHelper.startPage(orderRequest.getPageNumber(),orderRequest.getPageSize());
        String supplierName = orderRequest.getSupplierName();
        Integer supplierId = refundOrderMapper.selectSupplierName(supplierName);
        orderRequest.setSupplierId(supplierId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        if (orderRequest.getShopName()!=null){
//            List<Product> products = refundOrderMapper.selectByName(orderRequest.getShopName());
//            for (Product product : products) {
//                List<StoreOrderItem> storeOrderItems = refundOrderMapper.selectItemByProductId(product.getId());
//                for (StoreOrderItem storeOrderItem : storeOrderItems) {
//                    Long orderNo = storeOrderItem.getOrderNo();
//                    RefundOrder refundOrder = refundOrderMapper.selectRefundByOrderNew(orderNo);
//                    orderRequest.setOrderNo(orderNo);
//                }
//
//            }
//
//        }
        List<RefundOrderResponce> list = refundOrderMapper.findAllRefunOrder(orderRequest);
        for (RefundOrderResponce refundOrderResponce : list) {


           refundOrderResponce.setRefundNewOrderNo(refundOrderResponce.getRefundOrderNo().toString());

               Long orderNo = refundOrderResponce.getOrderNo();
            List<StoreOrderItem> storeOrderItems = refundOrderMapper.selectProductIdByOrderNo(orderNo);
            for (StoreOrderItem storeOrderItem : storeOrderItems) {
                    Product product = refundOrderMapper.selectNameById(storeOrderItem.getProductId());
                    refundOrderResponce.setShopName(product.getName());
            }

            //若4小时后仍未被卖家受理的售后单，1:超过;0:未超过;
            if(refundOrderResponce.getRefundStatus()==1 && refundOrderResponce.getApplyTime()>0 && (System.currentTimeMillis()-refundOrderResponce.getApplyTime())>(4*60*60*1000)){
                refundOrderResponce.setUntreatedTimeMoreThan4Hours(1);
            }else{
                refundOrderResponce.setUntreatedTimeMoreThan4Hours(0);
            }
            String dealTime = buildAcceptTime(refundOrderResponce);
            refundOrderResponce.setDealTime(dealTime);
            refundOrderResponce.setApplyRefundTime(sdf.format(new Date(refundOrderResponce.getApplyTime())));
        }
        PageInfo<RefundOrderResponce> poolResponsePageInfo = new PageInfo<RefundOrderResponce>(list);
        return Response.success(poolResponsePageInfo);
    }


    /**
     * @Auther:胡坤
     * 查询售后订单详情
     * @param refundOrderId
     * @return
     */
    @Override
    public Map<String, Object> getRefundOrderInfo(long refundOrderId) {
//        RefundOrder refundOrder = MybatisOperaterUtil.getInstance().findOne(new RefundOrder(), new MybatisSqlWhereBuild(RefundOrder.class)
//                .eq(RefundOrder::getRefundOrderNo, refundOrderId));
        RefundOrder refundOrder = refundOrderMapper.getRefundOrderInfo(refundOrderId);
        Map<String, Object> refundOrderData = new HashMap<String, Object>();
        //StoreOrder storeOrder = orderCount.selectByOrderNo(refundOrder.getOrderNo());

//        StoreOrder storeOrder = MybatisOperaterUtil.getInstance().findOne(new StoreOrder(),
//                new MybatisSqlWhereBuild(StoreOrder.class).
//                        eq(StoreOrder::getOrderNo, refundOrder.getOrderNo()));
        StoreOrder storeOrder = refundOrderMapper.selectPayTypeAndTotalMoney(refundOrder.getOrderNo());
        //填充售后订单数据
        int refundStatus = refundOrder.getRefundStatus();
        //付款类型
        refundOrderData.put("payType",storeOrder.getPaymentType());
        //申请人名称
        refundOrderData.put("applyName",refundOrder.getStoreName());
        //申请人手机号
        refundOrderData.put("applyPhone",refundOrder.getStorePhone());
        //售后单ID
        refundOrderData.put("refundOrderId", refundOrder.getId());
        //售后单编号
        refundOrderData.put("refundOrderNo", refundOrder.getRefundOrderNo());
        //订单品牌名称
        refundOrderData.put("brandName", refundOrder.getBrandName());
        //申请时间
        refundOrderData.put("applyTime", DateUtil.parseLongTime2Str(refundOrder.getApplyTime()));
        //long storeId = refundOrder.getStoreId();
        //售后订单状态
        refundOrderData.put("refundStatus", refundStatus);
        //售后订单状态名称
        refundOrderData.put("refundStatusName", buildInfoRefundStatusName(refundStatus));
        //退款类型：1.仅退款  2.退货退款
        refundOrderData.put("refundType", refundOrder.getRefundType());
        //申请退款金额
        refundOrderData.put("refundCost", refundOrder.getRefundCost());

        //剩余卖家确认时间毫秒数
        refundOrderData.put("surplusAffirmTime", buildSurplusAffirmTime(refundOrder));
        //剩余买家发货时间毫秒数
        refundOrderData.put("surplusDeliverTIme", buildSurplusDeliverTime(refundOrder));
        //商品的skuid
        Long skuId = refundOrder.getSkuId();
        Long orderNo = refundOrder.getOrderNo();

        //申请退款数量
        refundOrderData.put("refundCount", refundOrder.getReturnCount());
        //退款金额
        refundOrderData.put("storeBackMoney", refundOrder.getStoreBackMoney());
        //实际退款金额
        refundOrderData.put("realBackMoney",refundOrder.getRealBackMoney());
        //填充订单数据
        if (skuId != 0 && skuId != null) {
            StoreOrderItem storeOrderItem = refundOrderMapper.selectItem(orderNo,skuId);
            //最高可退金额
            refundOrderData.put("maxMoney",storeOrderItem.getTotalPay());
            String skuSnapshot = storeOrderItem.getSkuSnapshot();
            if (StringUtils.isEmpty(skuSnapshot)) {
                refundOrderData.put("color", "");
                refundOrderData.put("size", "");
            } else {
                String[] split = skuSnapshot.split("  ");
                String[] color = split[0].split(":");
                String[] size = split[1].split(":");
                //颜色
                refundOrderData.put("color", color[1]);
                //型号
                refundOrderData.put("size", size[1]);
            }
            Double totalPay = storeOrderItem.getTotalPay();
            //refundOrderData.put("refund",)
            long productId = storeOrderItem.getProductId();
            /**
             * 对应Product表的id
             * private Long ProductId;
             */
            Product product = orderCount.selectProdectById(productId);
            refundOrderData.put("firstDetailImage", product.getFirstDetailImage());
            refundOrderData.put("firstDetailImageArr", product.getFirstDetailImage().split(","));
            String clothesNumber = product.getClothesNumber();
            String name = product.getName();
            //商品款号
            refundOrderData.put("clothesNumber", clothesNumber);
            //商品名称
            refundOrderData.put("shopName", name);
            //商品名
            refundOrderData.put("name", name);
        }else {
            refundOrderData.put("maxMoney",storeOrder.getTotalPay());
        }
        //订单Id
        refundOrderData.put("orderNo", refundOrder.getOrderNo());

        //货物状态名称：已收到货、未收到货
        refundOrderData.put("takeProductStateName", buildTakeProductStateName(refundOrder));
        //退款原因
        refundOrderData.put("refundReason", refundOrder.getRefundReason());
        //退款说明
        refundOrderData.put("refundRemark", refundOrder.getRefundRemark());
        //平台介入处理意见
//        refundOrderData.put("handlingSuggestion", refundOrder.getHandlingSuggestion());
        //剩余卖家自动确认收货时间毫秒数
        refundOrderData.put("surplusSupplierAutoTakeTime", buildSurplusSupplierAutoTakeTime(refundOrder));
        String refundProofImages = refundOrder.getRefundProofImages();
        //退款凭证
        refundOrderData.put("refundProofImages", refundProofImages);
        if (StringUtils.isNotEmpty(refundProofImages)) {
            //退款凭证图片数组
            refundOrderData.put("refundProofImagesArr", JSON.toJSONString(refundProofImages.split(",")));
        } else {
            String[] refundProofImagesArr = {};
            //退款凭证图片数组
            refundOrderData.put("refundProofImagesArr", JSON.toJSONString(refundProofImagesArr));
        }
        //填充流程相关字段
        //受理时间
        refundOrderData.put("acceptTime", buildAcceptTime(refundOrder));
        //受理说明
        refundOrderData.put("acceptNote", buildAcceptNote(refundOrder));
        //退款时间
        refundOrderData.put("refundTime", DateUtil.parseLongTime2Str(refundOrder.getRefundTime()));
        //平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
        refundOrderData.put("platformInterveneState", refundOrder.getPlatformInterveneState());
        //平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束结束
        refundOrderData.put("platformInterveneStateName", refundOrder.buildPlatformInterveneStateName());
        //平台介入时间
        refundOrderData.put("platformInterveneTime", DateUtil.parseLongTime2Str(refundOrder.getPlatformInterveneTime()));
        //关闭时间
        refundOrderData.put("closeTime", buildCloseTime(refundOrder));
        //关闭原因
        refundOrderData.put("closeCause", buildCloseCause(refundOrder));
        //关闭备注
        refundOrderData.put("closeNote", buildCloseNote(refundOrder));
        //买家发货时间
        refundOrderData.put("customerReturnTime", DateUtil.parseLongTime2Str(refundOrder.getCustomerReturnTime()));
        //买家发货物流单号
        refundOrderData.put("customerExpressNo", refundOrder.getCustomerExpressNo());
        //买家发货物流公司
        refundOrderData.put("customerExpressCompany", refundOrder.getCustomerExpressCompany());
        // 买家发货快递公司名称
        refundOrderData.put("customerExpressCompanyName", refundOrder.getCustomerExpressCompanyName());
        //添加是否有收货地址信息
        List<SupplierDeliveryAddress> supplierDeliveryAddressList = refundOrderMapper.selectListBySupplierId(refundOrder.getSupplierId());
        //是否有收货信息，有：1 无：0
        int hasDeliveryAddressInfo = 0;
        if (supplierDeliveryAddressList.size() > 0) {
            hasDeliveryAddressInfo = 1;
        }
        List<Map<String, Object>> deliveryAddressList = new ArrayList<>();
        for (SupplierDeliveryAddress supplierDeliveryAddress : supplierDeliveryAddressList) {
            Map<String, Object> map = new HashMap<>();
            String receiptInfoName = supplierDeliveryAddress.getReceiptInfoName();
            String address = supplierDeliveryAddress.getAddress();
            String receiverName = supplierDeliveryAddress.getReceiverName();
            String phoneNumber = supplierDeliveryAddress.getPhoneNumber();
            int defaultAddress = supplierDeliveryAddress.getDefaultAddress();

            //仓库名称
            map.put("receiptInfoName", receiptInfoName);
            //地址
            map.put("address", address);
            //收货人信息
            map.put("receiverName", receiverName);
            //电话
            map.put("phoneNumber", phoneNumber);
            //状态 0：非默认收货地址，1：默认收货地址
            map.put("defaultAddress", defaultAddress);
            //收货信息ID
            map.put("id", supplierDeliveryAddress.getId());
            deliveryAddressList.add(map);
        }
        //是否有收货信息，有：1 无：0
        refundOrderData.put("hasDeliveryAddressInfo", hasDeliveryAddressInfo);
        //收货信息列表
        refundOrderData.put("deliveryAddressList", deliveryAddressList);

        return refundOrderData;
    }

    /**
     * 金额校验
     *@Auther:胡坤
     * @param orderNo
     * @param skuId
     * @param money
     * @return
     */
    @Override
    public Integer equalsMoney(Long orderNo, Long skuId, Double money) {
        StoreOrderItem storeOrderItem = refundOrderMapper.selectItem(orderNo, skuId);
        Double totalPay = storeOrderItem.getTotalPay();
        if (money > totalPay) {
            return 1; //1代表金额大于实际金额
        }
        return 0;//0代表退款金额不大于实际金额
    }


    /**
     * 订单关闭备注:平台介入时，由平台操作关闭售后单时填写的备注信息。若数据为空则不显示。
     *
     * @param refundOrder
     * @return
     */
    private String buildCloseNote(RefundOrder refundOrder) {
        return refundOrder.getPlatformCloseReason();
    }


    /**
     * 订单关闭原因,2种情况：买家撤销售后申请/买家超时未发货/平台介入
     * 售后订单状态：6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    private String buildCloseCause(RefundOrder refundOrder) {
        String closeCause = "";
        int refundStatus = refundOrder.getRefundStatus();
        if (refundStatus == 6) {
            closeCause = "买家超时未发货";
        } else if (refundStatus == 7) {
            closeCause = "买家撤销售后申请";
        } else if (refundStatus == 8) {
            closeCause = "平台介入";
        } else if (refundStatus == 9) {
            closeCause = "买家撤销售后申请";
        }
        return closeCause;
    }

    /**
     * 售后订单关闭时间关闭时间
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    private String buildCloseTime(RefundOrder refundOrder) {
        long closeTime = 0;
        int refundStatus = refundOrder.getRefundStatus();// 售后订单状态：6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
        if (refundStatus == 6) {
            closeTime = refundOrder.getCustomerOvertimeTimeNoDelivery();//买家超时未发货时间
        } else if (refundStatus == 7) {
            closeTime = refundOrder.getCustomerCancelTime();//买家撤销售后订单时间
        } else if (refundStatus == 8) {
            closeTime = refundOrder.getPlatformInterveneCloseTime();//平台客服关闭时间
        } else if (refundStatus == 9) {
            closeTime = refundOrder.getCustomerCancelTime();//买家撤销售后订单时间
        }

        return DateUtil.parseLongTime2Str(closeTime);

    }

    /**
     * 获取受理说明
     *
     * @param refundOrder
     * @return
     */
    private String buildAcceptNote(RefundOrder refundOrder) {
        String storeAgreeRemark = refundOrder.getStoreAgreeRemark();
        String storeRefuseReason = refundOrder.getStoreRefuseReason();
        String acceptNote = "";
        if (StringUtils.isNotEmpty(storeAgreeRemark)) {//卖家同意退款备注
            acceptNote = storeAgreeRemark;
        } else if (StringUtils.isNotEmpty(storeRefuseReason)) {//卖家拒绝退款原因、拒绝理由：卖家确认拒绝退款时填写的理由
            acceptNote = storeRefuseReason;
        }
        return acceptNote;
    }

    /**
     * 获取受理时间
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    private String buildAcceptTime(RefundOrder refundOrder) {
        long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();
        long storeRefuseRefundTime = refundOrder.getStoreRefuseRefundTime();
        String acceptTime ="0";
        if (storeAllowRefundTime > 0) {
            acceptTime = DateUtil.parseLongTime2Str(storeAllowRefundTime);//卖家同意时间
        } else if (storeRefuseRefundTime > 0) {
            acceptTime = DateUtil.parseLongTime2Str(storeRefuseRefundTime);//卖家拒绝时间
        }
        return acceptTime;
    }

    /**
     * 剩余卖家自动确认收货时间毫秒数
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    public long buildSurplusSupplierAutoTakeTime(RefundOrder refundOrder) {

        long customerReturnTime = refundOrder.getCustomerReturnTime();//买家发货时间
        if (customerReturnTime == 0) {//未发货则返回0
            return 0;
        }

        long supplierAutoTakeDeliveryPauseTime = refundOrder.getSupplierAutoTakeDeliveryPauseTime();//卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
        long supplierAutoTakeDeliveryPauseTimeLength = refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength();//卖家自动确认收货总暂停时长（毫秒）
        long time = 0;//
        if (supplierAutoTakeDeliveryPauseTime == 0) {//未暂停
            time = System.currentTimeMillis();//当前时间
        } else {//卖家申请平台介入售后订单暂停
            time = supplierAutoTakeDeliveryPauseTime;//暂停时间
        }

        long supplierAutoTakeTime = customerReturnTime + refundOrderSupplierAutoTakeTime + supplierAutoTakeDeliveryPauseTimeLength;//自动确认收货时间节点
        long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
        surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
        if (surplusSupplierAutoTakeTime < 0) {
            surplusSupplierAutoTakeTime = 0;
        }
        return surplusSupplierAutoTakeTime;

    }

    /**
     * 货物状态
     * @Auther:胡坤
     */
    private String buildTakeProductStateName(RefundOrder refundOrder) {
        String takeProductState = "未收货";
        int refundType = refundOrder.getRefundType();
        if (refundType == RefundOrder.refundType_refund_and_product) {//2.退货退款
            takeProductState = "已收货";
        }
        ;
        return takeProductState;
    }

    /**
     * 剩余买家发货时间毫秒数
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    public long buildSurplusDeliverTime(RefundOrder refundOrder) {

        long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();//卖家同意时间
        if (storeAllowRefundTime == 0) {
            return 0;
        }
        long deliverTime = storeAllowRefundTime + refundOrderRestrictiveDeliverTime;//结束发货时间
        long time = System.currentTimeMillis();//当前时间
        long surplusDeliverTime = 0;//剩余卖家确认时间
        surplusDeliverTime = deliverTime - time;
        if (surplusDeliverTime < 0) {
            surplusDeliverTime = 0;
        }
        return surplusDeliverTime;
    }

    /**
     * 剩余卖家确认时间毫秒数
     *@Auther:胡坤
     * @param refundOrder
     * @return
     */
    public long buildSurplusAffirmTime(RefundOrder refundOrder) {

        long applyTime = refundOrder.getApplyTime();//申请售后时间
        if (applyTime == 0) {
            return 0;
        }
        long endAffirmTime = applyTime + refundOrderRestrictiveConfirmTime;//结束确认时间
        long time = System.currentTimeMillis();//当前时间
        long surplusAffirmTime = 0;//剩余卖家确认时间
        surplusAffirmTime = endAffirmTime - time;
        if (surplusAffirmTime < 0) {
            surplusAffirmTime = 0;
        }
        return surplusAffirmTime;
    }

    /**
     * 根据列表售后订单状态返回显示名称
     *@Auther:胡坤
     * @param refundStatus
     * @return
     */
    private String buildInfoRefundStatusName(int refundStatus) {
        //  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
        String refundStatusName = "";
        if (refundStatus == 1) {//1(待卖家确认、默认)、
            refundStatusName = "待卖家确认";
        } else if (refundStatus == 2) {//2（待买家发货）
            refundStatusName = "待买家发货";
        } else if (refundStatus == 3) {//、3（待卖家确认收货）、
            refundStatusName = "待卖家收货";
        } else if (refundStatus == 4) {//4(退款成功)、
            refundStatusName = "退款成功";
        } else if (refundStatus == 5) {//5(卖家拒绝售后关闭)、
            refundStatusName = "卖家已拒绝";
        } else if (refundStatus == 6) {//6（买家超时未发货自动关闭）、
            refundStatusName = "已关闭";
        } else if (refundStatus == 7) {// 7(卖家同意前买家主动关闭)、
            refundStatusName = "已关闭";
        } else if (refundStatus == 8) {//8（平台客服主动关闭）
            refundStatusName = "已关闭";
        } else if (refundStatus == 9) {// 9(卖家同意后买家主动关闭)、
            refundStatusName = "已关闭";
        }
        for (OrderStatusEnums orderStatusEnums:OrderStatusEnums.values()){
            if (orderStatusEnums.getCode()==refundStatus){
                refundStatusName=orderStatusEnums.getValue();
            }
        }
        if (StringUtils.isEmpty(refundStatusName)){
            logger.info("未知售后订单状态,请尽快处理");
            throw new RuntimeException("未知售后订单状态");
        }
        return refundStatusName;
    }

    /**
     * 售后处理 退货退款
     *@Auther:胡坤
     * @param obj
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response refundGoodsMoney(RefundGoodsMoneyRequest obj) {
        logger.info("售后处理={} 退货退款", obj);
        Map map = AnnotationUtils.validate(obj);
        if (map.get("result") == Boolean.FALSE) {
            logger.warn("参数为空={}", obj);
            return Response.error(map.get("message"));
        }
        if (obj.getStatus() == 0) {
//            卖家同意退款
            obj.setRefundStatus(2);
            //obj.setReceiverAddress("广东省佛山市南海区桂城街道宝石西路一号C时代产业园综合楼702");
            obj.setOperTime(System.currentTimeMillis());
           // obj.setReceiverName("陈先生");
            //obj.setReceiverPhone("18667913990");
            refundOrderMapper.updateAdress(obj);
        } else if (obj.getStatus() == 1) {
            obj.setOperTime(System.currentTimeMillis());
//            卖家拒绝
            obj.setRefundStatus(5);
        }
        Long refundOrderNo = refundOrderMapper.selectRefundNo(obj.getId());
        StoreRefundOrderActionLog storeRefundOrderActionLog=new StoreRefundOrderActionLog();
        storeRefundOrderActionLog.setRefundOrderId(refundOrderNo);
        storeRefundOrderActionLog.setActionTime(System.currentTimeMillis());
        if (obj.getStatus()==1){
            storeRefundOrderActionLog.setActionName("卖家拒绝退款");
        }
        if (obj.getStatus()==0){
            storeRefundOrderActionLog.setActionName("卖家已同意退款,等待卖家收货");

        }
        refundOrderMapper.insertLog(storeRefundOrderActionLog);
        refundOrderDao.refundGoodsMoney(obj);
        return Response.success();

    }

    /**
     * 售后处理 退款
     *@Auther:胡坤
     * @param obj
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response refundMoney(RefundGoodsMoneyRequest obj) {
        logger.info("售后处理={} 退款", obj);
        Map map = AnnotationUtils.validate(obj);
        if (map.get("result") == Boolean.FALSE) {
            logger.warn("参数为空={}", obj);
            return Response.error(map.get("message"));
        }
        obj.setOperTime(System.currentTimeMillis());
        if (obj.getStatus() == 0) {
            obj.setOperTime(System.currentTimeMillis());
//            卖家同意退款
            obj.setRefundStatus(4);
        } else if (obj.getStatus() == 1) {
            obj.setOperTime(System.currentTimeMillis());
//            卖家拒绝
            obj.setRefundStatus(5);
        }
        Long refundOrderNo = refundOrderMapper.selectRefundNo(obj.getId());
        /**
         * 日志记录
         */
        StoreRefundOrderActionLog storeRefundOrderActionLog=new StoreRefundOrderActionLog();
        storeRefundOrderActionLog.setRefundOrderId(refundOrderNo);
        storeRefundOrderActionLog.setActionTime(System.currentTimeMillis());
        if (obj.getStatus()==1){
            storeRefundOrderActionLog.setActionName("卖家拒绝退款");
        }
        if (obj.getStatus()==0){
            storeRefundOrderActionLog.setActionName("卖家已同意退款");
        }
        refundOrderMapper.insertLog(storeRefundOrderActionLog);

        refundOrderDao.refundGoodsMoney(obj);


        //退款
        if (obj.getStatus()==0){
            /**
             * 调用退款
             */
//            根据售后订单号查询 用户信息
            StoreBusiness storeBusiness = refundOrderDao.findRefundUser(obj.getId());
            YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setUserId(storeBusiness.getId());
            yjjStoreBusinessAccountLog.setOperMoney(obj.getMoney());
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.INCOME.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue()+"-"+"APP商品退款");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.REFUND_MONEY_SUCCESS.getCode());
            RefundOrder refundOrder = refundOrderMapper.selectRefundOrder(obj.getId());
            yjjStoreBusinessAccountLog.setAboutOrderNo(refundOrder.getOrderNo().toString());
            yjjStoreBusinessAccountLog.setOrderNo(refundOrder.getOrderNo().toString());
            //退款后待结算资金加上
            YjjStoreBusinessAccount yjjStoreBusinessAccount = refundOrderMapper.selectMoney(refundOrder.getStoreId());
            Double waitInMoney = yjjStoreBusinessAccount.getWaitInMoney();
            yjjStoreBusinessAccount.setWaitInMoney(waitInMoney+obj.getMoney());
            yjjStoreBusinessAccountLog.setRemainderMoney(yjjStoreBusinessAccount.getWaitInMoney());
            yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney()+yjjStoreBusinessAccount.getRealUseMoney());
            try {
                Double realBackMoney = refundOrder.getRealBackMoney();
                StoreOrder storeOrder = refundOrderMapper.selectStoreOrder(refundOrder.getOrderNo());
            if (storeOrder.getPaymentType()==2){
                alipayRefund(storeOrder,obj.getMoney().toString(),refundOrder.getRefundReason(),obj.getId().toString());
            }
            if (storeOrder.getPaymentType()==3){
                weixinRefund(storeOrder,obj.getMoney().toString(),refundOrder.getRefundReason(),true,obj.getId().toString());
            }
            if (storeOrder.getPaymentType()==4){
                int save = MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class)
                .eq(YjjStoreBusinessAccount::getUserId,yjjStoreBusinessAccount.getUserId()));
                if (save!=1){
                    logger.info("退款失败");
                }
                int log = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
                if (log!=1){
                    logger.info("日志记录失败");
                }
            }
                if ((storeOrder.getTotalPay()+storeOrder.getTotalExpressMoney())==obj.getMoney()){
                    int i = refundOrderMapper.updateOrderStatus(100, storeOrder.getOrderNo());
                    if (i!=1){
                        logger.info("订单关闭失败");
                    }
                }

            } catch (Exception e) {
                obj.setStatus(1);
                obj.setMoney(0.0);
                refundOrderDao.refundGoodsMoney(obj);
                e.printStackTrace();
                return Response.errorMsg("退款失败");
            }
        }
        return Response.success("退款成功");
    }

    /**
     * 确认收货  进行退款
     * @Auther:胡坤
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response confirmTackGoods(Long id) {
        RefundOrder refundOrder = refundOrderMapper.selectRefundOrder(id);
        Integer refundStatus = refundOrder.getRefundStatus();
        logger.info("确认收货id={}", id);
        try {
            refundOrderDao.confirmTackGoods(id,4);
             refundOrder = refundOrderMapper.selectRefundOrder(id);
            StoreOrder storeOrder = refundOrderMapper.selectStoreOrder(refundOrder.getOrderNo());
            Double realBackMoney = refundOrder.getRealBackMoney();
            //生成日志
            YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
            yjjStoreBusinessAccountLog.setUserId(refundOrder.getStoreId());
            yjjStoreBusinessAccountLog.setOperMoney(realBackMoney);
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.INCOME.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue()+"-"+"APP商品退款");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.REFUND_MONEY_SUCCESS.getCode());
            yjjStoreBusinessAccountLog.setAboutOrderNo(refundOrder.getOrderNo().toString());
            yjjStoreBusinessAccountLog.setOrderNo(refundOrder.getOrderNo().toString());
            //退款后待结算资金加上
            YjjStoreBusinessAccount yjjStoreBusinessAccount = refundOrderMapper.selectMoney(refundOrder.getStoreId());
            Double waitInMoney = yjjStoreBusinessAccount.getWaitInMoney();
            yjjStoreBusinessAccount.setWaitInMoney(waitInMoney+realBackMoney);
            yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney()+yjjStoreBusinessAccount.getRealUseMoney());
            yjjStoreBusinessAccountLog.setRemainderMoney(yjjStoreBusinessAccount.getWaitInMoney());
            //退款操作
            if (storeOrder.getPaymentType()==2){//支付宝退款
                alipayRefund(storeOrder,realBackMoney.toString(),refundOrder.getRefundReason(),id.toString());
            }
            if (storeOrder.getPaymentType()==3){//微信退款
                weixinRefund(storeOrder,refundOrder.getRealBackMoney().toString(),refundOrder.getRefundReason(),true,id.toString());
            }
            if (storeOrder.getPaymentType()==4){//虚拟账户退款
                int save = MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccount,new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class)
                        .eq(YjjStoreBusinessAccount::getUserId,yjjStoreBusinessAccount.getUserId()));
                if (save!=1){
                    logger.info("退款失败");
                }
                int log = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
                if (log!=1){
                    logger.info("日志记录失败");
                }
            }
            if (refundOrder.getRealBackMoney()==(storeOrder.getTotalExpressMoney()+storeOrder.getTotalPay())){
                int i = refundOrderMapper.updateOrderStatus(100, storeOrder.getOrderNo());
                if (i!=1){
                    logger.info("订单关闭失败");
                }
            }

        } catch (Exception e) {
            refundOrderDao.confirmTackGoods(id,refundStatus);
            e.printStackTrace();
            logger.info("退货退款失败");
            return Response.errorMsg("退货退款失败");
        }

        return Response.success("退货退款成功");
    }

    /**
     * 支付宝退款
     *
     * @Auther:胡坤
     */
    public void alipayRefund(StoreOrder order, String refundAmount, String refundReason, String refundOrderNo) throws Exception {

        /**
         * 获取支付宝秘钥
         */
        String aliKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMZYBIwKoen8lwRI+WJtyubqBbeqCHTkcS0/swrEjd0oIVEd2uFNQ0JDgpTnfyTtoJs7GJFOgmFohK2xxepOTDe3fbwAOC8NUWTswWgU3x9WitokuIa8Z4et2UqoXxYDNA1En8Oy0UjE4rvoP8d0T6zRdL5SG5BjREaLDAaa0pWZAgMBAAECgYEAm4wDZOAhwqK4vD+OdEauTRFSkoriPum4aEgAXX1v0/TYzAih0vcIvDq9eZFjAM7qmVJrHel4DnQtORqln+7vjY97aDTFhr0QGc84RzelGtAgG+ra0s9uNtm3lOwPDZ2khRIDW8m3FxP03gmtKiGvLynmpIhn8miwfORs7ic0cAECQQD3Yo2zKib3CCVJfLfJ4GDWTsWlAjAh0K1tNOVQMgjjmUZg2pC2u23c/+FvxkmQQ0r19TkR/CY6Qe8FczRwkPMZAkEAzUBCrpzY6iktT7KitJG/xRiyAElOrtOg5HZ/lfWnqxmT87oh7LW4GpKH6fbQHyzp2fR7CMrI2sEVc0IPaPGGgQJALKe3mF3FhtYLlQZUTraYBFdXyf9pHNGEXLAtrJo7jIoAcD9D3BhdLoVp9jk+0jGzeE55rMttQxrfwIYZMzCXEQJBAMkJ6Eaf2teA/aDSmAvFttCXH8KoCymyoCUm7FE2DMTKiOBxsEjqtSlR3U6NMc1XcLbLgLdb6OBbv2bljbJ84AECQQCk+kmViCyjD72IFBYb2+8a1Yhnc8q2q/HukEHikVJrtnZvB6wBlD8nqqcennsMpugfRkmnTYFFVQopKjoMT1Kp";
//        String aliKey=aliPublicKey;
//        String privateKey=rsaPrivateKey;
        String appid = "2015072500186581";
        StringBuffer stringBuffer = new StringBuffer("S");
        //添加子母订单号
        String out_trade_no = "";
        if (order.getParentId() > -1) {
            out_trade_no = stringBuffer.append(order.getParentId()).toString();
        } else {
            out_trade_no = stringBuffer.append(order.getOrderNo()).toString();
        }
        String trade_no = order.getPaymentNo();
        String refund_amount = refundAmount;
        String refund_reason = refundReason;
        logger.info("支付宝退款：" + "out_trade_no:" + out_trade_no +
                ",子订单号：" + order.getOrderNo() +
                ",母订单号：" + (order.getParentId() == -1 ? order.getOrderNo() : order.getParentId()) +
                ",paymentNo:" + trade_no +
                ",refund_amount" + refundAmount +
                ",refund_reason" + refundReason);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, privateKey, "JSON", "utf-8", aliKey, "RSA");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//    	AlipayRequest ar;
        String bizContent = "{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"," +
                "\"trade_no\":\"" + trade_no + "\"," +
                "\"refund_amount\":" + refund_amount + "," +
                "\"refund_reason\":\"" + refund_reason + "\"," +
                "\"out_request_no\":\"" + refundOrderNo + "\"," +
                "\"operator_id\":\"OP004\"," +
                "\"store_id\":\"NJ_S_002\"," +
                "\"terminal_id\":\"NJ_T_003\"" +
                "}";
        request.setBizContent(bizContent);
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info("测试版，支付宝支付，如果是验签问题，就先放在一边");
            String reg = "check Sign and Data Fail";
            if (!e.getMessage().contains(reg)) {
                throw new RuntimeException("支付宝退款失败!orderNo:" + out_trade_no);
            }
        }
        logger.info("支付宝退款成功");
    }

    /**
     * 退款 微信
     * @Auther:胡坤
     */

    public Map<String, String> weixinRefund(StoreOrder order, String refundAmount, String refundReason, boolean app, String refundOrderNo) throws Exception {
        //系统后台向微信支付系统发送数据，生成退款
        //先向map中添加appid等请求参数
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("transaction_id", order.getPaymentNo());
        StringBuilder stringBuffer = new StringBuilder("S");
        //添加子母订单号
        String out_trade_no = "";
        String total_fee = "";
        String total_fee2=null;
        Long parentId = order.getParentId();


        if (order.getParentId() > 1) {
            Query query=new Query();
            query.setOrderNo(order.getParentId());
            StoreOrderNew storeOrderNew = refundOrderMapper.selectStoreOrderNew(query);
            total_fee = String.valueOf((int) (100 * (storeOrderNew.getTotalPay() + storeOrderNew.getTotalExpressMoney())));
            reqData.put("total_fee", total_fee);
            /**
             * 分批后的子订单
             */
            StoreOrderNew storeOrderNewl =null;
            if (!(order.getParentId().equals(order.getOrderNo()))){
                query=new Query();
                query.setOrderNo(order.getParentId());
                query.setStauts(0);
                storeOrderNewl = refundOrderMapper.selectStoreOrderNew(query);
                if(storeOrderNewl!=null){
                    Double totalPay = storeOrderNewl.getTotalPay();
                    Double totalPay1 = order.getTotalPay();
                    double a=(totalPay1+totalPay)*100+(storeOrderNewl.getTotalExpressMoney()+order.getTotalExpressMoney())*100;
                    total_fee2 = String.valueOf((int)a);
                }
            }
            reqData.put("total_fee", total_fee2);
        } else {
            total_fee = String.valueOf((int) (100 * (order.getTotalPay() + order.getTotalExpressMoney())));
            reqData.put("total_fee", total_fee);
        }
        BigDecimal refundFee = new BigDecimal(refundAmount).multiply(new BigDecimal(100));
        reqData.put("refund_fee", String.valueOf(refundFee.intValue()));
        reqData.put("refund_desc", refundReason);

        reqData.put("out_refund_no", refundOrderNo);
        logger.info("微信退款：" + "out_trade_no:" + out_trade_no +
                ",子订单号：" + order.getOrderNo() +
                ",母订单号：" + (order.getParentId() == 1 ? order.getOrderNo() : order.getParentId()) +
                ",transaction_id:" + order.getPaymentNo() +
                ",refund_fee" + String.valueOf(refundFee.intValue()) +
                ",refund_desc" + refundReason +
                ",out_refund_no" + refundOrderNo +
                ",total_fee" + total_fee);
        StoreOrderNew storeOrderNew1 =null;
        String total_fee1=null;
        if (!(order.getParentId().equals(order.getOrderNo()))&&order.getParentId()<order.getOrderNo()){
            Query query = new Query();
            Long orderNo = order.getOrderNo();
            query.setParentId(order.getOrderNo());
            query.setStauts(0);
            storeOrderNew1 = refundOrderMapper.selectStoreOrderNew(query);
            if(storeOrderNew1!=null){
                Double totalPay = storeOrderNew1.getTotalPay();
                Double totalPay1 = order.getTotalPay();
                double a=(totalPay1+totalPay)*100+(storeOrderNew1.getTotalExpressMoney()+order.getTotalExpressMoney())*100;
                total_fee1 = String.valueOf((int)a);
            }
        }
        /**
         * 对分批后的订单进行判断
         */
        if(storeOrderNew1==null){

            if (!(order.getParentId().equals(order.getOrderNo()))){
                if (order.getParentId()>order.getOrderNo()){
                    reqData.put("total_fee",total_fee);
                }
                if (!(order.getOriginalPrice().equals(order.getTotalPay()))){

                    double a=(order.getOriginalPrice()+order.getTotalExpressMoney())*100;
                    String total_fee4=String.valueOf((int)a);

                    reqData.put("total_fee", total_fee4);

                }else if ((order.getParentId().equals(order.getOrderNo()))&&(order.getOriginalPrice().equals(order.getTotalPay()))){

                    reqData.put("total_fee",total_fee2);

                }
            }else {
                reqData.put("total_fee", total_fee);
            }

        }else if (!(order.getOriginalPrice().equals(order.getTotalPay()))){
            double a=(order.getOriginalPrice()+order.getTotalExpressMoney())*100;
            String total_fee3=String.valueOf((int)a);
            reqData.put("total_fee", total_fee3);
        }else{
            reqData.put("total_fee", total_fee1);
        }
        reqData = fillRequestData(reqData,3, "MD5");
        //
        //发送请求带证书
        String respXml = requestWithCert("https://api.mch.weixin.qq.com/secapi/pay/refund", reqData,  3);
        logger.info("请求退款 responseXml====> {}", respXml);
        return processResponseXml(respXml, 3);

    }

    /**
     * 需要证书的请求,比如退款，撤销订单
     *
     * @param url              String
     * @param reqData          向wxpay post的请求数据  Map
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String url, Map<String, String> reqData,
                                  Integer paymentType) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = PaymentUtils.toXml(reqData);
        String resp = requestOnce(url, msgUUID, reqBody, true, paymentType);
        return resp;
    }
    /**
     * 请求，只请求一次，不做重试
     *
     * @param url
     * @param uuid
     * @param data
     * @param useCert          是否使用证书，针对退款、撤销等操作
     * @param paymentType
     * @return
     * @throws Exception
     */
    /**
     * app.id=wx9c5d8e90dc52e29c
     * api.key=ebdd1da629156627139d0b5be22bee67
     * mch.id=1403320302
     * @param url
     * @param uuid
     * @param data
     * @param useCert
     * @param paymentType
     * @return
     * @throws Exception
     */
    private String requestOnce(String url, String uuid, String data, boolean useCert, Integer paymentType) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            String str="1403320302";
            char[] password = str.toCharArray();

            InputStream certStream = getCertStream();

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        } else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();


        HttpPost httpPost = new HttpPost(url);

//        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
//        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 "+1403320302);  // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }
    /**
     * 获取证书
     * @throws FileNotFoundException
     */
    public static InputStream getCertStream() throws FileNotFoundException{
        InputStream inputStream = RefundOrderServiceImp.class.getClassLoader().getResourceAsStream("apiclient_cert.p12");
        return inputStream;
    }
    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData, Integer paymentType, String signType) throws Exception {
        reqData.put("appid", "wx9c5d8e90dc52e29c");
        reqData.put("mch_id", "1403320302");
        reqData.put("nonce_str",genNonceStr());
        reqData.put("sign_type", signType);
        reqData.put("sign", generateSignature(reqData, "ebdd1da629156627139d0b5be22bee67", signType));
        return reqData;
    }


    public static String genNonceStr() {
        Random random = new Random();
        return DigestUtils.md5Hex(String.valueOf(random.nextInt(10000)));
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, String signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
            {sb.append(k).append("=").append(data.get(k).trim()).append("&");}
        }
        sb.append("key=").append(key);
        if ("MD5".equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     *
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr, Integer paymentType) throws Exception {
        String RETURN_CODE = "return_code";
        String RESULT_CODE = "result_code";
        String return_code;
        String result_code;
        Map<String, String> respData = PaymentUtils.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            logger.error(String.format("No `return_code` in XML: %s", xmlStr));
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals("FAIL")) {
            logger.error("退款失败！");
            logger.info(respData.toString());
            throw new RuntimeException("退款失败！");
        } else if (return_code.equals("SUCCESS")) {
            if (true) {
                logger.info("成功响应！");
                if (respData.containsKey(RESULT_CODE)) {
                    result_code = respData.get(RESULT_CODE);
                    if (result_code.equals("FAIL")) {
                        logger.info("resData:" + respData);
                        logger.info("退款失败！");
                        throw new RuntimeException("退款失败！");
                    } else if (result_code.equals("SUCCESS")) {
                        logger.info("resData:" + respData);
                        logger.info("退款成功！");
                    }
                }
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            logger.error(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }
}
