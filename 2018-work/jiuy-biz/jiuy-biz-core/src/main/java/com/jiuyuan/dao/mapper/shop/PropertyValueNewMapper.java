package com.jiuyuan.dao.mapper.shop;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.PropertyValueNew;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性值
 * @author Administrator
 *
 */
@DBMaster
public interface PropertyValueNewMapper extends BaseMapper<PropertyValueNew> {

    /**
     * 查询门店用户的属性值
     *
     * @param propertyValue 属性值名称
     * @param propertyValueGroupId 属性值分组id nullable
     * @param storeId 门店id
     * @return java.util.List<com.jiuyuan.entity.newentity.PropertyValueNew>
     * @author Charlie(唐静)
     * @date 2018/7/9 11:36
     */
    List<PropertyValueNew> findStorePropValue(@Param("propertyValue") String propertyValue
            , @Param("propertyValueGroupId") Long propertyValueGroupId
            , @Param("storeId") Long storeId);

}
