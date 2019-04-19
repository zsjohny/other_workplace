package com.jiuy.rb.service.impl.order;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.Globals;
import com.jiuy.rb.enums.GeTuiEnum;
import com.jiuy.rb.enums.OrderRefundType;
import com.jiuy.rb.enums.OrderStatusEnum;
import com.jiuy.rb.mapper.order.StoreOrderItemRbMapper;
import com.jiuy.rb.mapper.order.StoreOrderLogRbMapper;
import com.jiuy.rb.mapper.order.StoreOrderRbMapper;
import com.jiuy.rb.mapper.order.StoreRefundOrderRbMapper;
import com.jiuy.rb.model.order.*;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.model.user.SupplierCustomerRb;
import com.jiuy.rb.model.user.SupplierCustomerRbQuery;
import com.jiuy.rb.model.user.SupplierUserRb;
import com.jiuy.rb.service.common.IAppMsgService;
import com.jiuy.rb.service.order.IExpressService;
import com.jiuy.rb.service.order.IOrderService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.user.IUserService;
import com.jiuy.rb.util.GeTuiVo;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 订单相关接口的时间类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/12 16:59
 * @Copyright 玖远网络
 */
@Service("orderServiceRb")
public class OrderServiceImpl implements IOrderService {


    private static final Log logger = LogFactory.get(OrderServiceImpl.class);

    @Resource(name = "storeOrderRbMapper")
    private StoreOrderRbMapper storeOrderRbMapper;

    @Resource(name = "userService")
    private IUserService userService;

    @Resource(name = "storeRefundOrderRbMapper")
    private StoreRefundOrderRbMapper storeRefundOrderRbMapper;

    @Resource(name = "storeOrderItemRbMapper")
    private StoreOrderItemRbMapper storeOrderItemRbMapper;

    @Resource(name = "storeOrderLogRbMapper")
    private StoreOrderLogRbMapper storeOrderLogRbMapper;

    @Resource(name = "productService")
    private IProductService productService;

    @Resource(name = "expressServiceRb")
    private IExpressService expressService;

    @Resource(name = "appMsgService")
    private IAppMsgService appMsgService;


    /**
     * 获取订单信息
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/29 15:16
     * @return com.jiuy.rb.model.order.StoreOrderRb
     */
    @Override
    public StoreOrderRb getByOrderNo(Long orderNo) {
        return storeOrderRbMapper.selectByPrimaryKey(orderNo);
    }

    /**
     * 查询订单的列表
     *
     * @param query 查询参数
     * @author Aison
     * @date 2018/6/8 14:23
     */
    @Override
    public MyPage<Map<String, Object>> orderList(StoreOrderRbQuery query) {

        return new MyPage<>(storeOrderRbMapper.selectOrderList(query));
    }

    /**
     * 修改订单的优惠券发放状态
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/8/3 18:28
     * @return int
     */
    @Override
    public int updateSendCoupon(String orderNo) {
        return storeOrderRbMapper.updateSendCoupon(orderNo);
    }


    /**
     * 获取订单详情
     *
     * @param orderNo orderNo
     * @return java.util.List<com.jiuy.rb.model.order.StoreOrderItemRb>
     * @author Aison
     * @date 2018/6/28 16:56
     */
    @Override
    public List<StoreOrderItemRbQuery> getOrderItem(Long orderNo) {

        StoreOrderItemRbQuery query = new StoreOrderItemRbQuery();
        query.setOrderNo(orderNo);
        return MyPage.copy2Child(storeOrderItemRbMapper.selectList(query), StoreOrderItemRbQuery.class, (source, target) -> {
            ProductRb productRb = productService.getById(source.getProductId());
            target.setDetailImage(productRb.getDetailImages());
            target.setProductName(productRb.getName());
            target.setClothesNumber(productRb.getClothesNumber());
        }).getRows();

    }

