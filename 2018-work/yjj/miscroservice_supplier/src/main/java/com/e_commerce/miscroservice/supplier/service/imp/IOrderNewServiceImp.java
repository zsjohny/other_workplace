package com.e_commerce.miscroservice.supplier.service.imp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.order.*;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.entity.request.StoreRefundOrderActionLog;
import com.e_commerce.miscroservice.supplier.service.IOrderNewService;
import com.e_commerce.miscroservice.supplier.mapper.OrderCountMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IOrderNewServiceImp implements IOrderNewService {
    private Log logger = Log.getInstance(IOrderNewServiceImp.class);

    @Autowired
    private OrderCountMapper orderCount;
    @Override
    public Map<String, Object> getSupplierOrderCount(long userId) {
        Map<String,Object> supplierOrderCount = new HashMap<String,Object>();
        //获取订单总数
        supplierOrderCount.put("allCount", getSupplierOrderCountAllCount(userId));
        //获取今日新增个数
        supplierOrderCount.put("todayNewCount", getSupplierOrderCountTodayNewCount(userId));

        //获取待处理个数
        supplierOrderCount.put("unDealWithCount", getSupplierOrderCountUnDealWithCount(userId));

        //获取售后中订单个数
        supplierOrderCount.put("refundOrderUnDealCount", getSupplierOrderCountUnDealRefundOrderCount(userId));

        return supplierOrderCount;
    }


    //获取订单总数
    private Integer getSupplierOrderCountAllCount(Long userId) {
        Integer count = orderCount.selectCount(userId);
        return count;
    }
    //获取今日新增个数
    private Integer getSupplierOrderCountTodayNewCount(long userId) {
        long todayZeroTimeInMillis = getTodayZeroTimeInMillis();
        TodayCount todayCount=new TodayCount();
        todayCount.setSupplierId(userId);
        todayCount.setTodayZeroTimeInMillis(todayZeroTimeInMillis);
        Integer count = orderCount.todayCount(todayCount);
        return count;
    }
    //获取今日零时毫秒数
    private long getTodayZeroTimeInMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    //获取待处理个数
    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IOrderNewService#getSupplierOrderCountUnDealWithCount(long)
     */
    @Override
    public Integer getSupplierOrderCountUnDealWithCount(long userId) {
        /**
         * @Override
         * 	public Object getSupplierOrderCountUnDealWithCount(long userId) {
         * 		Wrapper<StoreOrderNew> wrapperAllCount =
         * 				new EntityWrapper<StoreOrderNew>().eq("status", 0).
         * 				eq("supplierId",userId).eq("OrderStatus", OrderStatus.PAID.getIntValue()).gt("ParentId", 0);
         * 		return supplierOrderMapper.selectCount(wrapperAllCount);
         * 	}
         * 	10, "已付款"
         */
        Integer count = orderCount.unDealWithCount(userId);

        return count;
    }

    /**
     * 根据订单号查询商品的详情信息
     * @param orderNo
     * @return
     */
    @Override
    public StoreOrder selectInfo(Long orderNo) {
        StoreOrder storeOrder = orderCount.selectInfo(orderNo);
        return storeOrder;
    }

    /**
     * 根据用户id查询号码
     * @param storeId
     * @return
     */
    @Override
    public String selectById(Long storeId) {
        String phoneNumber = orderCount.selectById(storeId);
        return phoneNumber;
    }

    /**
     * 根据phoneNumber,storeId,supplierId查询信息
     * @param phoneNumber
     * @param storeId
     * @param supplierId
     * @return
     */
    @Override
    public SupplierCustomer getCustomerByStoreIdOrPhoneNumber(String phoneNumber, Long storeId, Long supplierId) {
        List<SupplierCustomer> customers = orderCount.getCustomerByStoreIdOrPhoneNumber(phoneNumber,storeId,supplierId);
        return customers.size() > 0 ? customers.get(0) : null;
    }

    /**
     * 是否有售后：0没有售后、1有售后
     * @param orderNo
     * @return
     */
    @Override
    public Integer getHaveRefund(long orderNo) {
        Integer count = orderCount.getHaveRefund(orderNo);

        return count;
    }

    /**
     * 根据用户id查询商家信息
     * @param orderNo
     * @return
     */
    @Override
    public String selectBusinessNameById(Long orderNo) {
        String businessName = orderCount.selectBusinessNameById(orderNo);
        return businessName;
    }

    /**
     * 根据订单查询物流信息
     * @param expressInfo
     * @return
     */
    @Override
    public StoreExpressInfo selectOne(StoreExpressInfo expressInfo) {
        Long orderNo = expressInfo.getOrderNo();
        StoreExpressInfo storeExpressInfo = orderCount.selectOne(orderNo);
        return storeExpressInfo;
    }

    /**
     * 根据快递名查询快递商信息
     * @param EngName
     * @return
     */
    @Override
    public List<ExpressSupplier> selectList(String EngName) {
        List<ExpressSupplier> list = orderCount.selectList(EngName);
        return list;

    }

    /**
     * 查询订单列表
     * @param orderNo
     * @return
     */
    @Override
    public List<Map<String, Object>> getSupplierOrderItemByOrderNo(long orderNo) {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        StoreOrder storeOrder = orderCount.selectByOrderNo(orderNo);
        Integer refundUnderway = storeOrder.getRefundUnderway();
        //Double totalRefundCost = storeOrder.getTotalRefundCost();
        List<StoreOrderItem> itemList = orderCount.selectItemList(orderNo);
        for (StoreOrderItem storeOrderItem : itemList) {
            Map<String,Object> item = new HashMap<String,Object>();
            //订单的是否有售后0(否),1(是)
            if (refundUnderway==0){
                item.put("refundUnderwaySon",0);
                item.put("totalRefundCostSon",0.00);
            }else {
                //RefundOrder refundOrder = orderCount.selectRefundOrder(orderNo);
                /**
                 * 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、
                 * 5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、
                 * 8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
                 */
               // Long id = refundOrder.getId();
                //item.put("refundId",id);
                //item.put("refundUnderwaySon",1);
                //Integer refundStatus = refundOrder.getRefundStatus();
                //if (4==refundStatus){
                    //Double refundCost = refundOrder.getRefundCost();
                    //item.put("totalRefundCostSon",refundCost);
                //}else {
                  //  item.put("totalRefundCostSon",0.00);
               // }
            }



            Long skuId = storeOrderItem.getSkuId();

            item.put("skuId", storeOrderItem.getSkuId());
            item.put("buyCount", storeOrderItem.getBuyCount());
            long productId = storeOrderItem.getProductId();
            /**
             * 对应Product表的id
             * private Long ProductId;
             */
            Product product = orderCount.selectProdectById(productId);
            item.put("firstDetailImage", product.getFirstDetailImage());
            item.put("firstDetailImageArr", product.getFirstDetailImage().split(","));

            item.put("name", product.getName());
            item.put("clothesNumber", product.getClothesNumber());



            item.put("buyCount", storeOrderItem.getBuyCount());
            item.put("money", storeOrderItem.getMoney());
            item.put("totalMoney", storeOrderItem.getTotalMoney());


            String skuSnapshot = storeOrderItem.getSkuSnapshot();
            if(StringUtils.isEmpty(skuSnapshot)){
                item.put("color", "");
                item.put("size", "");
            }else{
                String[] split = skuSnapshot.split("  ");
                String[] color = split[0].split(":");
                String[] size = split[1].split(":");
                item.put("color", color[1]);
                item.put("size", size[1]);
            }
            result.add(item);
        }
        return result;
    }

    /**
     * 获取邮递公司名称列表用于数据回显示
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllExpressCompanyNames() {
        List<ExpressSupplier> expressSupplierList = orderCount.getAllExpressCompanyNames();
        List<Map<String,Object>> expressCompanyList = new ArrayList<Map<String,Object>>();
        for (ExpressSupplier expressSupplier : expressSupplierList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("CnName", expressSupplier.getCnName());
            map.put("EngName", expressSupplier.getEngName());
            map.put("Id", expressSupplier.getId());
            expressCompanyList.add(map);
        }
        return expressCompanyList;
    }

    /**
     * 获取所有订单
     * @param userId
     * @param orderNo
     * @param orderStatus
     * @param addresseeName
     * @param addresseeTelePhone
     * @param clothesNumbers
     * @param updateTimeBegin
     * @param updateTimeEnd
     * @param remark
     * @param customerName
     * @param customerPhone
     * @param expressNo
     * @param createTimeBegin
     * @param createTimeEnd
     * @param refundUnderway
     * @return
     */
    @Override
    public List<Map<String, Object>> getSupplierOrderList(long userId, long orderNo, int orderStatus,
                                                          String addresseeName, String addresseeTelePhone,
                                                          String clothesNumbers, String updateTimeBegin,
                                                          String updateTimeEnd, String remark, String customerName,
                                                          String customerPhone, String expressNo, String createTimeBegin,
                                                          String createTimeEnd, int refundUnderway) {

        return null;
    }

    /**
     * 根据订单号查询售后信息
     * @param orderNo
     * @return
     */
    @Override
    public List<Map<String, Object>> getRefundItenList(Long orderNo) {
        List<Map<String,Object>> expressCompanyList = new ArrayList<Map<String,Object>>();
        List<RefundOrder> refundItemList = orderCount.getRefundItemList(orderNo);
        Map<String, Object> map = new HashMap<String, Object>();
        for (RefundOrder refundOrder : refundItemList) {
            /**
             * 售后订单编号
             */

            map.put("refundOrderNo",refundOrder.getRefundOrderNo());
            /**
             * 订单ID
             */

            map.put("orderNo",refundOrder.getOrderNo());

            /**
             * 商家手机号
             */

            map.put("storePhone",refundOrder.getStorePhone());
            /**
             * 商家名称
             */

            map.put("storeName",refundOrder.getStoreName());

            /**
             * 商家号ID
             */

            map.put("storeId",refundOrder.getStoreId());
            /**
             * 供应商ID
             */

            map.put("supplierId",refundOrder.getSupplierId());
            /**
             * 品牌ID
             */

            map.put("brandId",refundOrder.getBrandId());
            /**
             * 品牌名称
             */

            map.put("brandName",refundOrder.getBrandName());
            /**
             * 退款类型：1.仅退款  2.退货退款
             */

            map.put("refundType",refundOrder.getRefundType());
            /**
             * 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
             *              6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
             */

            map.put("refundStatus",refundOrder.getRefundStatus());
            /**
             * 退款申请金额
             */

            map.put("refundCost",refundOrder.getRefundCost());
            /**
             * 退货数量
             */

            map.put("returnCount",refundOrder.getReturnCount());
            /**
             * 退款原因ID
             */

            map.put("refundReasonId",refundOrder.getRefundReasonId());
            /**
             * 退款原因
             */

            map.put("refundReason",refundOrder.getRefundReason());
            /**
             * 退款说明
             */

            map.put("refundRemark",refundOrder.getRefundRemark());
            /**
             * 退款证明图片，英文逗号分隔的字符串
             */

            map.put("refundProofImages",refundOrder.getRefundProofImages());

            /**
             * 买家发货快递单号
             */

            map.put("customerExpressNo",refundOrder.getCustomerExpressNo());
            /**
             * 买家发货快递公司
             */

            map.put("customerExpressCompany",refundOrder.getCustomerExpressCompany());
            /**
             * 买家发货快递公司名称
             */

            map.put("customerExpressCompanyName",refundOrder.getCustomerExpressCompanyName());

            /**
             * 退款通道：0(无)、1(支付宝)、2(微信)
             */

            map.put("refundWay",refundOrder.getRefundWay());

            /**
             * 申请售后时间
             */

            map.put("applyTime",refundOrder.getApplyTime());

            /**
             * 买家发货时间
             */

            map.put("customerReturnTime",refundOrder.getCustomerReturnTime());
            /**
             * 卖家确认收货时间
             */

            map.put("confirmTime",refundOrder.getConfirmTime());

            /**
             * 退款时间
             */

            map.put("refundTime",refundOrder.getRefundTime());

            /**
             * 卖家同意时间
             */

            map.put("storeAllowRefundTime",refundOrder.getStoreAllowRefundTime());
            /**
             * 卖家拒绝时间
             */

            map.put("storeRefuseRefundTime",refundOrder.getStoreAllowRefundTime());

            /**
             * 卖家同意退款备注
             */

            map.put("storeAgreeRemark",refundOrder.getStoreAgreeRemark());

            /**
             * 卖家拒绝退款原因、拒绝理由：卖家确认拒绝退款时填写的理由
             */

            map.put("storeRefuseReason",refundOrder.getStoreRefuseReason());

            /**
             * 平台关闭理由
             */

            map.put("platformCloseReason",refundOrder.getPlatformCloseReason());

            /**
             * 平台介入处理意见
             */

            map.put("handlingSuggestion",refundOrder.getHandlingSuggestion());

            /**
             * 供应商收货人姓名
             */

            map.put("receiver",refundOrder.getReceiver());

            /**
             * 供应商收货地址
             */

            map.put("supplierReceiveAddress",refundOrder.getSupplierReceiveAddress());

            /**
             * 收货人电话
             */

            map.put("receiverPhone",refundOrder.getReceiverPhone());

            /**
             * 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
             */
            map.put("platformInterveneState",refundOrder.getPlatformInterveneState());

            /**
             * 平台介入时间
             */
            map.put("platformInterveneTime",refundOrder.getPlatformInterveneTime());

            /**
             * 平台客服关闭时间
             */
            map.put("platformInterveneCloseTime",refundOrder.getPlatformInterveneCloseTime());

            /**
             * 买家撤销售后订单时间
             */
            map.put("customerCancelTime",refundOrder.getCustomerCancelTime());
            /**
             * 买家超时未发货时间
             */
            map.put("customerOvertimeTimeNoDelivery",refundOrder.getCustomerOvertimeTimeNoDelivery());
            /**
             * 物流信息内容
             */
            map.put("expressInfo",refundOrder.getExpressInfo());

            /**
             * 卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
             */
            map.put("supplierAutoTakeDeliveryPauseTime",refundOrder.getSupplierAutoTakeDeliveryPauseTime());
            /**
             * 卖家自动确认收货总暂停时长（毫秒）
             */
            map.put("supplierAutoTakeDeliveryPauseTimeLength",refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength());
            expressCompanyList.add(map);
        }
        return expressCompanyList;
    }

    /**
     * 立即发货
     * @param orderNo
     * @return
     */
    @Override
   public Response sendStore(Long orderNo, String expressInfo,String expressNo, String storeSendList, String remark,Integer type) {

        StoreOrder storeOrder2 = orderCount.selectOrderStatus(orderNo);
        StoreOrder storeOrder = null;
        Integer orderStatus = null;
        try {
            /**
             * 判断改订单号所对应的订单是否是待发货(待发货才可以发货)
             */
            storeOrder = orderCount.selectOrderStatus(orderNo);
            orderStatus = storeOrder.getOrderStatus();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发货失败,该订单不在待发货状态" + e.getMessage());
        }
        //订单是否是待发货(已付款)
        if (orderStatus != 10 && orderStatus == null) {
            return Response.errorMsg("发货失败,订单不满足发货条件");
        }
        /**
         * 判断该订单号所对应的订单是否在售后中
         */
//        StoreOrder storeOrder1 = orderCount.selectByOrderNo(orderNo);
        Integer refundUnderway = storeOrder.getRefundUnderway();
        //是否是售后进行中：0(否)、1(是)'
        if (refundUnderway != 0) {
            return Response.errorMsg("发货失败,该订单正在售后中");
        }

        // TODO: 2018/11/8
        /**
         * 查看当前发货是否是全部发货
         * 暂时定一个状态为0 和 1
         * 0是全部发货,1是分批发货
         * 参数暂定为type
         */
        Boolean flag=false;
        Double originalPrice=0.00;
        StoreOrder storeOrderf = orderCount.selectByOrderNo(orderNo);
        if (storeOrderf.getParentId()>storeOrderf.getOrderNo()){
            flag=storeOrderf.getParentId()>storeOrderf.getOrderNo();
            Long parentIdO = storeOrderf.getParentId();
            StoreOrder storeOrderi = orderCount.selectParentId(parentIdO);
            storeOrderf.setOrderNoAttachmentStr( storeOrderi.getOrderNoAttachmentStr());
            originalPrice=storeOrderi.getTotalPay();

            orderCount.updateOrderNoAttachmentStr(storeOrderf);
        }

        if (type == 0) {
            if (storeOrderf.getParentId()>storeOrderf.getOrderNo()){
                storeOrder.setOriginalPrice(originalPrice);
                orderCount.updateStore(storeOrder);
            }
            storeOrder.setOrderStatus(50);
            //全部发货
            deliverGoods(orderNo, expressInfo, expressNo, remark, storeOrder);
        } else {

            //  根据订单号查询订单是否支持分批发货
            Long parentId = storeOrder.getParentId();

            // 母订单id 0:其他  1:已拆分订单, OrderNo:没有子订单
            if (storeOrder.getParentId() == 1) {
                return Response.errorMsg("发货失败,该订单不再支持分批发货");
            }

            /**
             * 先修改母订单的状态
             * setOrderStatus=10 已付款(待发货)  115(发货中)
             */
            storeOrder.setOrderStatus(10);
            //被拆分过所以将订单改为被拆分过的订单 下次不可再被拆分

                storeOrder.setParentId(1L);

            long currentTimeMillis = System.currentTimeMillis();
            //修改母订单更新时间
            storeOrder.setUpdateTime(currentTimeMillis);
            //对分批后的子订单形成新的订单
            StoreOrder storeOrder1 = orderCount.selectOrderStatus(orderNo);
            //订单状态改为已发货
            storeOrder1.setOrderStatus(50);
            //将主键设置为null
            storeOrder1.setOrderNo(null);
            //修改该订单的parentId为母订单的订单号
            storeOrder1.setParentId(orderNo);
            //修改订单的创建和更新时间
            storeOrder1.setUpdateTime(currentTimeMillis);
            storeOrder1.setCreateTime(currentTimeMillis);
            //在订单列表中插入此订单
            //int save = MybatisOperaterUtil.getInstance().save(storeOrder1);
            orderCount.insertNewOrderNo(storeOrder1);
            Long newOrderNo = storeOrder1.getOrderNo();
            if (newOrderNo==0){
                List<Long> storeOrderList = orderCount.selectByParentIdNew(orderNo);
                int size = storeOrderList.size();
                Long aLong = storeOrderList.get(size - 1);
                storeOrder1.setOrderNo(aLong);
                newOrderNo=aLong;
            }
            //总数量
            Integer totalBuyCount = 0;
            //总邮费价钱
            Double totalExpress = 0.00;
            Double totalExpress1 = 0.00;

            storeOrder1.setTotalMoney(0.00);
            storeOrder1.setTotalPay(0.00);
            storeOrder.setTotalPay(0.00);
            storeOrder.setTotalMoney(0.00);
            Double totalMoney = 0.00;
            Double totalPay = 0.00;
            JSONArray updSkuListJsonArr = JSON.parseArray(storeSendList);
            for (int i = 0; i < updSkuListJsonArr.size(); i++) {
                JSONObject skuObject = updSkuListJsonArr.getJSONObject(i);
                Long skuId = skuObject.getLong("skuId");
                int byCount = skuObject.getInteger("byCount");
                //获取对应skuId的商品价钱(针对母订单下的订单)
                StoreOrderItem storeOrderItem = orderCount.selectBySkuId(skuId, orderNo);
                StoreOrderItem storeOrderItem1 = orderCount.selectBySkuId(skuId, orderNo);
                int buyCount = storeOrderItem.getBuyCount();

                if (buyCount - byCount != 0) {
                    double c = byCount;
                    double a = buyCount;
                    double b = c / a;
                    //修改数量
                    storeOrderItem.setBuyCount(buyCount - byCount);
                    //修改优惠前总价
                    storeOrderItem.setTotalMoney(storeOrderItem.getTotalMoney() * (1 - b));

                    //总价格
                    storeOrderItem.setTotalPay(storeOrderItem.getTotalPay() * (1 - b));

                    Double totalExpressMoney = storeOrderItem.getTotalExpressMoney();
                    double v = totalExpressMoney * (1 - b);
                    //邮费总价
                    storeOrderItem.setTotalExpressMoney(storeOrderItem.getTotalExpressMoney() * (1 - b));


                    //修改更新时间
                    storeOrderItem.setUpdateTime(currentTimeMillis);
                    //在对应的商品详情表中更新
                    orderCount.updateSotrePay(storeOrderItem);
                    storeOrderItem1.setId(null);
                    storeOrderItem1.setOrderNo(newOrderNo);
                    storeOrderItem1.setUpdateTime(currentTimeMillis);
                    storeOrderItem1.setCreateTime(currentTimeMillis);
                    storeOrderItem1.setTotalMoney(storeOrderItem1.getTotalMoney() * b);
                    storeOrderItem1.setBuyCount(byCount);
                    storeOrderItem1.setTotalPay(storeOrderItem1.getTotalPay() * b);
                    storeOrderItem1.setTotalExpressMoney(storeOrderItem1.getTotalExpressMoney() * b);

                    totalExpress1 = totalExpress1 + storeOrderItem1.getTotalExpressMoney();

                    storeOrder1.setTotalPay(storeOrderItem1.getTotalPay() + storeOrder1.getTotalPay());
                    storeOrder1.setTotalMoney(storeOrderItem1.getTotalMoney() + storeOrder1.getTotalMoney());
                    //进行插入
                    orderCount.insertItem(storeOrderItem1);
                } else {
                    storeOrder.setTotalPay(storeOrderItem.getTotalPay() + storeOrder.getTotalPay());
                    storeOrder.setTotalMoney(storeOrderItem.getTotalMoney() + storeOrder.getTotalMoney());
                    //如果改商品对应的数量已经全部发完那就直接将该订单号进行修改
                    storeOrderItem.setOrderNo(newOrderNo);
                    storeOrder1.setTotalMoney(storeOrderItem.getTotalMoney());
                    storeOrder1.setTotalPay(storeOrderItem.getTotalPay());
                    storeOrder1.setOriginalPrice(storeOrder1.getTotalPay());

                    orderCount.updateItem(storeOrderItem);
                }
                StoreOrderItem storeOrderItem2 = orderCount.selectBySkuId(skuId, newOrderNo);
                totalMoney = totalMoney + storeOrderItem2.getTotalMoney();
                totalPay = totalPay + storeOrderItem2.getTotalPay();
                //获取新订单的数量
                totalBuyCount = totalBuyCount + byCount;
            }
            storeOrder1.setTotalBuyCount(totalBuyCount);
            //邮费
            storeOrder1.setTotalExpressMoney(totalExpress1);

            storeOrder1.setTotalMoney(totalMoney);
            storeOrder1.setTotalPay(totalPay);
            storeOrder1.setTotalExpressMoney(storeOrder.getTotalExpressMoney());
            if (flag){
                storeOrder1.setOriginalPrice(originalPrice);
            }else {
                storeOrder1.setOriginalPrice(totalPay);
            }
            //对订单进行更新
            orderCount.updateTotalMoney(storeOrder1);
            //开始对此订单进行发货
            deliverGoods(newOrderNo, expressInfo, expressNo, remark, storeOrder1);
            //修改母订单的数量
            storeOrder.setTotalBuyCount(storeOrder.getTotalBuyCount() - totalBuyCount);
            storeOrder.setTotalExpressMoney(totalExpress);
            storeOrder.setTotalPay(storeOrder2.getTotalPay() - totalPay);
            storeOrder.setTotalMoney(storeOrder2.getTotalMoney() - totalMoney);
            if (flag){
                storeOrder.setOriginalPrice(originalPrice);
            }else {
                storeOrder.setOriginalPrice(storeOrder.getTotalPay());
            }
            storeOrder.setTotalExpressMoney(0.00);
            //将母订单更新到表中
            orderCount.updateParentId(storeOrder);
            if (!(parentId.equals(orderNo)) && (parentId != -1)) {
                orderCount.updateParentIdStatus(parentId);
            }
        }
        return Response.success("发货成功");

    }

    /**
     * 恢复原价
     * @param orderNo
     * @param userId
     * @param
     */
    @Override
    public void changeOrderPrice(Long orderNo, Long userId) {
        StoreOrder storeOrder = orderCount.selectByOrderNo(orderNo);
        //判定该订单是否能够被该供应商修改
        if(!(storeOrder.getSupplierId().equals(userId))){
            logger.info("该供应商没有权限修改该订单！orderNo="+orderNo+"supplierId="+userId);
            throw new RuntimeException("没有权限修改该订单！");
        }
        //判断该订单是否能够改价
        if(storeOrder.getOrderStatus() != 0){
            logger.info("该订单不是未付款订单，无法改价！orderNo="+orderNo+"supplierId="+userId);
            throw new RuntimeException("该订单不是未付款订单，无法改价！");
        }
        if(storeOrder.getLockingOrder()==1){
            logger.info("该订单正在支付中，无法进行改价！orderNo="+orderNo+"supplierId="+userId);
            throw new RuntimeException("该订单正在支付中，无法恢复价格！请联系客户，取消第三方支付订单，再申请恢复价格！");
        }
        if(storeOrder.getLockingOrder()==2){
            logger.info("用户下单的APP版本低于3.5.0，不支持改价功能!orderNo="+orderNo+"supplierId="+userId);
            throw new RuntimeException("用户下单的APP版本低于3.5.0，不支持改价功能!");
        }

    }
    /**
     * 获取所有的子订单
     */
    @Override
    public List<StoreOrder> selectOrderSonAll(Long orderNo) {
        return orderCount.selectOrderSonAll(orderNo);
    }

    /**
     * 获取供应商id
     * @param userId
     * @return
     */
    @Override
    public Long getSupplierId(Long userId) {
        return null;
    }

    @Override
    public List<StoreRefundOrderActionLog> selectRefundLog(Long orderNo) {
        return orderCount.selectRefundLog(orderNo);
    }


    /**
     * 发货方法
     * @param orderNo
     * @param expressCompamyName
     * @param expressNo
     * @param remark
     */
    public void deliverGoods(long orderNo, String expressCompamyName, String expressNo, String remark,StoreOrder storeOrder){
        long time = System.currentTimeMillis();
        StoreExpressInfo storeExpressInfo = new StoreExpressInfo();
        storeExpressInfo.setOrderNo(orderNo);
        storeExpressInfo.setExpressSupplier(expressCompamyName);
        storeExpressInfo.setExpressOrderNo(expressNo);
        storeExpressInfo.setExpressUpdateTime(time);
        storeExpressInfo.setStatus(0);
        storeExpressInfo.setCreateTime(time);
        storeExpressInfo.setUpdateTime(time);
        /**
         * 将物流信息插入表内
         */
        orderCount.insert(storeExpressInfo);
        StoreOrder entity=new StoreOrder();
        entity.setOrderNo(orderNo);
        entity.setOrderStatus(50);



        /**
         * 更改母订单发货状态
         */
        Long parentId = storeOrder.getParentId();
        if (!(parentId.equals(orderNo))&&(parentId!=1)){
            orderCount.updateStatus(parentId);
        }


//			storeOrder.setExpressCompamyName(expressCompamyName);
//			storeOrder.setExpressNo(expressNo);
        entity.setRemark(remark);
        entity.setUpdateTime(time);
        entity.setSendTime(time);
        /**
         * 修改订单信息
         */
        orderCount.updateById(entity);

        //生成订单日志
        StoreOrderLog storeOrderLog = new StoreOrderLog();
        storeOrderLog.setOrderNo(storeOrder.getOrderNo());
        storeOrderLog.setStoreId(storeOrder.getStoreId());
        storeOrderLog.setOldStatus(storeOrder.getOrderStatus());
        storeOrderLog.setNewStatus(50);
        storeOrderLog.setCreateTime(System.currentTimeMillis());
        storeOrderLog.setUpdateTime(storeOrder.getUpdateTime());
        /**
         * 插入日志表
         */
        orderCount.insertLog(storeOrderLog);
        //Integer record =
//        if (record==1){
//            long storeId = storeOrder.getStoreId();
//            StoreBusiness storeBusiness = orderCount.selectByTtoreId(storeId);
//            long supplierId = storeOrder.getSupplierId();
//            UserNew userNew = orderCount.SelectBySupplierId(supplierId);
//            String userCID = storeBusiness.getUserCID();
//
//        }

    }






    //获取售后中订单个数
    public Integer getSupplierOrderCountUnDealRefundOrderCount(long userId) {
        /**
         * @Override
         * 	public Object getSupplierOrderCountUnDealRefundOrderCount(long userId) {
         * 		Wrapper<StoreOrderNew> wrapperAllCount =
         * 				new EntityWrapper<StoreOrderNew>().eq("status", 0)
         * 				.eq("supplierId",userId).eq("refund_underway",
         * 				StoreOrderNew.REFUND_UNDERWAY).gt("ParentId", 0);售后1
         * 		return supplierOrderMapper.selectCount(wrapperAllCount);
         * 	}
         */
        Integer count = orderCount.unDealRefundOrderCount(userId);
        return count;
    }



}
