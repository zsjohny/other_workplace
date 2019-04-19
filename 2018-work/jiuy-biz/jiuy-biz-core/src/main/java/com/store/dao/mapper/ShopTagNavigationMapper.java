package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.newentity.ShopTagNavigation;

/**
 * <p>
  * 小程序标签导航表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-11
 */
public interface ShopTagNavigationMapper extends BaseMapper<ShopTagNavigation> {
    List<ShopTagNavigation> findShopNavigationListById(@Param("storeId") long storeId);

//	List<Map<String, Object>> selectTagNavigationMap(@Param(value = "storeId")long storeId);

}