    /**
     * 订单详情
     *
     * @param orderId orderId
     * @return StoreOrderRbQuery
     * @author Aison
     * @date 2018/6/28 15:37
     */
    @Override
    public StoreOrderRbQuery orderInfo(Long orderId) {

        StoreOrderRb storeOrderRb = storeOrderRbMapper.selectByPrimaryKey(orderId);
        StoreOrderRbQuery query = Biz.copyBean(storeOrderRb, StoreOrderRbQuery.class);
        //商家店铺优惠
        BigDecimal supplierPreferential = query.getSupplierPreferential();
        //商家店铺改价
        BigDecimal supplierChangePrice = query.getSupplierChangePrice();
        BigDecimal supplierAddPrice = BigDecimal.ZERO;
        if (supplierChangePrice.compareTo(BigDecimal.ZERO) >= 0) {
            supplierPreferential = supplierPreferential.add(supplierChangePrice);
        } else {
            supplierAddPrice = BigDecimal.ZERO.subtract(supplierChangePrice);
        }
        query.setSupplierPreferential(supplierPreferential);
        //订单加价金额
        query.setSupplierAddPrice(supplierAddPrice);
        query.setOrderStatusName(OrderStatusEnum.getStatusName(query.getOrderStatus()));

        Long supplierId = query.getSupplierId();
        Long storeId = query.getStoreId();

        StoreBusinessRb storeBusinessRb = userService.getStoreBusinessById(storeId);

        SupplierCustomerRbQuery scQuery = new SupplierCustomerRbQuery();
        scQuery.setPhoneNumber(storeBusinessRb.getPhoneNumber());
        scQuery.setStoreId(storeId);
        scQuery.setSupplierId(supplierId);
        SupplierCustomerRb supplierCustomer = userService.getSupplierCustomer(scQuery);
        if (supplierCustomer == null) {
            query.setIsCustomer(0);
            query.setCustomerId(0L);
        } else {
            query.setIsCustomer(1);
            query.setCustomerId(supplierCustomer.getId());
        }
        query.setOrderCloseTime(query.getRealCloseTime());


        StoreRefundOrderRbQuery refundQuery = new StoreRefundOrderRbQuery();
        refundQuery.setOrderNo(query.getOrderNo());
        int count = storeRefundOrderRbMapper.selectCount(refundQuery);
        if (count > 0) {
            query.setHaveRefund(1);
        } else {
            query.setHaveRefund(0);
        }
        query.setBusinessName(storeBusinessRb.getBusinessName());

        query.setStoreOrderItemList(getOrderItem(query.getOrderNo()));

        ExpressInfoRbQuery expressInfo = expressService.orderExpressInfo(query.getOrderNo());
        if(expressInfo!=null) {
            query.setExpressCompanyName(expressInfo.getExpressSupplier());
            query.setExpressCnName(expressInfo.getExpressCnName());
        }
        return query;
    }


