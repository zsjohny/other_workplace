package com.jiuy.rb.model.user; 

import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import lombok.Data;

import java.util.List;

/**
 * SupplierCustomerRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 04:11:22
 * @Copyright 玖远网络 
*/
@Data
public class SupplierCustomerRbQuery extends SupplierCustomerRb {  

    /**
     * 客户分组信息
     */
    private List<SupplierCustomerGroupRbQuery> groupList;
} 
