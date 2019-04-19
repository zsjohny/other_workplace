package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.entity.newentity.PropertyValueGroup;
import com.jiuyuan.entity.newentity.PropertyValueNew;

public interface IPropertySupplierService {

	/**
	 * 添加颜色
	 * @param colorName
	 * @param colorGroupId
	 * @param supplierId
	 * @return
	 */
	long addColorPropertyValue(String colorName, long colorGroupId, long supplierId);

	/**
	 * 添加尺码
	 * @param sizeName
	 * @param sizeGroupId
	 * @param supplierId
	 * @return
	 */
	long addSizePropertyValue(String sizeName, long sizeGroupId, long supplierId);

	/**
	 * 获取颜色组列表
	 * @return
	 */
	List<PropertyValueGroup> getSizeGroupList();

	/**
	 * 获取颜色组列表
	 * @return
	 */
	List<PropertyValueGroup> getColorGroupList();

	/**
	 * 获取颜色列表
	 * @return
	 */
	List<PropertyValueNew> getColorList(long supplierId);

	/**
	 * 获取尺码列表
	 * @return
	 */
	List<PropertyValueNew> getSizeList(long supplierId);

	/**
	 * 根据指定属性名ID获取属性值列表
	 * @return
	 */
	List<PropertyValueNew> getValueListByPropertyNameId(long propertyNameId, long supplierId);

}