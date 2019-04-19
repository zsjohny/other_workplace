/**
 *
 */
package com.jiuyuan.service.common;

import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.shop.PropertyNameNewMapper;
import com.jiuyuan.dao.mapper.shop.PropertyValueGroupMapper;
import com.jiuyuan.dao.mapper.shop.PropertyValueNewMapper;
import com.jiuyuan.entity.newentity.PropertyValueGroup;
import com.jiuyuan.entity.newentity.PropertyValueNew;
import org.springframework.util.ObjectUtils;

/**
 * 供应商平台专用商品服务
 */

@Service
public class PropertySupplierService implements IPropertySupplierService{
    private static final Logger logger = LoggerFactory.getLogger(PropertySupplierService.class);

    private static final long colorPropertyNameId = 7;//颜色属性名ID
    private static final long sizePropertyNameId = 8;//尺寸属性名ID
    private static final int status_del = - 1;//状态:-1删除，0正常
    private static final int status_normal = 0;//状态:-1删除，0正常


    @Autowired
    private PropertyValueNewMapper propertyValueNewMapper;
    @Autowired
    private PropertyValueGroupMapper propertyValueGroupMapper;
    @Autowired
    private PropertyNameNewMapper propertyNameNewMapper;

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#addColorPropertyValue(java.lang.String, long, long)
     */
    @Override
    public long addColorPropertyValue(String colorName, long colorGroupId, long supplierId) {
        long colorId = addPropertyValue(colorName, colorGroupId, colorPropertyNameId, supplierId);
        return colorId;
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#addSizePropertyValue(java.lang.String, long, long)
     */
    @Override
    public long addSizePropertyValue(String sizeName, long sizeGroupId, long supplierId) {
        long sizeId = addPropertyValue(sizeName, sizeGroupId, sizePropertyNameId, supplierId);
        return sizeId;
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#getSizeGroupList()
     */
    @Override
    public List<PropertyValueGroup> getSizeGroupList() {
        return getGroupListByPropertyNameId(sizePropertyNameId);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#getColorGroupList()
     */
    @Override
    public List<PropertyValueGroup> getColorGroupList() {
        return getGroupListByPropertyNameId(colorPropertyNameId);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#getColorList(long)
     */
    @Override
    public List<PropertyValueNew> getColorList(long supplierId) {
        return getValueListByPropertyNameId(colorPropertyNameId, supplierId);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#getSizeList(long)
     */
    @Override
    public List<PropertyValueNew> getSizeList(long supplierId) {
        return getValueListByPropertyNameId(sizePropertyNameId, supplierId);
    }


    /**
     * 根据指定属性名ID获取属性值组列表
     *
     * @return
     */
    private List<PropertyValueGroup> getGroupListByPropertyNameId(long propertyNameId) {
        Wrapper<PropertyValueGroup> wrapper = new EntityWrapper<PropertyValueGroup>();
        wrapper.eq("PropertyNameId", propertyNameId);
        wrapper.eq("Status", status_normal);
        wrapper.orderBy("OrderIndex", true);
        List<PropertyValueGroup> list = propertyValueGroupMapper.selectList(wrapper);
        return list;
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IPropertySupplierService#getValueListByPropertyNameId(long, long)
     */
    @Override
    public List<PropertyValueNew> getValueListByPropertyNameId(long propertyNameId, long supplierId) {
        Wrapper<PropertyValueNew> wrapper = new EntityWrapper<PropertyValueNew>();
        wrapper.eq("PropertyNameId", propertyNameId);
        wrapper.eq("Status", status_normal);
        wrapper.in("supplierId", new String[]{"0", String.valueOf(supplierId)});
        wrapper.orderBy("OrderIndex", true);
        List<PropertyValueNew> list = propertyValueNewMapper.selectList(wrapper);
        return list;
    }

    /**
     * 添加属性值
     *
     * @param propertyValue
     * @param propertyValueGroupId
     * @param supplierId
     * @return
     */
    private long addPropertyValue(String propertyValue, long propertyValueGroupId, long propertyNameId, long supplierId) {
        long time = System.currentTimeMillis();
        PropertyValueNew value = new PropertyValueNew();
        value.setPropertyValue(propertyValue);//属性值
        value.setPropertyNameId(propertyNameId);//对应属性名id
        value.setStatus(status_normal);//状态:-1删除，0正常
        value.setOrderIndex(0);//排序索引
        value.setSupplierId(supplierId);//供应商Id
        value.setPropertyValueGroupId(propertyValueGroupId);//属性值组ID
        value.setCreateTime(time);//创建时间
        value.setUpdateTime(time);//更新时间
        propertyValueNewMapper.insert(value);
        return value.getId();
    }


}
