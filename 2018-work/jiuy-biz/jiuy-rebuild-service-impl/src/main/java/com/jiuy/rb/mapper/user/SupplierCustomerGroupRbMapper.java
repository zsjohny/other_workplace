package com.jiuy.rb.mapper.user; 
 
import com.jiuy.rb.model.user.SupplierCustomerGroupRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.user.SupplierCustomerGroupRbQuery;

import java.util.List;

/** 
 * 供应商分组表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月29日 下午 02:42:39
 * @Copyright 玖远网络 
 */
public interface SupplierCustomerGroupRbMapper extends BaseMapper<SupplierCustomerGroupRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    List<SupplierCustomerGroupRbQuery> selectGroupBySupplierId(Long supplierId);
}