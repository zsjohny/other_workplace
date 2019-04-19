package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.newentity.ShopTagProduct;

/**
 * <p>
  * 标签商品中间表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
public interface ShopTagProductMapper extends BaseMapper<ShopTagProduct> {

	List<Map<String, Object>> selectBindTagList(@Param("storeId")long storeId, @Param("productId")long productId);

    /**
     * 根据标签获取上架商品数量
     *
     * @param tagId tagId
     * @param storeId storeId
     * @return java.lang.Integer 商品数量
     * @author Charlie
     * @date 2018/7/18 15:31
     */
    Integer getSoldOutProductCount(@Param("tagId")Long tagId, @Param("storeId")Long storeId);
}