    /**
     * 修改订单信息
     *
     * @param storeOrderRb storeOrderRb
     * @author Aison
     * @date 2018/6/29 10:24
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    @Override
    @MyLogs(logInfo = "修改订单",model = ModelType.ORDER_MODEL)
    public MyLog<Long> updateOrder(StoreOrderRb storeOrderRb, UserSession userSession) {

        Declare.notNull(storeOrderRb.getOrderNo(),"订单号为空");
        StoreOrderRb old = storeOrderRbMapper.selectByPrimaryKey(storeOrderRb.getOrderNo());
        Declare.notNull(old,GlobalsEnums.PARAM_ERROR);

        storeOrderRbMapper.updateByPrimaryKeySelective(storeOrderRb);
        return new MyLog<>(old,storeOrderRb,userSession);
    }

    /**
     * 发货
     *
     * @param query 查询条件 如果是供应商的则赋值供应商值
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 10:46
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @MyLogs(logInfo = "订单发货",model = ModelType.ORDER_MODEL)
    public MyLog<Long> deliverOrder(StoreOrderRbQuery query, UserSession userSession) {

        Long orderNo = query.getOrderNo();
        String expressCompanyName = query.getExpressCompanyName();
        String remark = query.getRemark();
        String expressNo = query.getExpressNo();

        Declare.noNullParams(orderNo,expressCompanyName,remark,expressNo);
        // 新建一个查询对象防止参数污染
        StoreOrderRbQuery newQuery = new StoreOrderRbQuery();
        newQuery.setOrderNo(orderNo);
        newQuery.setSupplierId(query.getSupplierId());
        StoreOrderRb order =  storeOrderRbMapper.selectOne(newQuery);
        Declare.notNull(order,GlobalsEnums.ORDER_NOT_FOUND);
        Integer refundType = order.getRefundUnderway();
        // 是否在售后中
        if(OrderRefundType.IN_SERVER.isThis(refundType)){
            throw BizException.instance(GlobalsEnums.ORDER_IS_IN_SERVER);
        }
        Integer orderStatus = order.getOrderStatus();

        MyLog<Long> mylog = new MyLog<>(userSession);
        // 是否是支付完成的
        if(OrderStatusEnum.PAID.isThis(orderStatus)){

            // 添加一条订单物流记录
            long time = System.currentTimeMillis();
            ExpressInfoRb expressInfo = new ExpressInfoRb();
            expressInfo.setOrderNo(orderNo);
            expressInfo.setExpressSupplier(expressCompanyName);
            expressInfo.setExpressOrderNo(expressNo);
            expressInfo.setExpressUpdateTime(time);
            expressInfo.setStatus(0);
            expressInfo.setCreateTime(time);
            expressInfo.setUpdateTime(time);
            expressService.addExpressInfo(expressInfo,userSession);


            // 更新订单状态
            StoreOrderRb entity = new StoreOrderRb();
            entity.setOrderNo(orderNo);
            entity.setOrderStatus(OrderStatusEnum.DELIVER.getCode());
            entity.setRemark(remark);
            entity.setUpdateTime(time);
            entity.setSendTime(time);
            storeOrderRbMapper.updateByPrimaryKeySelective(entity);
            mylog.moreLog(order,entity,MyLog.Type.up);

            //生成订单日志
            StoreOrderLogRb storeOrderLog = new StoreOrderLogRb();
            storeOrderLog.setOrderNo(order.getOrderNo());
            storeOrderLog.setStoreId(order.getStoreId());
            storeOrderLog.setOldStatus(order.getOrderStatus());
            storeOrderLog.setNewStatus(OrderStatusEnum.DELIVER.getCode());
            storeOrderLog.setCreateTime(System.currentTimeMillis());
            int record = storeOrderLogRbMapper.insertSelective(storeOrderLog);
            mylog.moreLog(storeOrderLog,MyLog.Type.add);

            // 发送个推
            if(record==1){
                long storeId = order.getStoreId();
                StoreBusinessRb storeBusiness = userService.getStoreBusinessById(storeId);
                long supplierId = order.getSupplierId();
                SupplierUserRb supplierUser = userService.getSupplierUser(Integer.parseInt(supplierId+""));
                String userCID = storeBusiness.getUserCID();

                appMsgService.geTui(userCID,new GeTuiVo("[俞姐姐门店宝]您在["+supplierUser.getBusinessName()+"]采购的美丽的衣服[订单号："+orderNo+"]已经发货成功了。",
                        "",orderNo+"","", GeTuiEnum.PACKAGE_SEND.getCode(),System.currentTimeMillis()+""));
            }
        }else if( OrderStatusEnum.DELIVER.isThis(orderStatus)){
            // 已经发货订单

            // 更新订单物流信息
            long time = System.currentTimeMillis();
            ExpressInfoRb storeExpressInfoEntity = new ExpressInfoRb();
            storeExpressInfoEntity.setExpressSupplier(expressCompanyName);
            storeExpressInfoEntity.setExpressOrderNo(expressNo);
            storeExpressInfoEntity.setExpressUpdateTime(time);
            storeExpressInfoEntity.setStatus(0);
            storeExpressInfoEntity.setUpdateTime(time);
            storeExpressInfoEntity.setOrderNo(orderNo);
            expressService.updateExpressInfo(storeExpressInfoEntity,userSession);

            // 更新订单信息
            StoreOrderRb storeOrderEntity = new StoreOrderRb();
            storeOrderEntity.setOrderNo(orderNo);
            storeOrderEntity.setOrderStatus(OrderStatusEnum.DELIVER.getCode());
            storeOrderEntity.setRemark(remark);
            storeOrderEntity.setUpdateTime(time);
            storeOrderEntity.setSendTime(time);
            storeOrderRbMapper.updateByPrimaryKeySelective(storeOrderEntity);
            mylog.moreLog(order,storeOrderRbMapper,MyLog.Type.add);

            //生成订单日志
            StoreOrderLogRb storeOrderLog = new StoreOrderLogRb();
            storeOrderLog.setOrderNo(order.getOrderNo());
            storeOrderLog.setStoreId(order.getStoreId());
            storeOrderLog.setOldStatus(order.getOrderStatus());
            storeOrderLog.setNewStatus(OrderStatusEnum.DELIVER.getCode());
            storeOrderLog.setCreateTime(System.currentTimeMillis());
            storeOrderLogRbMapper.insertSelective(storeOrderLog);
            mylog.moreLog(storeOrderLog,MyLog.Type.add);

        }else{
            throw BizException.instance(GlobalsEnums.ORDER_STATUS_ERROR);
        }

        return mylog;
    }


    /**
     * 取消发货
     *
     * @param order order
     * @author Aison
     * @date 2018/6/29 16:07
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    public MyLog<Long> revokeOrder(StoreOrderRbQuery order,UserSession userSession) {

        Declare.notNull(order.getOrderNo(),GlobalsEnums.PARAM_ERROR);
        StoreOrderRb old =  storeOrderRbMapper.selectOne(order);
        Declare.notNull(old,GlobalsEnums.PARAM_ERROR);
        // 母订单验证
        Declare.isFailed(old.getParentId()==-1,GlobalsEnums.ORDER_IS_PARENT);

        // 订单状态验证
        Integer orderStatus = old.getOrderStatus();
        Declare.isFailed(!OrderStatusEnum.DELIVER.isThis(orderStatus),GlobalsEnums.ORDER_NOT_AT_DELIVER);


        Set<Long> orderNos = new HashSet<>();
        orderNos.add(order.getOrderNo());

        if(order.getMergedId() == -1){
            List<StoreOrderRb> storeOrders = storeOrderRbMapper.childOrders(orderNos);
            storeOrders.forEach(action->{
                orderNos.add(action.getOrderNo());
            });
        }



        StoreOrderRb upOrder = new StoreOrderRb();
        upOrder.setOrderNo(order.getOrderNo());
        upOrder.setUpdateTime(System.currentTimeMillis());
        upOrder.setOrderStatus(OrderStatusEnum.PAID.getCode());
        storeOrderRbMapper.updateByPrimaryKeySelective(upOrder);


        //生成订单日志
        StoreOrderLogRb storeOrderLog = new StoreOrderLogRb();
        storeOrderLog.setOrderNo(order.getOrderNo());
        storeOrderLog.setStoreId(order.getStoreId());
        storeOrderLog.setOldStatus(order.getOrderStatus());
        storeOrderLog.setNewStatus(OrderStatusEnum.PAID.getCode());
        storeOrderLog.setCreateTime(System.currentTimeMillis());
        storeOrderLogRbMapper.insertSelective(storeOrderLog);



        //逻辑删除物流记录
        expressService.removeExpress(orderNos,userSession);

        //StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), 1, "撤销已发货", "");
        //storeOrderMessageBoardService.add(storeOrderMessageBoard);

        return null;
    }


}
