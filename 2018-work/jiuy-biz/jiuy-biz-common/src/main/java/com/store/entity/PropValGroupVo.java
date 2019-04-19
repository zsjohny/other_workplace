package com.store.entity;

import com.jiuyuan.entity.newentity.PropertyValueNew;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/9 8:11
 * @Copyright 玖远网络
 */
public class PropValGroupVo{

    /**
     * 属性名id
     */

    private Long propertyNameId;

    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 分组id
     */
    private Long groupId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 属性值
     */
    private List<PropertyValueNew> propertyValues;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<PropertyValueNew> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValueNew> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Long getPropertyNameId() {
        return propertyNameId;
    }

    public void setPropertyNameId(Long propertyNameId) {
        this.propertyNameId = propertyNameId;
    }

}
