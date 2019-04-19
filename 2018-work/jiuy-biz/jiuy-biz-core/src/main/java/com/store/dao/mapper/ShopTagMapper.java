package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.newentity.ShopTag;

/**
 * <p>
  * 门店标签表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
public interface ShopTagMapper extends BaseMapper<ShopTag> {

	@Deprecated
	List<Map<String, Object>> selectShopTagMap(@Param("storeId")Long storeId);


	/**
	 * 获取用户标签信息,包括统计信息
	 *
	 * @param storeId 用户id
	 * @param soldOuts 商品的状态 0：草稿，1：上架， 2：下架
	 * @return java.util.List
	 * @author Charlie(唐静)
	 * @date 2018/7/10 16:46
	 */
	List<Map<String,Object>> getTagMapV377(@Param("storeId")Long storeId, @Param("soldOuts")List<Integer> soldOuts);
}