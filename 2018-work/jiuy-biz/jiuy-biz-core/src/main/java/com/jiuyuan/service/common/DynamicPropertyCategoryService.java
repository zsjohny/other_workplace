package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.dynamicproperty.DynamicPropertyCategoryMapper;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicProperty;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;

/**
 * <p>
 * 动态属性与分类关系表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@Service
public class DynamicPropertyCategoryService implements IDynamicPropertyCategoryService{
    @Autowired
    private IDynamicPropertyService dynamicPropertyService;
    @Autowired
    private DynamicPropertyCategoryMapper dynamicPropertyCategoryMapper;

    @Override
    public List<Map<String, Object>> getChoosedDynamicProperty(long cateGoryId, int status) {
        List<Map<String, Object>> list = dynamicPropertyCategoryMapper.getChoosedDynamicProperty(cateGoryId, status);
        return list;
    }

    @Override
    public Map<String, Object> getDynamicPropertyAndGroup(List<Map<String, Object>> dynamicPropertyCategorylist,
                                                          List<DynamicPropertyGroup> dynaPropGroupList) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for (DynamicPropertyGroup dynamicPropertyGroup : dynaPropGroupList) {
            Wrapper<DynamicProperty> wrapper = new EntityWrapper<DynamicProperty>().eq("dyna_prop_group_id", dynamicPropertyGroup.getId()).eq("status", 1).orderBy("weight", false);
            //获取动态属性组下的属性
            List<DynamicProperty> dynamicPropertyList = dynamicPropertyService.getDynamicProperty(wrapper);
            if (dynamicPropertyList.size() > 0) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("dynaPropGroupName", dynamicPropertyGroup.getName());
                map1.put("dynaPropGroupId", dynamicPropertyGroup.getId());
                List<Map<String, Object>> list1 = new ArrayList<>();//TODO
                for (DynamicProperty dynamicProperty : dynamicPropertyList) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("dynaPropName", dynamicProperty.getName());
                    map2.put("dynaPropId", dynamicProperty.getId());
                    map2.put("remark", dynamicProperty.getRemark());
                    map2.put("choosed", 0);
                    //判断该属性是否与当前分类绑定0：否 ，1：是
                    for (Map<String, Object> map3 : dynamicPropertyCategorylist) {
                        if (map3.get("dynaPropId") == dynamicProperty.getId()) {
                            map2.put("choosed", 1);
                        }
                    }
                    list1.add(map2);
                }
                map1.put("dynaPropList", list1);
                list.add(map1);
            }
            map.put("choosedDynaProp", dynamicPropertyCategorylist);
            map.put("dynamicPropertyAndGroup", list);
        }
        return map;
    }

    /**
     * 启用或者停用分类与动态属性的关联关系
     */
    @Transactional( rollbackFor = Exception.class )
    public void update(DynamicPropertyCategory dynamicPropertyCategory) {
        dynamicPropertyCategoryMapper.updateById(dynamicPropertyCategory);
    }

    /**
     * 根据id查询
     */
    public DynamicPropertyCategory selectById(long id) {
        DynamicPropertyCategory dynamicPropertyCategory = dynamicPropertyCategoryMapper.selectById(id);
        return dynamicPropertyCategory;
    }

    /**
     * 添加分类与动态属性关联关系
     */
    @Transactional( rollbackFor = Exception.class )
    public void insert(DynamicPropertyCategory dynamicPropertyCategory) {
        dynamicPropertyCategoryMapper.insert(dynamicPropertyCategory);
    }

    /**
     * 获取动态属性与分类关系列表
     */
    public List<DynamicPropertyCategory> selectList(Wrapper<DynamicPropertyCategory> wrapper) {
        List<DynamicPropertyCategory> dynamicPropertyCategoryList = dynamicPropertyCategoryMapper.selectList(wrapper);
        return dynamicPropertyCategoryList;
    }

    /**
     * 动态属性与分类关联关系数量
     */
    public int selectCount(Wrapper<DynamicPropertyCategory> wrapper) {
        return dynamicPropertyCategoryMapper.selectCount(wrapper);
    }



}
