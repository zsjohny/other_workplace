package com.e_commerce.miscroservice.supplier.controller;

import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import com.e_commerce.miscroservice.supplier.service.IOrderNewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/supplier/allOrder")
public class AllOrderController {

    private Log logger = Log.getInstance(AllOrderController.class);


    @Autowired
    private IOrderNewService supplierOrderService;

    /**
     * 获取供应商订单详情
     *
     * @return
     */
    @RequestMapping("/getOrderDetail")
    public Response getOrderInfo(@RequestParam(value = "orderNo", defaultValue = "100003959") long orderNo, HttpServletRequest request) {
        //orderNo=100003933L;
   //Integer id = IdUtil.getId();
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            //获取该定下所有的子订单,然后通过订单信息获取到供应商的id
            //然后根据前端传过来的id与通过订单信息获取到供应商的id进行比对 相同的进行进行下一步操作 不相同的说明该商品不是该供应商提供的
            // TODO: 2018/11/13

            // xml还没写
//            List<StoreOrder> storeOrderList = supplierOrderService.selectOrderSonAll(orderNo);
//            for (StoreOrder storeOrder : storeOrderList) {
//
//                Long supplierId = supplierOrderService.getSupplierId(userId);
//
//                if ( storeOrder.getSupplierId()-supplierId==0){
//                    Long orderNoSon = storeOrder.getOrderNo();
//                }
//            }

            /**
             * 添加售后信息,根据订单号进行查询
             */

            List<StoreRefundOrderActionLog> storeRefundOrderActionLogs = supplierOrderService.selectRefundLog(orderNo);
            if (storeRefundOrderActionLogs.size()!=0){
                ArrayList list=new ArrayList();
                for (StoreRefundOrderActionLog storeRefundOrderActionLog : storeRefundOrderActionLogs) {
                    Map<String,Object> map=new HashMap<>();
                    map.put("time",storeRefundOrderActionLog.getActionTime());
                    map.put("name",storeRefundOrderActionLog.getActionName());
                    list.add(map);
                }
                data.put("userRefundLogList",list);
            }


            //1、订单信息
            StoreOrder storeOrder = supplierOrderService.selectInfo(orderNo);
            data.put("orderNo", storeOrder.getOrderNo()+"");//订单编号
            data.put("totalBuyCount", storeOrder.getTotalBuyCount());//商品件数
            data.put("orderStatus", storeOrder.getOrderStatus());//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
            data.put("orderStatusName", getOrderStatusName(storeOrder.getOrderStatus(), storeOrder.getOrderCloseType()));//订单状态名称
            //改价字段
            double supplierPreferential = storeOrder.getSupplierPreferential();//商家店铺优惠
            double supplierChangePrice = storeOrder.getSupplierChangePrice();//商家店铺改价
            double supplierAddPrice = 0;
            if (supplierChangePrice >= 0) {
                supplierPreferential += supplierChangePrice;
            } else {
                supplierAddPrice = 0 - supplierChangePrice;
            }
            data.put("totalMoney", storeOrder.getTotalMoney());//订单原价
            data.put("platformPreferential", storeOrder.getPlatformTotalPreferential());//平台优惠金额
            data.put("supplierPreferential", supplierPreferential);//商家优惠金额
            data.put("supplierAddPrice", supplierAddPrice);//订单加价金额
            data.put("totalPay", storeOrder.getTotalPay());//订单改价后待付款金额
            data.put("originalPrice", storeOrder.getOriginalPrice());//订单改价前原始待付款金额
            /**
             * 新添字段
             */
            Double totalRefundCost = storeOrder.getTotalRefundCost();
            if (totalRefundCost==null&&totalRefundCost==0){
                data.put("totalRefundCost",0.00);
            }else {
                data.put("totalRefundCost", storeOrder.getTotalRefundCost()); //实际退款金额
            }
            data.put("totalExpressMoney", storeOrder.getTotalExpressMoney());
            //判断下单人是不是该供应商的客户
            Long supplierId = storeOrder.getSupplierId();
            Long storeId = storeOrder.getStoreId();
            String phoneNumber = supplierOrderService.selectById(storeId);
            SupplierCustomer supplierCustomer = supplierOrderService.getCustomerByStoreIdOrPhoneNumber(phoneNumber, storeId, supplierId);
            if (supplierCustomer == null) {
                data.put("customerId", 0);
                data.put("isCustomer", 0);
            } else {
                data.put("customerId", supplierCustomer.getId());
                data.put("isCustomer", 1);
            }
            data.put("createTime", DateUtil.parseLongTime2Str(storeOrder.getCreateTime()));//下单时间
            data.put("payTime", DateUtil.parseLongTime2Str(storeOrder.getPayTime()));//付款时间
            data.put("sendTime", DateUtil.parseLongTime2Str(storeOrder.getSendTime()));//发货时间
            data.put("finishTime", DateUtil.parseLongTime2Str(storeOrder.getConfirmSignedTime()));//完成时间   买家确认收货提交成功时间
            data.put("closeTime", DateUtil.parseLongTime2Str(storeOrder.buildOrderCloseTime(storeOrder)));//订单关闭时间
            data.put("haveRefund", getHaveRefund(storeOrder.getOrderNo()));//是否有售后：0没有售后、1有售后
            String businessName = supplierOrderService.selectBusinessNameById(storeId);
            data.put("storeId", storeId);//门店ID
            data.put("businessName", businessName);//客户名称、商家名称
            //2、收货人信息
            String expressInfo = storeOrder.getExpressInfo();
            if (StringUtils.isEmpty(expressInfo)) {
                throw new RuntimeException("收件人信息为空");
            }
            String[] expressInfoArr = expressInfo.split(",");
            if (expressInfoArr.length != 3) {
                throw new RuntimeException("收件人信息错误，请排查问题。expressInfo：" + expressInfo);
            }
            data.put("receiverName", expressInfoArr[0]);//收货人姓名
            data.put("receiverPhone", expressInfoArr[1]);//收货人手机号
            data.put("receiverAddress", expressInfoArr[2]);//收货人地址
            //3、发货信息
            StoreExpressInfo entity = new StoreExpressInfo();
            entity.setOrderNo(orderNo);
            entity.setStatus(0);
            StoreExpressInfo storeExpressInfo = supplierOrderService.selectOne(entity);
            if (storeExpressInfo != null) {
                String EngName = storeExpressInfo.getExpressSupplier();
                /**
                 * EngName快递提供商
                 * store_ExpressInfo  sql表
                 */
                List<ExpressSupplier> list = supplierOrderService.selectList(EngName);
                if (list == null || list.size() == 0) {
                    data.put("expressNo", storeExpressInfo.getExpressOrderNo());//物流单号
                    data.put("expressCompamyName", storeExpressInfo.getExpressSupplier());//物流公司
                    data.put("expressCnName", "");//物流公司
                } else {
                    data.put("expressNo", storeExpressInfo.getExpressOrderNo());//物流单号
                    data.put("expressCompamyName", storeExpressInfo.getExpressSupplier());//物流公司、快递提供商
                    data.put("expressCnName", list.get(0).getCnName());//物流公司、快递供应商中文名
                }
            } else {
                data.put("expressNo", "");//物流单号
                data.put("expressCompamyName", "");//物流公司
                data.put("expressCnName", "");//物流公司
            }
            data.put("remark", storeOrder.getRemark());//发货说明
            data.put("orderSupplierRemark", storeOrder.getOrderSupplierRemark());//订单供应商备注


            //4、订单商品信息
            List<Map<String, Object>> storeOrderItemList = supplierOrderService.getSupplierOrderItemByOrderNo(orderNo);
            data.put("storeOrderItemList", storeOrderItemList);

            //5、获取邮寄公司名称列表,用于回显数据
            List<Map<String, Object>> allExpressCompanys = supplierOrderService.getAllExpressCompanyNames();
            data.put("allExpressCompanys", allExpressCompanys);

            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("获取商品信息失败,请尽快排查问题");
        }
    }

    /**
     * 分批发货按钮
     */
    @RequestMapping("/order/son")
    public Response order(@RequestParam(value = "orderNo", defaultValue = "100003959") long orderNo, HttpServletRequest request) {
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> storeOrderItemList = supplierOrderService.getSupplierOrderItemByOrderNo(orderNo);
            data.put("storeOrderItemList", storeOrderItemList);
            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("获取商品失败");
        }
    }

    /**
     * 是否有售后：0没有售后、1有售后
     *
     * @param orderNo
     * @return
     */
    private int getHaveRefund(long orderNo) {
        Integer count = supplierOrderService.getHaveRefund(orderNo);
        return count;
    }

    /**
     * 根据int状态值获取供应商订单列表对应的中文状态值
     *
     * @param orderStatus    订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
     * @param orderCloseType 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
     * @return
     */
    private String getOrderStatusName(int orderStatus, int orderCloseType) {
        String orderStatusName = "";
        switch (orderStatus) {
            case 0:
                orderStatusName = "待付款（未付款，请勿发货）";
                break;
            case 10:
                orderStatusName = "待发货";
                break;
            case 50:
                orderStatusName = "已发货";
                break;
            case 70:
                orderStatusName = "交易完成";
                break;
            case 100:
//			待实现根据三种情况分别显示详细关闭交易原因，关闭的3种情况是：1、买家取消待付款的订单，2、平台取消已付款的订单，3、通过申请售后退货退款全部商品的订单 4、买家未付款超时
                orderStatusName = "交易关闭";//交易关闭（买家未付款超时/买家取消未付款/平台取消已付款/售后退单）
                if (orderCloseType == 101) {//101买家主动取消订单、
                    orderStatusName = orderStatusName + "（买家取消未付款）";
                } else if (orderCloseType == 102) {//102超时未付款系统自动关闭订单、
                    orderStatusName = orderStatusName + "（买家未付款超时）";
                } else if (orderCloseType == 103) {//103全部退款关闭订单、
                    orderStatusName = orderStatusName + "（售后退单）";
                } else if (orderCloseType == 104) {//104卖家关闭订单
                    orderStatusName = orderStatusName + "";
                } else if (orderCloseType == 105) {//105、平台客服关闭订单
                    orderStatusName = orderStatusName + "（平台取消已付款）";
                }
                break;
        }
        return orderStatusName;
    }


    /**
     * 获取供应商订单数量
     *
     * @return
     */
    @RequestMapping("/getSupplierOrderCount")
    public Response getSupplierOrderCount(HttpServletRequest request) {
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            //获取订单总数，今日新增个数，待处理个数
            Map<String, Object> supplierOrderCount = supplierOrderService.getSupplierOrderCount(userId);
            data.put("supplierOrderCount", supplierOrderCount);
            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取供应商订单数量:" + e.getMessage());
        }
    }

    /**
     * 暂时写死userId
     */
    interface UserHelper {
        static long userId(HttpServletRequest request) {
            return 161L;
        }

        ;

    }

    /**
     * 获取供应商订单列表
     *
     * @return
     */
