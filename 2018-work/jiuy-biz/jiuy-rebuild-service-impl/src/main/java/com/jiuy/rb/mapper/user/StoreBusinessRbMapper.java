package com.jiuy.rb.mapper.user; 
 
import com.jiuy.rb.model.user.StoreBusinessRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.user.StoreBusinessRbQuery;
import com.jiuy.rb.model.user.SupplierCustomerGroupRbQuery;
import com.jiuyuan.dao.annotation.DBMaster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 门店商家 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月28日 下午 04:08:40
 * @Copyright 玖远网络 
 */
@DBMaster
public interface StoreBusinessRbMapper extends BaseMapper<StoreBusinessRb>{

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    StoreBusinessRbQuery selectCountByIdNumber(@Param("id") Long id,@Param("number") String number);

    StoreBusinessRb selectStoreBusinessRb(@Param("storeId") Long storeId);

    /**
     * 获取手机号
     * @param storeId
     * @return
     */
    String getStoreBusinessPhoneById(@Param("storeId") Long storeId);
}