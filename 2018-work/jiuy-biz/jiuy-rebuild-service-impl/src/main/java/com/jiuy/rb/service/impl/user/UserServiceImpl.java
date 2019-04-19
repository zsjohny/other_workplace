package com.jiuy.rb.service.impl.user;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.mapper.user.StoreBusinessRbMapper;
import com.jiuy.rb.mapper.user.SupplierCustomerGroupRbMapper;
import com.jiuy.rb.mapper.user.SupplierCustomerRbMapper;
import com.jiuy.rb.mapper.user.SupplierUserRbMapper;
import com.jiuy.rb.model.order.StoreOrderRb;
import com.jiuy.rb.model.user.*;
import com.jiuy.rb.service.order.IOrderService;
import com.jiuy.rb.service.user.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户相关的模块
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 16:04
 * @Copyright 玖远网络
 */
@Service( "userService" )
public class UserServiceImpl implements IUserService{


    @Resource( name = "storeBusinessRbMapper" )
    private StoreBusinessRbMapper storeBusinessRbMapper;

    @Resource( name = "supplierUserRbMapper" )
    private SupplierUserRbMapper supplierUserRbMapper;

    @Resource( name = "supplierCustomerGroupRbMapper" )
    private SupplierCustomerGroupRbMapper supplierCustomerGroupRbMapper;

    @Resource( name = "supplierCustomerRbMapper" )
    private SupplierCustomerRbMapper supplierCustomerRbMapper;

    @Resource(name = "orderServiceRb")
    private IOrderService orderService;

    /**
     * 通过id获取用户信息
     *
     * @param storeId storeId
     * @return StoreBusinessRb
     * @author Aison
     * @date 2018/6/28 16:09
     */
    @Override
    public StoreBusinessRb getStoreBusinessById(Long storeId) {
//        storeBusinessRbMapper.selectByPrimaryKey(storeId);
        return  storeBusinessRbMapper.selectStoreBusinessRb(storeId);
    }

    @Override
    public StoreBusinessRb getStoreBusinessByIdNumber(Long storeId,String number) {
        return storeBusinessRbMapper.selectCountByIdNumber(storeId,number);
    }

    /**
     * 获取供应商信息
     *
     * @param supplierId supplierId
     * @return com.jiuy.rb.model.user.SupplierUserRb
     * @author Aison
     * @date 2018/6/29 11:33
     */
    @Override
    public SupplierUserRb getSupplierUser(Integer supplierId) {

        return supplierUserRbMapper.selectByPrimaryKey(supplierId);
    }

    /**
     * 获取某个供应商的客户信息
     *
     * @param query query
     * @author Aison
     * @date 2018/6/28 16:14
     * @return SupplierCustomerRb
     */

    /**
     * 批量查询用户
     *
     * @param query 查询参数封装
     * @return java.util.List<com.jiuy.rb.model.user.StoreBusinessRb>
     * @author Charlie(唐静)
     * @date 2018/7/3 10:01
     */
    @Override
    public List<StoreBusinessRb> listStoreBusiness(StoreBusinessRbQuery query) {
        return storeBusinessRbMapper.selectList(query);
    }

    @Override
    public String getStoreBusinessPhoneById(Long storeId) {
        return storeBusinessRbMapper.getStoreBusinessPhoneById(storeId);
    }


    @Override
    public SupplierCustomerRb getSupplierCustomer(SupplierCustomerRbQuery query) {
        return supplierCustomerRbMapper.selectOne(query);
    }


    /**
     * 查询一个供应商的客户分组列表
     *
     * @param suppierId suppierId
     * @param orderNo   orderNo
     * @return java.util.List<com.jiuy.rb.model.user.SupplierCustomerGroupRbQuery>
     * @author Aison
     * @date 2018/6/29 15:17
     */
    @Override
    public List<SupplierCustomerGroupRbQuery> groupList(Long suppierId, Long orderNo) {

        if (suppierId == null) {
            StoreOrderRb storeOrderRb = orderService.getByOrderNo(orderNo);
            suppierId = storeOrderRb.getSupplierId();
        }
        return supplierCustomerGroupRbMapper.selectGroupBySupplierId(suppierId);

    }

    /**
     * 更新门店用户相对于供应商的用户分组
     *
     * @param query       query
     * @param userSession userSession
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     * @author Aison
     * @date 2018/6/29 15:38
     */
    @MyLogs( logInfo = "修改门店用户对应供应商的分组", model = ModelType.USER )
    @Override
    public MyLog<Long> updateStoreGroup(SupplierCustomerRbQuery query, UserSession userSession) {

        Declare.notNull(query.getId(), GlobalsEnums.PARAM_ERROR);
        SupplierCustomerRb old = supplierCustomerRbMapper.selectByPrimaryKey(query.getId());
        Declare.notNull(old, GlobalsEnums.PARAM_ERROR);

        supplierCustomerRbMapper.updateByPrimaryKeySelective(query);

        return new MyLog<>(old, query, userSession);
    }
}