// TODO: 2018/11/9  
    @RequestMapping("/getSupplierOrderList")
    public Object getSupplierOrderList(@RequestParam(value = "orderNo", required = false) long orderNo,
                                       @RequestParam(value = "orderStatus", required = false, defaultValue = "-1") int orderStatus,
                                       @RequestParam(value = "addresseeName", required = false, defaultValue = "") String addresseeName,
                                       @RequestParam(value = "addresseeTelePhone", required = false, defaultValue = "") String addresseeTelePhone,
                                       @RequestParam(value = "clothesNumbers", required = false, defaultValue = "") String clothesNumbers,
                                       @RequestParam(value = "updateTimeBegin", required = false, defaultValue = "") String updateTimeBegin,
                                       @RequestParam(value = "updateTimeEnd", required = false, defaultValue = "") String updateTimeEnd,
                                       @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
                                       @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
                                       @RequestParam(value = "customerPhone", required = false, defaultValue = "") String customerPhone,
                                       @RequestParam(value = "expressNo", required = false, defaultValue = "") String expressNo,
                                       @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,
                                       @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd,
                                       @RequestParam(value = "refundUnderway", required = false, defaultValue = "-1") int refundUnderway,

                                       HttpServletRequest request) {

        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        //PageHelper.startPage(request.getPageNumber(),request.getPageSize());
//        List<CustomerPoolResponse> list = customerPoolDao.findAllCustomerPool(request);
//        PageInfo<CustomerPoolResponse> poolResponsePageInfo = new PageInfo<CustomerPoolResponse>(list);
//        return Response.success(poolResponsePageInfo);
        try {
            //获取所有订单
            List<Map<String, Object>> selectList = supplierOrderService.getSupplierOrderList(userId, orderNo, orderStatus, addresseeName, addresseeTelePhone, clothesNumbers,
                    updateTimeBegin, updateTimeEnd, remark, customerName, customerPhone, expressNo, createTimeBegin, createTimeEnd, refundUnderway);

            return Response.success(selectList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取供应商列表:" + e.getMessage());
        }
    }

    /**
     * 根据订单编号获取该订单售后详情
     *
     * @return
     */
    @RequestMapping("/refund")
    public Response getRefundOrder(@RequestParam(value = "orderNo", defaultValue = "100003933") long orderNo, HttpServletRequest request) {
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        List<Map<String, Object>> refundItenList = supplierOrderService.getRefundItenList(orderNo);

        return Response.success(refundItenList);
    }

    /**
     * 立即发货
     * orderNo 订单号
     * request
     * expressCompamyName 快递公司名字
     * expressNo 快递订单号
     * remark 快递备注信息
     * type 是否全部发货
     *
     * @return
     */
    @RequestMapping("/send/stroe")
    public Response sendStore(HttpServletRequest request,
                              @RequestParam Long orderNo,
                              @RequestParam String storeSendList,
                              @RequestParam String expressInfo,
                              @RequestParam String expressNo,
                              @RequestParam String remark,
                              @RequestParam Integer type) {
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        try {
            Response response = supplierOrderService.sendStore(orderNo, expressInfo,expressNo, storeSendList, remark, type);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg("发货异常,请尽快排查问题");
        }
    }

    /**
     * 获取供应商待发货订单列表
     * @return
     */
    @RequestMapping("/getSupplierOrderListPendingDelivery")
    public Response getSupplierOrderListPendingDelivery(@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
      @RequestParam(value="addresseeName", required=false, defaultValue = "") String addresseeName,
      @RequestParam(value="addresseeTelePhone", required=false, defaultValue = "") String addresseeTelePhone,
      @RequestParam(value="clothesNumbers", required=false, defaultValue = "") String clothesNumbers,
      @RequestParam(value="payTimeBegin", required=false, defaultValue = "") String payTimeBegin,
      @RequestParam(value="payTimeEnd", required=false, defaultValue = "") String payTimeEnd,
      @RequestParam(value="remark", required=false, defaultValue = "") String remark,
      @RequestParam(value="customerName", required=false, defaultValue = "") String customerName,
      @RequestParam(value="customerPhone", required=false, defaultValue = "") String customerPhone,
      @RequestParam(value="expressNo", required=false, defaultValue = "") String expressNo,
                                                        HttpServletRequest request) {
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }

        try {


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取供应商列表:"+e.getMessage());
            throw new RuntimeException("获取供应商列表:"+e.getMessage());
        }
        return Response.success();
    }


    /**
     * 供应商修改订单价格
     */
    @RequestMapping("/changeOrderPrice")
    public Response changeOrderPrice(@RequestParam(value = "orderNo") long orderNo ,
                                     @RequestParam(value = "changePrice") double changePrice,
                                     HttpServletRequest request){
        long userId = UserHelper.userId(request);
        if (userId !=161L) {
            return Response.errorMsg("请先登录！");
        }
        try {
            supplierOrderService.changeOrderPrice(orderNo, userId);
            return Response.success();
        }catch(Exception e){
            logger.info(e.getMessage());
            return Response.errorMsg("恢复价格失败");
        }
    }
}
